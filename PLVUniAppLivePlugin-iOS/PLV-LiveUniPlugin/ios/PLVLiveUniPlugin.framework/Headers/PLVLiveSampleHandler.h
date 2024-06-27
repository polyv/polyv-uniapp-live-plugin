//
//  PLVLiveSampleHandler.h
//  PLVScreenShareExtension
//
//  Created by Sakya on 2022/2/9.
//  Copyright © 2022 PLV. All rights reserved.
//

#import <ReplayKit/ReplayKit.h>

@interface PLVLiveSampleHandler : RPBroadcastSampleHandler

- (void)setupSampleHandlerWithAppGroup:(NSString *)appGroup;

@end
