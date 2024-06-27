//
//  SampleHandler.m
//  PLVUniScreenShareExtension
//
//  Created by Sakya on 2024/6/19.
//  Copyright © 2024 Polyv. All rights reserved.
//


#import "SampleHandler.h"

@implementation SampleHandler

- (void)broadcastStartedWithSetupInfo:(NSDictionary<NSString *,NSObject *> *)setupInfo {
    [self setupSampleHandlerWithAppGroup:@"group.cn.plv.PolyvLiveScenesDemo.ScreenShare"];
    [super broadcastStartedWithSetupInfo:setupInfo];
}

- (void)broadcastPaused {
    [super broadcastPaused];
}

- (void)broadcastResumed {
    [super broadcastResumed];
}

- (void)broadcastFinished {
    [super broadcastFinished];
}

- (void)processSampleBuffer:(CMSampleBufferRef)sampleBuffer withType:(RPSampleBufferType)sampleBufferType {
    [super processSampleBuffer:sampleBuffer withType:sampleBufferType];
}

@end
