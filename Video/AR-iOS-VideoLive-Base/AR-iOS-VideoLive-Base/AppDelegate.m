//
//  AppDelegate.m
//  AR-iOS-VideoLive-Base
//
//  Created by anyRTC on 2021/1/26.
//

#import "AppDelegate.h"
#import "ARHomeViewController.h"
@interface AppDelegate ()

@end

@implementation AppDelegate


- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    // Override point for customization after application launch.
    [[UINavigationBar appearance] setBarTintColor:kUIColorFromRGB(0x121F2B)];
    [[UINavigationBar appearance] setTintColor:[UIColor whiteColor]];
    [[UINavigationBar appearance] setTitleTextAttributes:@{NSForegroundColorAttributeName:[UIColor whiteColor],NSFontAttributeName:[UIFont systemFontOfSize:18]}];
    UIStoryboard *sb = [UIStoryboard storyboardWithName:@"Main" bundle:[NSBundle mainBundle]];
    ARHomeViewController *vc = [sb instantiateViewControllerWithIdentifier:@"ARHomeViewController"];
    
    self.window.rootViewController = [[UINavigationController alloc] initWithRootViewController:vc];
    
    NSError *error;
    AVAudioSession *session = [AVAudioSession sharedInstance];
    [session setCategory:AVAudioSessionCategoryPlayback error:&error];
    [session setActive:YES error:&error];
    return YES;
}

- (void)applicationDidEnterBackground:(UIApplication *)application {
    //开启一个后台标示任务
    UIApplication *app = [UIApplication sharedApplication];
    __block UIBackgroundTaskIdentifier taskIdentifiery;
    taskIdentifiery = [app beginBackgroundTaskWithExpirationHandler:^{
        [app endBackgroundTask:taskIdentifiery];
        //标示一个后台任务请求
         taskIdentifiery = 0;//UIBackgroundTaskInvalid;
    }];
    
    //开启一个线程队列
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
        [app endBackgroundTask:taskIdentifiery];
        taskIdentifiery = UIBackgroundTaskInvalid;
    });
}

@end
