

var Pedometer = {
    initialize: function (success, error) {
        cordova.exec(
            function (data) {
                if (success) success(data);
            },
            function (err) {
                if (error) error(err);
            },
            "Pedometer",
            "initialize",
            [] 
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

Pedometer.initialize();
module.exports = Pedometer;

