function MaterialDatePicker() {

}

//Todo -> add options to configure the plugin. Currently just send default along with cordova.exec function call
MaterialDatePicker.prototype.show = function(callback, errorCallback, options) {

    var today = new Date();

    var datePickerOptions = {
        'selectedDay': today.getDate(),
        'selectedMonth': today.getMonth(),
        'selectedYear': today.getFullYear()
    }

    Object.assign(datePickerOptions, options);

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
		[JSON.stringify(datePickerOptions)]
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