var exec = require('cordova/exec');

module.exports = {
  config: {},
  configure: function (config) {
    this.config = config;
    var interval = (config.interval >= 0) ? config.interval : 1000, // milliseconds
      debug = config.debug || false;

    exec(function () {}, function () {},
      'GpsTracker',
      'configure',
      [interval, debug]
    );
  },
  start: function (success, failure) {
    exec(success || function () {}, failure || function () {},
      'GpsTracker',
      'start',
      []);
  },
  stop: function (success, failure) {
    exec(success || function() {}, failure || function() {},
      'GpsTracker',
      'stop',
      []);
  },
}
