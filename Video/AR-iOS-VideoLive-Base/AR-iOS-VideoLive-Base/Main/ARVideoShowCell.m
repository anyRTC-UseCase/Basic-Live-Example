//
//  VideoShowCell.m
//  AR-iOS-Video-Base
//
//  Created by anyRTC on 2021/1/26.
//

#import "ARVideoShowCell.h"

@interface ARVideoShowCell ()

@property (weak, nonatomic) IBOutlet UIView *videoView;

@end

@implementation ARVideoShowCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

+ (instancetype)createVideoShowCellWithCollectionView:(UICollectionView *)collection IndexPath:(NSIndexPath *)indexPath {
    ARVideoShowCell *cell = [collection dequeueReusableCellWithReuseIdentifier:NSStringFromClass([self class]) forIndexPath:indexPath];
    return cell;
}


- (void)setModel:(ARItemModel *)model {
    ARtcVideoCanvas *canvas = [[ARtcVideoCanvas alloc] init];
    canvas.view = self.videoView;
    canvas.renderMode = ARVideoRenderModeFit;
    canvas.uid = model.uid;
    model.isSelf ? [model.engineKit setupLocalVideo:canvas] : [model.engineKit setupRemoteVideo:canvas];
}


@end
