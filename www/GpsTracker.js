var exec = require('cordova/exec');
var emptyFn = function(){};

module.exports = {
  config: {},
  configure: function (success, failure, config) {
    this.config = config;
    var interval     = (config.interval       >= 0) ? config.interval : 5000, // milliseconds
      distanceFilter = (config.distanceFilter >= 0) ? config.distanceFilter : 5, // meters
      debug          = config.debug || false;

    exec(success, failure,
      'GpsTracker',
      'configure',
      [interval, distanceFilter, debug]
    );
  },
  echo: function (arg0, success, failure) {
    exec(success || emptyFn, failure || emptyFn, 'GpsTracker', 'echo', [arg0]);
  },
  start: function (success, failure) {
    exec(success || emptyFn, failure || emptyFn, 'GpsTracker', 'start', []);
  },
  stop: function (success, failure) {
    exec(success || emptyFn, failure || emptyFn, 'GpsTracker', 'stop', []);
  },
}
