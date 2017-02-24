#import "GpsTracker.h"

@implementation GpsTracker {
  bool isDebugging;
  double distanceFilter;
  double allowedAccuracy;
  long interval;

  double latitude;
  double longitude;
  double accuracy;
  double altitude;
  double bearing;
  double speed;
  double timestamp;
}

- (GpsTracker*)init
{
    self = [super init];
    if (self) {
        isDebugging = false;
        distanceFilter = 2.0;
        allowedAccuracy = 20;
        interval = 2000;
    }
    return self;
}

- (void)startLocationUpdates
{
    // Create the location manager if this object does not
    // already have one.
    if (self.locationManager == nil) {
        self.locationManager = [[CLLocationManager alloc] init];
    }

    self.locationManager.delegate = self;
    self.locationManager.desiredAccuracy = kCLLocationAccuracyBest;
    self.locationManager.activityType = CLActivityTypeFitness;

    // Movement threshold for new events.
    self.locationManager.distanceFilter = distanceFilter; // meters

    [self.locationManager startUpdatingLocation];
}

- (void)locationManager:(CLLocationManager *)manager
     didUpdateLocations:(NSArray *)locations
{
  for (CLLocation *newLocation in locations) {
    if (newLocation.horizontalAccuracy < allowedAccuracy) {
      latitude = newLocation.coordinate.latitude;
      longitude = newLocation.coordinate.longitude;
      accuracy = newLocation.horizontalAccuracy;
      altitude = newLocation.altitude;
      bearing = newLocation.course;
      speed = newLocation.speed;
      timestamp = [newLocation.timestamp timeIntervalSince1970] * 1000;

      [self returnGpsInfo];
    }
  }
}

- (void)returnGpsInfo
{
    // Create an orientation object
    NSMutableDictionary* orientationProps = [NSMutableDictionary dictionaryWithCapacity:4];

    [orientationProps setValue:[NSNumber numberWithDouble:latitude] forKey:@"latitude"];
    [orientationProps setValue:[NSNumber numberWithDouble:longitude] forKey:@"longitude"];
    [orientationProps setValue:[NSNumber numberWithDouble:accuracy] forKey:@"accuracy"];
    [orientationProps setValue:[NSNumber numberWithDouble:altitude] forKey:@"altitude"];
    [orientationProps setValue:[NSNumber numberWithDouble:bearing] forKey:@"bearing"];
    [orientationProps setValue:[NSNumber numberWithDouble:speed] forKey:@"speed"];
    [orientationProps setValue:[NSNumber numberWithDouble:timestamp] forKey:@"timestamp"];

    CDVPluginResult* result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:orientationProps];
    [result setKeepCallback:[NSNumber numberWithBool:YES]];
    [self.commandDelegate sendPluginResult:result callbackId:self.callbackId];
}

- (void)configure:(CDVInvokedUrlCommand*)command
{
  self.callbackId = command.callbackId;

  interval = [[command.arguments objectAtIndex:0] longValue];
  distanceFilter = [[command.arguments objectAtIndex:1] doubleValue];
  allowedAccuracy = [[command.arguments objectAtIndex:2] boolValue];
  isDebugging = [[command.arguments objectAtIndex:3] boolValue];
}

- (void)echo:(CDVInvokedUrlCommand*)command
{
  CDVPluginResult* pluginResult = nil;
  self.callbackId = command.callbackId;
  NSString* echo = [command.arguments objectAtIndex:0];

  if (echo != nil && [echo length] > 0) {
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:echo];
  } else {
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
  }

  [self startLocationUpdates];

  [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)start:(CDVInvokedUrlCommand*)command
{
  [self startLocationUpdates];
}

- (void)stop:(CDVInvokedUrlCommand*)command
{
  [self.locationManager stopUpdatingLocation];
}

@end

