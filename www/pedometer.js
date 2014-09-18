

var Pedometer = {
    init: function (success) {
        cordova.exec(
            function (data) {
                if (success) success(data);
            },
            function (err) {
                if (error) error(err);
            },
            "Pedometer",
            "init",
            [] // option : "debug", 0.5
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

Pedometer.init();
module.exports = Pedometer;

