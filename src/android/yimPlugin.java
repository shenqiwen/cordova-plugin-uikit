package com.rsc.yim.plugin;

import android.util.Log;

import com.netease.nim.uikit.NimUIKit;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * This class echoes a string called from JavaScript.
 */
public class yimPlugin extends CordovaPlugin {
    CallbackContext callback;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        callback = callbackContext;
        if (action.equals("login")) {  // 登录
            PluginResult result = null;

            String userName = args.getString(0);
            String passWord = args.getString(1);

            if (userName == null || userName.equals("") || userName.equals("null") ||
                    passWord == null || passWord.equals("") || passWord.equals("null")) {
                result = new PluginResult(PluginResult.Status.ERROR, "请输入用户名或密码");
                callback.sendPluginResult(result);
                return false;
            }

//            // 调用云信sdk登陆方法登陆聊天服务器
//            LoginInfo info = new LoginInfo(userName, passWord); //账号 密码
//            RequestCallback<LoginInfo> callback_netease = new RequestCallback<LoginInfo>() {
//                @Override
//                public void onSuccess(LoginInfo param) {
//                    Log.i("SQW", "登陆聊天服务器成功！");
//                    //   启动单聊界面
//                    //    NimUIKit.startP2PSession( yimPlugin.this.cordova.getActivity().getApplication() , "admin");
//                    //        NimUIKit.startTeamSession(MainActivity.this, "13434751");
//                }
//
//                @Override
//                public void onFailed(int code) {
//                    Log.i("SQW", "登陆聊天服务器失败,code=" + code);
//                }
//
//                @Override
//                public void onException(Throwable exception) {
//                    Log.i("SQW", "登陆聊天服务器异常");
//                }
//            };
//
//            NIMClient.getService(AuthService.class).login(info).setCallback(callback_netease);//进行登录
            LoginInfo info = new LoginInfo(userName, passWord); //账号 密码
            NimUIKit.doLogin(info, new RequestCallback<LoginInfo>() {
                @Override
                public void onSuccess(LoginInfo loginInfo) {
                    Log.i("SQW","登陆成功");

                }

                @Override
                public void onFailed(int i) {
                    Log.i("SQW","登陆失败: "+i);
                }

                @Override
                public void onException(Throwable throwable) {
                    Log.i("SQW","登陆异常");
                }
            });

            result = new PluginResult(PluginResult.Status.OK, "userName: " + userName + "/" + "passWord: " + passWord);
            callback.sendPluginResult(result);

            return true;
        } else if (action.equals("chat")) {  // 聊天
            PluginResult result = null;

            String touserName = args.getString(0);
            int chatType = args.getInt(1);

            if (touserName == null || touserName.equals("") || touserName.equals("null")) {
                result = new PluginResult(PluginResult.Status.ERROR, "请输入用户名");
                callback.sendPluginResult(result);
                return false;
            }

            if (chatType == 0){ // 单聊
                NimUIKit.startP2PSession(yimPlugin.this.cordova.getActivity().getApplication(), touserName);
            }else if (chatType == 1){ // 群聊
                NimUIKit.startTeamSession(yimPlugin.this.cordova.getActivity().getApplication(), touserName);
            }

            result = new PluginResult(PluginResult.Status.OK, "touserName: " + touserName);
            callback.sendPluginResult(result);

            return true;
        }
        return false;
    }


}
