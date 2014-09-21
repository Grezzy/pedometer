

var Pedometer = {
    initialize: function (success) {
        cordova.exec(
            function (data) {
                if (success) success(data);
            },
            function (err) {
                if (error) error(err);
            },
            "Pedometer",
            "initialize",
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

Pedometer.initialize();
module.exports = Pedometer;

