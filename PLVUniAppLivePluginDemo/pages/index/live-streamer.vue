<template>
	<view class="content" style="padding: 10rpx 20rpx;">
		<view class="live-fill-view">
			<!-- 直播UI -->
			<view>
				<input class="live-input" :value="loginInfo.channelId"
					@input="(event)=>{loginInfo.channelId = event.detail.value}" placeholder="请输入频道号" />
				<input class="live-input" :value="loginInfo.password"
					@input="(event)=>{loginInfo.password = event.detail.value}" placeholder="请输入密码" />
				<input class="live-input" :value="loginInfo.nickname"
					@input="(event)=>{loginInfo.nickname = event.detail.value}" placeholder="请输入昵称" />
			</view>
		</view>
		<button type="primary" style="padding: 0 250rpx; margin: 30rpx;" @click="loginLiveroom">登录</button>
	</view>
</template>

<script>
	var liveModule = uni.requireNativePlugin("PLV-LiveUniPlugin-StreamerLiveModule")
	var configModule = uni.requireNativePlugin("PLV-LiveUniPlugin-StreamerConfigModule")

	export default {
		data() {
			return {
				loginInfo: {
					channelId: "",
					password: "",
					nickname: "",
				}
			}
		},
		onLoad() {
			this.setAppGroup();
		},
		methods: {
			loginLiveroom() {
				liveModule.loginStreamer({
					channelId: this.loginInfo.channelId,
					password: this.loginInfo.password,
					nickname: this.loginInfo.nickname
				}, (result) => {
					uni.showToast({
						title: result.isSuccess ? "login succeed" : result.errMsg,
						icon: "none"
					})
				});	
			},
			setAppGroup() {
				const platform = uni.getSystemInfoSync().platform
				if (platform == 'ios') {
					configModule.setAppGroup({
						appGroup: "group.cn.plv.PolyvLiveScenesDemo.ScreenShare"
					}, (result) => {
					
					});	
				}
			}
		}
	}
</script>

<style>
	.content {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		height: 100%;
	}

	.tab-select-view {
		display: flex;
		width: 100%;
	}

	.tab-select-item {
		flex: 0.5;
		padding: 20rpx 0;
		text-align: center;
		color: white;
	}

	.live-fill-view {
		padding: 10rpx 0;
		width: 100%;
	}

	.live-input {
		min-height: 35px;
		height: 35px;
		border-bottom: 1px solid #cccccc;
		font-size: 14px;
		padding-top: 10rpx;
	}

	.uni-list-cell {
		position: relative;
		display: -webkit-box;
		display: -webkit-flex;
		display: flex;
		-webkit-box-orient: horizontal;
		-webkit-box-direction: normal;
		-webkit-flex-direction: row;
		flex-direction: row;
		-webkit-box-pack: justify;
		-webkit-justify-content: space-between;
		justify-content: space-between;
		-webkit-box-align: center;
		-webkit-align-items: center;
		align-items: center;
		padding: 20rpx 0;
	}

	.uni-list-cell::after {
		position: absolute;
		z-index: 3;
		right: 0;
		bottom: 0;
		left: 15px;
		height: 1px;
		content: "";
		-webkit-transform: scaleY(.5);
		transform: scaleY(.5);
		background-color: #c8c7cc;
	}
</style>