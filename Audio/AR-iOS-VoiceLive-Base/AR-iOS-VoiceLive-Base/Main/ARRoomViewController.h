//
//  RoomViewController.h
//  AR-iOS-VoiceLive-Base
//
//  Created by anyRTC on 2021/1/26.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface ARRoomViewController : UIViewController

@property (nonatomic ,copy) NSString *channelId;

@property (nonatomic ,copy) NSString *userId;

@property (nonatomic ,assign) BOOL isAnchor;

@end

NS_ASSUME_NONNULL_END
