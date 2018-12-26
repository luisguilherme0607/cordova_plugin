// Empty constructor
function ToastyPlugin() {}

// The function that passes work along to native shells
// Message is a string, duration may be 'long' or 'short'
ToastyPlugin.prototype.show = function(message, duration, successCallback, errorCallback) {
  var options = {};
  options.message = message;
  options.duration = duration;
  cordova.exec(successCallback, errorCallback, 'ToastyPlugin', 'show', [options]);
}

ToastyPlugin.prototype.scan = function(light_option, successCallback, errorCallback){
  var options = {};
  options.light = light_option;
  cordova.exec(successCallback, errorCallback,'ToastyPlugin', 'scan', [options]);
}

ToastyPlugin.prototype.stopScan = function(result, successCallback, errorCallback){
  var options = {result};
  cordova.exec(successCallback, errorCallback,'ToastyPlugin', 'stopScan', [options]);
}

// Installation constructor that binds ToastyPlugin to window
ToastyPlugin.install = function() {
  if (!window.plugins) {
    window.plugins = {};
  }
  window.plugins.toastyPlugin = new ToastyPlugin();
  return window.plugins.toastyPlugin;
};
cordova.addConstructor(ToastyPlugin.install);

module.exports = new ToastyPlugin();
