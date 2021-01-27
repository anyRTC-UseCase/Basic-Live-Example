//
//  PeopleCellCell.h
//  AR-iOS-VoiceLive-Base
//
//  Created by anyRTC on 2021/1/26.
//

#import <UIKit/UIKit.h>
#import "ARUserModel.h"

NS_ASSUME_NONNULL_BEGIN

@interface ARPeopleCell : UITableViewCell

+ (instancetype)createPeopleCellWithTableView:(UITableView *)tableView IndexPath:(NSIndexPath *)indexPath;

@property (nonatomic ,strong) ARUserModel *model;

@end

NS_ASSUME_NONNULL_END
