//
//  UserModel.h
//  AR-iOS-VoiceLive-Base
//
//  Created by anyRTC on 2021/1/26.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface ARUserModel : NSObject

/// 下行
@property (nonatomic ,assign) NSInteger rxAudioKBitrate;

/// 上行
@property (nonatomic ,assign) NSInteger txAudioKBitrate;

@property (nonatomic ,copy) NSString *uid;

@property (nonatomic ,assign) BOOL isSelf;

@end

NS_ASSUME_NONNULL_END
