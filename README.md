# cordova-plugin-gps-tracking
Cordova plugin for enabling GPS tracking functionality on a phone.
Primary purpose was to use sports application to track people movement during
an athletic activity.

It works for iOS and Android. You can specify the frequency of the update
and minimal distance between two data points.

## Installation

```
cordova plugin add https://github.com/vohtaski/cordova-plugin-gps-tracker.git --save
```

## How to use

This plugin exports an object `window.plugins.GpsTracker`

```js
// Get plugin
var GpsTracker = window.plugins.GpsTracker;

// Congfigure Plugin
GpsTracker.configure({
  distanceFilter: 5, // (Meters) How far you must move from the last point to trigger a location update
  allowedAccuracy: 20, // (Meters) Data points with accuracy more than threshold will not be emitted
  interval: 9000, // (Milliseconds) Requested Interval in between location updates.
}, function (location) {
  location.latitude;
  location.longitude;
  location.accuracy;
  location.altitude;
  location.bearing;
  location.speed;
  location.timestamp;
});

GpsTracker.start();

GpsTracker.stop();
```
