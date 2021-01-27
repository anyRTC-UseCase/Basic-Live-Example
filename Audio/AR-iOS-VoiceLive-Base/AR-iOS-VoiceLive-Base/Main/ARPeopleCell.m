//
//  PeopleCellCell.m
//  AR-iOS-VoiceLive-Base
//
//  Created by anyRTC on 2021/1/26.
//

#import "ARPeopleCell.h"

@interface ARPeopleCell ()

@property (weak, nonatomic) IBOutlet UILabel *downLab;
@property (weak, nonatomic) IBOutlet UIImageView *downImg;
@property (weak, nonatomic) IBOutlet UILabel *upLab;
@property (weak, nonatomic) IBOutlet UIImageView *upImg;
@property (weak, nonatomic) IBOutlet UILabel *userName;

@property (nonatomic ,strong) NSIndexPath *currentIndex;
@end

@implementation ARPeopleCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

+ (instancetype)createPeopleCellWithTableView:(UITableView *)tableView IndexPath:(nonnull NSIndexPath *)indexPath {
    ARPeopleCell *cell = [tableView dequeueReusableCellWithIdentifier:NSStringFromClass([self class])];
    if (!cell) {
        cell = [[ARPeopleCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:NSStringFromClass([self class])];
    }
    cell.currentIndex = indexPath;
    return cell;
}


- (void)setModel:(ARUserModel *)model {
    if (model.isSelf) {
        self.upImg.hidden = NO;
        self.upLab.hidden = NO;
        self.upLab.text = [NSString stringWithFormat:@"%ldKb/s",model.txAudioKBitrate];
        self.userName.text = [model.uid stringByAppendingString:@"(自己)"];
    } else {
        self.upImg.hidden = YES;
        self.upLab.hidden = YES;
        self.userName.text = model.uid;
    }
    self.downLab.text = [NSString stringWithFormat:@"%ldKb/s",model.rxAudioKBitrate];
    
}


@end
