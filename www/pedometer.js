cordova.define("com.firerunner.cordova.pedometer.pedometer", function(require, exports, module) { 

var Pedometer = {
    initialize: function (bioprofile, success, error) {
        cordova.exec(
            function (data) {
                if (success) success(data);
            },
            function (err) {
                if (error) error(err);
            },
            "Pedometer",
            "initialize",
            [bioprofile]
        );
    },

    isSupported: function (success, error) {
        cordova.exec(
            function (data) {
                if (success) success(data);
            },
            function (err) {
                if (error) error(err);
            },
            "Pedometer",
            "isSupported",
            [] 
        );
    },

    start: function (success, error) {
        cordova.exec(
            function (data) {
                if (success) success(data);
            },
            function (err) {
                if (error) error(err);
            },
            "Pedometer",
            "start",
            []
        );
    },

    stop: function (success, error) {
        cordova.exec(
            function (data) {
                if (success) success(data);
            },
            function (err) {
                if (error) error(err);
            },
            "Pedometer",
            "stop",
            []
        );
    },

    getCurrentReading: function (success, error) {
        cordova.exec(
            function (data) {
                if (success) success(data);
            },
            function (err) {
                if (error) error(err);
            },
            "Pedometer",
            "getCurrentReading",
            []
        );
    }
};

module.exports = Pedometer;


});
