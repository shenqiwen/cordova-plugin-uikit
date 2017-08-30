var exec = require('cordova/exec');

exports.chat = function(touserName,chatType, left_role, right_role, success, error) {
        exec(success, error, "yimPlugin", "chat", [touserName,chatType, left_role, right_role]);

};

exports.login = function(userName, passWord, success, error) {
        exec(success, error, "yimPlugin", "login", [userName, passWord]);

};