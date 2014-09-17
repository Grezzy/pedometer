var Pedometer = {

    init: function (str, callback) {
        cordova.exec(
            function (data) {
                callback(data);
            },
            function (err) {
                callback(err);
            },
            "Pedometer",
            "init",
            [] // option : "debug", 0.5
        );
    },

    start: function (str, callback) {
        cordova.exec(
            function (data) {
                callback(data);
            },
            function (err) {
                callback(err);
            },
            "Pedometer",
            "start",
            []
        );
    },

    stop: function (str, callback) {
        cordova.exec(
            function (data) {
                callback(data);
            },
            function (err) {
                callback(err);
            },
            "Pedometer",
            "stop",
            []
        );
    }
};

module.exports = Pedometer;