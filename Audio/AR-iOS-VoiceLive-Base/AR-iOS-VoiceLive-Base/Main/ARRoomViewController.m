//
//  RoomViewController.m
//  AR-iOS-VoiceLive-Base
//
//  Created by anyRTC on 2021/1/26.
//

#define APPID               @"177e21c0d1641291c34e46e1198bd49a"

#import "ARRoomViewController.h"
#import "ARUserModel.h"
#import "ARPeopleCell.h"
@interface ARRoomViewController ()<UITableViewDelegate,UITableViewDataSource,ARtcEngineDelegate>

@property (weak, nonatomic) IBOutlet UIButton *micBtn;
@property (weak, nonatomic) IBOutlet UIButton *closeBtn;
@property (weak, nonatomic) IBOutlet UIButton *volumeBtn;
@property (weak, nonatomic) IBOutlet UITableView *table;

@property (nonatomic ,strong) ARtcEngineKit *engineKit;

@property (nonatomic ,strong) NSMutableArray *dataArray;

@end

@implementation ARRoomViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.title = self.channelId;
    [self initUI];
    [self initARtcKit];
    [self.engineKit joinChannelByToken:@"" channelId:self.channelId uid:self.userId joinSuccess:nil];
}

- (void)viewDidDisappear:(BOOL)animated {
    [super viewDidDisappear:animated];
    [self.engineKit leaveChannel:nil];
    self.engineKit.delegate = nil;
    self.engineKit = nil;
}


- (NSMutableArray *)dataArray {
    if (!_dataArray) {
        _dataArray = [[NSMutableArray alloc] init];
    }
    return _dataArray;
}

- (void)initUI {
    self.table.separatorStyle = UITableViewCellSeparatorStyleNone;
    [self.table setTableFooterView:[[UIView alloc] init]];
    
    
    [self.micBtn setImage:IMG(@"img_audio_open") forState:UIControlStateNormal];
    [self.micBtn setImage:IMG(@"img_audio_close") forState:UIControlStateSelected];
    
    [self.volumeBtn setImage:IMG(@"img_voice_open") forState:UIControlStateNormal];
    [self.volumeBtn setImage:IMG(@"img_voice_close") forState:UIControlStateSelected];
}

- (void)initARtcKit {
    self.engineKit = [ARtcEngineKit sharedEngineWithAppId:APPID delegate:self];
    [self.engineKit setEnableSpeakerphone:YES];
    
    if (self.isAnchor) {
        [self.engineKit setClientRole:ARClientRoleBroadcaster];
    } else {
        [self.engineKit setClientRole:ARClientRoleAudience];
        self.micBtn.hidden = YES;
        self.volumeBtn.hidden = YES;
    }
    
    [self.engineKit setChannelProfile:ARChannelProfileiveBroadcasting];
}

//MARK: - UITableViewDelegate/UITableViewDataSource

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.dataArray.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    ARPeopleCell *cell = [ARPeopleCell createPeopleCellWithTableView:tableView IndexPath:indexPath];
    cell.model = self.dataArray[indexPath.row];
    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 50;
}

//MARK: - ARtcEngineDelegate

///用户加入RTC频道回调
- (void)rtcEngine:(ARtcEngineKit *)engine didJoinChannel:(NSString *)channel withUid:(NSString *)uid elapsed:(NSInteger)elapsed {
    
    if ([uid isEqualToString:self.userId] && self.isAnchor) {
        ARUserModel *model = [[ARUserModel alloc] init];
        model.uid = uid;
        model.isSelf = YES;
        [self.dataArray addObject:model];
    } else {
        if (![uid isEqualToString:self.userId]) {
            ARUserModel *model = [[ARUserModel alloc] init];
            model.uid = uid;
            model.isSelf = NO;
            [self.dataArray addObject:model];
        }
    }
    
    [self.table reloadData];
}

///用户离开RTC频道回调
- (void)rtcEngine:(ARtcEngineKit * _Nonnull)engine didLeaveChannelWithStats:(ARChannelStats * _Nonnull)stats{
    
}

///远端用户加入RTC频道回调
- (void)rtcEngine:(ARtcEngineKit *)engine didJoinedOfUid:(NSString *)uid elapsed:(NSInteger)elapsed {
    ARUserModel *model = [[ARUserModel alloc] init];
    model.uid = uid;
    [self.dataArray addObject:model];
    [self.table reloadData];
}

///远端用户离开RTC频道回调
- (void)rtcEngine:(ARtcEngineKit *_Nonnull)engine didOfflineOfUid:(NSString *_Nonnull)uid reason:(ARUserOfflineReason)reason {
    for (ARUserModel *model in self.dataArray) {
        if ([model.uid isEqualToString:uid]) {
            [self.dataArray removeObject:model];
            break;
        }
    }
    [self.table reloadData];
}

///本地用户通话信息流的统计
- (void)rtcEngine:(ARtcEngineKit * _Nonnull)engine reportRtcStats:(ARChannelStats * _Nonnull)stats {
    for (ARUserModel *model in self.dataArray) {
        if ([model.uid isEqualToString:self.userId]) {
            model.rxAudioKBitrate = stats.rxAudioKBitrate;
            model.txAudioKBitrate = stats.txAudioKBitrate;
        }
    }
    [self.table reloadData];
}

///远端用户通话信息流的统计
- (void)rtcEngine:(ARtcEngineKit * _Nonnull)engine remoteAudioStats:(ARtcRemoteAudioStats * _Nonnull)stats {
    for (ARUserModel *model in self.dataArray) {
        if ([model.uid isEqualToString:stats.uid]) {
            model.rxAudioKBitrate = stats.receivedBitrate;
        }
    }
    [self.table reloadData];
}



- (IBAction)MicAction:(id)sender {
    self.micBtn.selected = !self.micBtn.selected;
    [self.engineKit enableLocalAudio:!self.micBtn.selected];
}

- (IBAction)CloseAction:(id)sender {
    [self.engineKit leaveChannel:nil];
    self.engineKit.delegate = nil;
    self.engineKit = nil;
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)VolumeAction:(id)sender {
    self.volumeBtn.selected = !self.volumeBtn.selected;
    [self.engineKit setEnableSpeakerphone:!self.volumeBtn.selected];
}


@end
