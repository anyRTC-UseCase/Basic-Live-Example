//
//  HomeViewController.m
//  AR-iOS-VoiceLive-Base
//
//  Created by anyRTC on 2021/1/26.
//

#import "ARHomeViewController.h"
#import "ARRoomViewController.h"
@interface ARHomeViewController ()
{
    NSString            *_uid;
}

@property (weak, nonatomic) IBOutlet UITextField *channelTF;
@property (weak, nonatomic) IBOutlet UIButton *anchorBtn;
@property (weak, nonatomic) IBOutlet UIButton *audienceBtn;

@end

@implementation ARHomeViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    _uid = [self getRandomUid];
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

- (IBAction)AnchorAction:(id)sender {
    [self pushRoomControllerWithAnchor:YES];
}

- (IBAction)AudienceAction:(id)sender {
    [self pushRoomControllerWithAnchor:NO];
}


/// 跳转到房间控制器
/// @param isAnchor 是否为主播
- (void)pushRoomControllerWithAnchor:(BOOL)isAnchor {
    [self.view endEditing:YES];
    UIStoryboard *sb = [UIStoryboard storyboardWithName:@"Main" bundle:[NSBundle mainBundle]];
    ARRoomViewController *vc = [sb instantiateViewControllerWithIdentifier:@"ARRoomViewController"];
    vc.userId = _uid;
    vc.channelId = self.channelTF.text;
    vc.isAnchor = isAnchor;
    [self.navigationController pushViewController:vc animated:YES];
}


/// 随机一个6位数用户id
- (NSString *)getRandomUid {
    NSArray *randoms = [[NSArray alloc] initWithObjects:@"1",@"2",@"3",@"4",@"5",@"6",@"7",@"8",@"9", nil];
    NSString *resultStr = @"";
    for (int i = 0; i < 6; i ++) {
        NSInteger index = arc4random()%(randoms.count - 1);
        NSString *randomStr = randoms[index];
        resultStr = [resultStr stringByAppendingString:randomStr];
    }
    return resultStr;
}


- (void)touchesBegan:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event {
    [self.view endEditing:YES];
}

@end
