# Polyv云直播uni 插件

## 简介

该插件封装了保利威 Android 与 iOS 原生多场景 Demo SDK。使用本模块可以轻松把保利威 Android 与 iOS 多场景功能集成到自己的 App 中。目前主要集成了云课堂场景、直播带货场景、手机开播场景。

- 云课堂场景：

  实现保利威视频教学直播、ppt 在线演示同步播放、教学连麦、在线聊天功能，以及直播回放功能。

- 直播带货场景：

  实现视频直播、在线聊天、打赏、商品等功能，非常适合直播带货的应用场景。
  
- 手机开播场景：

  三分屏和纯视频开播推流、在线聊天、连麦、PPT文档和白板等功能，非常适合直播视频教学的应用场景。

  


## 快速使用

### 1. <a href="https://my.polyv.net/v3/register/?sales=show&f=dcloud_uniapp" target="_blank">注册账号</a>，即可获取开发者配置参数。注册的时候，记得填写邀请人dcloud。

![注册页](https://demo.ipolyv.cn/tangjianguo/polyv/dcloud/register.png)

注册链接：https://my.polyv.net/v3/register/?sales=show&f=dcloud_uniapp

### 2. 获取参数。在注册完成后，登录后台，找到开发者信息，得到appId、userId、appSecret三个关键参数。（实际场景请务必进行加密处理）

![参数](https://demo.ipolyv.cn/tangjianguo/polyv/dcloud/console.png)

## **技术支持**

如有不明白的地方，建议扫码添加我们技术支持，协助接入和测试

<table>
<thead>
<tr>
<th>技术支持</th>
<th>开发者</th>
</tr>
</thead>
<tbody>
<tr>
<td><img src="https://demo.ipolyv.cn/tangjianguo/polyv/dcloud/contactUS1.png" width="180px" alt="参数"></td>
<td><img src="https://demo.ipolyv.cn/tangjianguo/polyv/dcloud/contactUS2.png" width="180px" alt="参数"></td>
</tr>
</tbody>
</table>

## API说明

### 观看端

#### 引入插件

```
var playModule = uni.requireNativePlugin("PLV-LiveUniPlugin-WatchPlayModule")
var configModule = uni.requireNativePlugin("PLV-LiveUniPlugin-WatchConfigModule")
```

#### 配置模块 - ConfigModule

ConfigModule 封装了账号信息、用户信息、SDK配置功能。
开发者要播放保利威视频，需先到 [保利威官网](https://www.polyv.net/?f=dcloud_uniapp-211019) 注册账号，登录账号后，进入**云直播 - 开发设置** 获取 `userId`、 `AppID`、 `AppSecret`，并将加密得到加密串放到自己的服务器，再在移动端通过网络获取加密串，app 本地解密，并设置给 `setConfig` 方法。配置模块信息如果没有特殊说明都需要在进入直播间前配置。

##### 1. setViewerInfo

配置直播间用户信息。（直播间用户的属性在初始化后不允许修改）
方法：`setViewerInfo()`

示例：

```vue
configModule.setViewerInfo({
	viewerId:"",
	viewerName: "",
	viewerAvatar: "",
}, (result) => {
})
```

**参数：**

| 名称                | 类型                                       | 说明                                         |
| :------------------ | :----------------------------------------- | :------------------------------------------- |
| params.viewerId     | String                                     | 对应观看日志中的 用户ID                      |
| params.viewerName   | String                                     | 对应观看日志中的 用户昵称                    |
| params.viewerAvatar | String                                     | 观看用户的头像                               |
| callback            | function `{"isSuccess": 0, @"errMsg": ""}` | 执行结果的callback，返回是否成功的状态和描述 |

##### 2. setConfig

设置多场景的账号属性。**为防止APP端被嗅探和反编译到开发者配置信息，强烈建议开发者设计自己的加密方式在服务端对 `userId`、 `AppID`、 `AppSecret`进行加密，并通过https协议的接口获取后重新解密，再将参数传递给SDK。**
方法：`setConfig()`

示例：

```
configModule.setConfig({
	appId: "",
	userId: "",
	appSecret: ""
}, (result) => {
});
```

**参数：**

| 名称                     | 类型                                       | 说明                                         |
| :----------------------- | :----------------------------------------- | :------------------------------------------- |
| params.appId `(必需)`    | String                                     | polyv 云直播应用ID （可在云直播后台插件）    |
| params.userId`(必需)`    | String                                     | polyv 云直播账号ID                           |
| params.appSecret`(必需)` | String                                     | 用户头像地址polyv 云直播应用密匙             |
| callback                 | function `{"isSuccess": 0, @"errMsg": ""}` | 执行结果的callback，返回是否成功的状态和描述 |

##### 3. setMarqueeConfig

设置自定义跑马灯的配置属性
方法：`setMarqueeConfig()`

示例

```
configModule.setMarqueeConfig({
		code: "",
}, (result) => {
})
```

**参数：**

| 名称        | 类型                                       | 说明                                         |
| :---------- | :----------------------------------------- | :------------------------------------------- |
| params.code | String                                     | 跑马灯code参数配置                           |
| callback    | function `{"isSuccess": 0, @"errMsg": ""}` | 执行结果的callback，返回是否成功的状态和描述 |

#### 播放模块 - PlayModule

playModule 封装了云课堂、直播带货的直播和回放功能。

##### 4. showFullScreenButtonOnIPad

是否在iPad上显示全屏按钮（仅在iPad云课堂场景），需要在进入房间前调用

方法：`showFullScreenButtonOnIPad()`

示例：

```
playModule.showFullScreenButtonOnIPad({show:true});
```

**参数：**

| 名称          | 类型                               | 说明 |
| :------------ | :--------------------------------- | :--- |
| show `(必需)` | Bool(true 显示全屏按钮，false显示) |      |

##### 5. loginLiveRoom

直播登录（云课堂场景，带货直播场景登录）

方法：`loginLiveRoom()`

示例：

```javascript
// 直播
playModule.loginLiveRoom(1, {
	channelId: "",
	liveParam4: "",
	liveParam5: ""
}, (result) => {
});
```

**参数：**

| 名称                        | 类型                                       | 说明                                         |
| :-------------------------- | :----------------------------------------- | :------------------------------------------- |
| sceneType `(必需)`          | Number(1云课堂场景, 2直播带货场景)         | 直播室类型                                   |
| params.channelId`(必需)`    | String                                     | 直播的频道号                                 |
| params.liveParam4`(非必需)` |                                            |                                              |
| params.liveParam5`(非必需)` |                                            |                                              |
| callback                    | function `{"isSuccess": 0, @"errMsg": ""}` | 执行结果的callback，返回是否成功的状态和描述 |

##### 6. loginPlaybackRoom

登录回放直播间（云课堂，带货直播回放登录）

方法：`loginPlaybackRoom()`

示例：

```javascript
playModule.loginPlaybackRoom(0, {
	channlId: "",
	liveParam4: "",
	liveParam5: "",
	videoId: "",
	vodType: 0
}, (result) => {
});
```

**参数：**

| 名称                        | 类型                                       | 说明                                         |
| :-------------------------- | :----------------------------------------- | :------------------------------------------- |
| sceneType `(必需)`          | Number(1云课堂场景, 2直播带货场景)         | 直播室类型                                   |
| params.channelId`(必需)`    | String                                     | 直播的频道号                                 |
| params.liveParam4`(非必需)` |                                            |                                              |
| params.liveParam5`(非必需)` |                                            |                                              |
| params.videoId`(必需)`      | String                                     | 回放视频id                                   |
| params.vodType`(必需)`      | Number 0回放视频 1回放列表                 |                                              |
| callback                    | function `{"isSuccess": 0, @"errMsg": ""}` | 执行结果的callback，返回是否成功的状态和描述 |

### 开播端

#### 引入插件

```
var liveModule = uni.requireNativePlugin("PLV-LiveUniPlugin-StreamerLiveModule")
var configModule = uni.requireNativePlugin("PLV-LiveUniPlugin-StreamerConfigModule")
```

#### 配置模块 - ConfigModule

##### 1. setAppGroup【仅iOS有效】

屏幕共享帐号配置的App Group
方法：`setAppGroup()`

示例：

```vue
configModule.setAppGroup({
	appGroup:""
}, (result) => {
})
```

**参数：**

| 名称            | 类型                                       | 说明                                         |
| :-------------- | :----------------------------------------- | :------------------------------------------- |
| params.appGroup | String                                     | 帐号配置的App Group                          |
| callback        | function `{"isSuccess": 0, @"errMsg": ""}` | 执行结果的callback，返回是否成功的状态和描述 |

#### 直播模块 - LiveModule

登录开播推流

方法：`loginStreamer()`

示例：

```javascript
playModule.loginStreamer({
	channelId: "",
	password: "",
	nickname: ""
}, (result) => {
});
```

**参数：**

| 名称                | 类型                                       | 说明                                         |
| :------------------ | :----------------------------------------- | :------------------------------------------- |
| `channelId` (必需)  | String                                     | 登录频道号                                   |
| `password` (必需)   | String                                     | 登录密码                                     |
| `nickname` (非必需) | String                                     | 昵称                                         |
| callback            | function `{"isSuccess": 0, @"errMsg": ""}` | 执行结果的callback，返回是否成功的状态和描述 |

## 注意事项

* **观看端请务必通过 ConfigModule 的 setViewerInfo 设置观众ID，观众ID是后台区分用户的唯一标识别。**

* **如果云打包遇到问题可以移步到离线工程插件工程，进行离线打包。**

  - 离线工程：[polyv-uniapp-livescenes-plugin](https://github.com/polyv/polyv-uniapp-livescenes-plugin)

* **iOS 端需要同时集成 [Polyv UTDID 插件-iOS](https://ext.dcloud.net.cn/plugin?id=7750)。如果项目同时集成了 支付模块的支付宝支付 则不需要集成 [Polyv UTDID 插件-iOS] 插件**

* **iOS 端使用到了后台音频播放和画中画的功能，因此打包时需要在 manifest.json 中进行权限配置，可通过 manifest.json 中最下面的源码视图添加权限配置**

  ```
  {  
  "app-plus" : {  
          /* 应用发布信息 */  
          "distribute" : {  
              /* ios打包配置 */  
              "ios" : {  
                  "UIBackgroundModes" : [ "audio"] // 数组，支持多个  
              },  
          }  
      }  
  }
  ```

  详细可以参考官方文档：https://nativesupport.dcloud.net.cn/NativePlugin/course/package?id=background-modes 和 https://ask.dcloud.net.cn/article/36430

* **为防止APP端被嗅探和反编译到开发者配置信息，强烈建议开发者设计自己的加密方式在服务端对 `userId`、 `AppID`、 `AppSecret`进行加密，并通过https协议的接口获取后重新解密，再将参数传递给SDK。**