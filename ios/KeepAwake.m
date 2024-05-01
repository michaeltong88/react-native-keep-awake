#import "KeepAwake.h"

@implementation KeepAwake

RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(setKeepScreenOn:(BOOL)lock)
{
    dispatch_async(dispatch_get_main_queue(), ^{
        [[UIApplication sharedApplication] setIdleTimerDisabled:lock];
    });
}

RCT_EXPORT_METHOD(setShowWhenLocked:(BOOL)lock)
{
    return;
}

RCT_EXPORT_METHOD(setSystemUiVisibility:(BOOL)lock)
{
    return;
}

RCT_EXPORT_METHOD(wakeScreen:(BOOL)lock)
{
    return;
}

@end
