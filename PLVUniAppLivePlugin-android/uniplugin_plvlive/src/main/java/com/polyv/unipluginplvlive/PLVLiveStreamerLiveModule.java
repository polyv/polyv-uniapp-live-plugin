package com.polyv.unipluginplvlive;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.easefun.polyv.livecommon.module.config.PLVLiveChannelConfig;
import com.easefun.polyv.livecommon.module.config.PLVLiveChannelConfigFiller;
import com.easefun.polyv.livecommon.module.config.PLVLiveScene;
import com.easefun.polyv.livecommon.module.utils.result.PLVLaunchResult;

import com.easefun.polyv.livecommon.ui.widget.PLVConfirmDialog;
import com.easefun.polyv.livestreamer.scenes.PLVLSLiveStreamerActivity;
import com.easefun.polyv.streameralone.scenes.PLVSAStreamerAloneActivity;

import com.plv.foundationsdk.permission.PLVFastPermission;
import com.plv.foundationsdk.permission.PLVOnPermissionCallback;
import com.plv.livescenes.config.PLVLiveChannelType;
import com.plv.livescenes.feature.login.IPLVSceneLoginManager;
import com.plv.livescenes.feature.login.PLVSceneLoginManager;
import com.plv.livescenes.feature.login.model.PLVLoginVO;
import com.polyv.unipluginplvlive.utils.JsonOptionUtil;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;

import java.util.ArrayList;
import java.util.List;

public class PLVLiveStreamerLiveModule extends WXModule {

    private String TAG = "PLVLiveStreamerLiveModule";

    //manager
    private final IPLVSceneLoginManager loginManager = new PLVSceneLoginManager();
    private IPLVSceneLoginManager loginSManager;

    // <editor-fold defaultstate="collapsed" desc="登录开播">
    @JSMethod(uiThread = true)
    public void loginStreamer(JSONObject options, final JSCallback callback) {
        final String channelId =  JsonOptionUtil.getString(options, "channelId", "").trim();
        final String password =  JsonOptionUtil.getString(options, "password", "").trim();
        if (TextUtils.isEmpty(channelId) || TextUtils.isEmpty(password) ) {
            objectCallback(false, "频道号和密码 不能为空", callback);
            return;
        }

        if (loginSManager == null) {
            loginSManager = new PLVSceneLoginManager();
        }
        final String nickname =  JsonOptionUtil.getString(options, "nickname", "");
        loginSManager.loginStreamerNew(channelId, password, new IPLVSceneLoginManager.OnStringCodeLoginListener<PLVLoginVO>() {
            @Override
            public void onLoginSuccess(final PLVLoginVO loginVO) {
                //更新开播状态
                PLVLiveChannelConfigFiller.setLiveStreamingWhenLogin(loginVO.isLiveStatus());

                //不填写登录昵称时，使用登录接口返回的后台设置的昵称
                final String loginNick = TextUtils.isEmpty(nickname) ? loginVO.getTeacherNickname() : "nick";

                PLVLiveChannelType liveChannelType = loginVO.getLiveChannelTypeNew();
                if (PLVLiveChannelType.PPT.equals(liveChannelType)) {
                    //进入手机开播三分屏场景
                    final boolean isOpenCamera = "N".equals(loginVO.getIsOnlyAudio());
                    requireStreamerPermissionThenRun(
                            isOpenCamera,
                            true,
                            new Runnable() {
                                @Override
                                public void run() {
                                    PLVLaunchResult launchResult = PLVLSLiveStreamerActivity.launchStreamer(
                                            (Activity) mWXSDKInstance.getContext(),
                                            loginVO.getChannelId(),
                                            loginVO.getInteractUid(),
                                            loginNick,
                                            loginVO.getTeacherAvatar(),
                                            loginVO.getTeacherActor(),
                                            loginVO.getRole(),
                                            loginVO.getColinMicType(),
                                            true,
                                            isOpenCamera,
                                            true
                                    );
                                    if (!launchResult.isSuccess()) {
                                        onLoginFailed(launchResult.getErrorMessage(), launchResult.getError());
                                    }
                                }
                            }
                    );
                } else if (PLVLiveChannelType.ALONE.equals(liveChannelType)) {
                    //进入手机开播纯视频场景
                    requireStreamerPermissionThenRun(
                            true,
                            true,
                            new Runnable() {
                                @Override
                                public void run() {
                                    PLVLaunchResult launchResult = PLVSAStreamerAloneActivity.launchStreamer(
                                            (Activity) mWXSDKInstance.getContext(),
                                            loginVO.getChannelId(),
                                            loginVO.getInteractUid(),
                                            loginNick,
                                            loginVO.getTeacherAvatar(),
                                            loginVO.getTeacherActor(),
                                            loginVO.getChannelName(),
                                            loginVO.getRole(),
                                            loginVO.getColinMicType()
                                    );
                                    if (!launchResult.isSuccess()) {
                                        onLoginFailed(launchResult.getErrorMessage(), launchResult.getError());
                                    }
                                }
                            }
                    );
                } else {
                    String errorMsg = ((Activity) mWXSDKInstance.getContext()).getResources().getString(R.string.plv_scene_login_toast_streamer_no_support_type);
                    onLoginFailed(errorMsg, new Throwable(errorMsg));
                }
            }

            @Override
            public void onLoginFailed(String msg, Throwable throwable) {
                onLoginFailed(msg, "", throwable);
            }

            @Override
            public void onLoginFailed(String msg, String code, Throwable throwable) {
                throwable.printStackTrace();

            }
        });
    }

    // </editor-fold >

    // <editor-fold defaultstate="collapsed" desc="内部方法">

    /***
     * callback 数据整合
     * isSuccess 成功或者失败
     * errMsg 返回的消息提示
     */
    private void objectCallback(Boolean isSuccess, String errMsg, final JSCallback callback){
        if (callback == null) return;
        JSONObject result = new JSONObject();
        result.put("isSuccess", isSuccess);
        result.put("errMsg", errMsg);
        callback.invoke(result);
    }

    private void requireStreamerPermissionThenRun(
            final boolean camera,
            final boolean audio,
            final Runnable runnable
    ) {
        final List<String> permissions = new ArrayList<>();
        if (camera) {
            permissions.add(Manifest.permission.CAMERA);
        }
        if (audio) {
            permissions.add(Manifest.permission.RECORD_AUDIO);
        }
        if (permissions.isEmpty() || PLVFastPermission.hasPermission(mWXSDKInstance.getContext(), permissions)) {
            runnable.run();
            return;
        }

        PLVFastPermission.getInstance().start((Activity) mWXSDKInstance.getContext(), permissions, new PLVOnPermissionCallback() {
            @Override
            public void onAllGranted() {
                runnable.run();
            }

            @Override
            public void onPartialGranted(ArrayList<String> grantedPermissions, ArrayList<String> deniedPermissions, ArrayList<String> deniedForeverP) {
                final boolean hasCameraPermission = PLVFastPermission.hasPermission(mWXSDKInstance.getContext(), Manifest.permission.CAMERA);
                final boolean hasAudioPermission = PLVFastPermission.hasPermission(mWXSDKInstance.getContext(), Manifest.permission.RECORD_AUDIO);
                if (hasCameraPermission && hasAudioPermission) {
                    runnable.run();
                    return;
                }
                final String notGrantedPermissionDescription;
                if (hasCameraPermission) {
                    notGrantedPermissionDescription = "麦克风";
                } else if (hasAudioPermission) {
                    notGrantedPermissionDescription = "摄像头";
                } else {
                    notGrantedPermissionDescription = "摄像头和麦克风";
                }

                new PLVLoginStreamerConfirmDialog(mWXSDKInstance.getContext())
                        .setTitle(notGrantedPermissionDescription + "权限被禁止")
                        .setContent("参与直播需要" + notGrantedPermissionDescription + "权限，请前往系统设置开启权限")
                        .setLeftButtonText("取消")
                        .setLeftBtnListener(new PLVConfirmDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, View v) {
                                dialog.dismiss();
                            }
                        })
                        .setRightButtonText("前往设置")
                        .setRightBtnListener(new PLVConfirmDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, View v) {
                                PLVFastPermission.getInstance().jump2Settings(mWXSDKInstance.getContext());
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
    }
    private static class PLVLoginStreamerConfirmDialog extends PLVConfirmDialog {

        public PLVLoginStreamerConfirmDialog(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.plv_login_streamer_confirm_window_layout;
        }

        @Override
        protected float dialogWidthInDp() {
            return 260;
        }
    }
}
