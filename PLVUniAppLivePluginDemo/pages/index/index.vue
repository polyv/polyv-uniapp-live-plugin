<template>
	<view class="uni-container">
		<view class="uni-panel" v-for="(item, index) in list" :key="item.id">
			<view class="uni-panel-h uni-panel-h-text-center" :class="item.open ? 'uni-panel-h-on' : ''" @click="triggerCollapse(index)">
				<text class="uni-panel-text">{{item.name}}</text>
			</view>
		</view>
	</view>
</template>
<script>
	
	export default {
		
		data() {
			return {
				list: [{
					id: 'live-watch',
					name: '手机开播',
					open: false,
					url: '/pages/index/live-streamer'					
				},
				{
					id: 'live-streamer',
					name: '云直播观看',
					open: false,
					url: '/pages/index/live-watch'
				}],
				navigateFlag: false,
				title: 'Demo',
			}
		},
		onLoad() {

		},
		methods: {
			triggerCollapse(e) {
				if (!this.list[e].pages) {
					this.goDetailPage(this.list[e].url);
					return;
				}
				for (var i = 0; i < this.list.length; ++i) {
					if (e === i) {
						this.list[i].open = !this.list[e].open;
					} else {
						this.list[i].open = false;
					}
				}
			},
			goDetailPage(e) {
				if (this.navigateFlag) {
					return;
				}
				this.navigateFlag = true;
				uni.navigateTo({
					url: e
				});
				setTimeout(() => {
					this.navigateFlag = false;
				}, 200)
				return false;
			}
		}
	}
</script>

<style>
	.uni-panel-h-text-center {
		text-align: center;
		background-color: #2B98F0;
	}
	.uni-panel-text {
		color: white;
		font-size: 18px;
	}
</style>
