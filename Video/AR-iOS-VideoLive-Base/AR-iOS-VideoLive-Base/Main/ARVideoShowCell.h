//
//  VideoShowCell.h
//  AR-iOS-Video-Base
//
//  Created by anyRTC on 2021/1/26.
//

#import <UIKit/UIKit.h>
#import "ARItemModel.h"
NS_ASSUME_NONNULL_BEGIN

@interface ARVideoShowCell : UICollectionViewCell

+ (instancetype)createVideoShowCellWithCollectionView:(UICollectionView *)collection IndexPath:(NSIndexPath *)indexPath;

@property (nonatomic ,strong) ARItemModel *model;

@end

NS_ASSUME_NONNULL_END
