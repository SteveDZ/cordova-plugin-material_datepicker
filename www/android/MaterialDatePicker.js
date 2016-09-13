function MaterialDatePicker() {

}

//Todo -> add options to configure the plugin. Currently just send default along with cordova.exec function call
MaterialDatePicker.prototype.show = function(callback, errorCallback) {

    var pluginCallback = function(selectedDate) {
        callback(selectedDate);
    }

    var pluginErrorCallback = function() {
        errorCallback();
    }

    cordova.exec(pluginCallback,
		pluginErrorCallback,
		"MaterialDatePickerPlugin",
		"displayDatePicker",
		[]
	);
}

var materialDatePicker = new MaterialDatePicker();
module.exports = materialDatePicker;

if(!window.plugins) {
    window.plugins = {};
}

if(!window.plugins.materialDatePicker) {
    window.plugins.materialDatePicker = materialDatePicker;
}