#import <Cordova/CDV.h>
#import <CoreLocation/CoreLocation.h>

@interface GpsTracker : CDVPlugin

@property (nonatomic, strong) CLLocationManager *locationManager;
@property (nonatomic, strong) NSString* callbackId;

- (GpsTracker*)init;

- (void)configure:(CDVInvokedUrlCommand*)command;
- (void)echo:(CDVInvokedUrlCommand*)command;
- (void)start:(CDVInvokedUrlCommand*)command;
- (void)stop:(CDVInvokedUrlCommand*)command;

@end

