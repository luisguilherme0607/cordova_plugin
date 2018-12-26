// Empty constructor
function Honeywell() {}

Honeywell.prototype.scan = function(light_option, successCallback, errorCallback){
  var options = {};
  options.light = light_option;
  cordova.exec(successCallback, errorCallback,'Honeywell', 'scan', [options]);
}

Honeywell.prototype.stopScan = function(result, successCallback, errorCallback){
  var options = {result};
  cordova.exec(successCallback, errorCallback,'Honeywell', 'stopScan', [options]);
}

// Installation constructor that binds ToastyPlugin to window
Honeywell.install = function() {
  if (!window.plugins) {
    window.plugins = {};
  }
  window.plugins.honeywell = new Honeywell();
  return window.plugins.honeywell;
};
cordova.addConstructor(Honeywell.install);

module.exports = new Honeywell();
