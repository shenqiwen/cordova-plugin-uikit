package com.rsc.yim.plugin;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;

import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.session.SessionEventListener;
import com.netease.nim.uikit.session.activity.TeamMessageActivity;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;

public class NimApplication extends Application {

    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    public void onCreate() {
        super.onCreate();

        /**
         * 云信 初始化
         */
        NIMClient.init(this ,null,options());
        if (inMainProcess()) {
            // 在主进程中初始化UI组件，判断所属进程方法请参见demo源码。
            initUiKit();
            // 初始化消息提醒
//            NIMClient.toggleNotification(UserPreferences.getNotificationToggle());
        }

    }

    private SDKOptions options() {
        SDKOptions options = new SDKOptions();

        // 如果将新消息通知提醒托管给 SDK 完成，需要添加以下配置。否则无需设置。
        StatusBarNotificationConfig config = new StatusBarNotificationConfig();

        config.notificationEntrance = TeamMessageActivity.class; // 点击通知栏跳转到该Activity
//	        config.notificationSmallIconId = R.drawable.ic_stat_notify_msg;
        // 呼吸灯配置
        config.ledARGB = Color.GREEN;
        config.ledOnMs = 1000;
        config.ledOffMs = 1500;
        // 通知铃声的uri字符串
        config.notificationSound = "android.resource://com.netease.nim.demo/raw/msg";
        options.statusBarNotificationConfig = config;

        // 配置保存图片，文件，log 等数据的目录
        // 如果 options 中没有设置这个值，SDK 会使用下面代码示例中的位置作为 SDK 的数据目录。
        // 该目录目前包含 log, file, image, audio, video, thumb 这6个目录。
        // 如果第三方 APP 需要缓存清理功能， 清理这个目录下面个子目录的内容即可。
        String sdkPath = Environment.getExternalStorageDirectory() + "/" + this.getPackageName() + "/nim";
        options.sdkStorageRootPath = sdkPath;

        // 配置是否需要预下载附件缩略图，默认为 true
        options.preloadAttach = true;

        // 配置附件缩略图的尺寸大小。表示向服务器请求缩略图文件的大小
        // 该值一般应根据屏幕尺寸来确定， 默认值为 Screen.width / 2
        options.thumbnailSize = 480 / 2;

        return options;
    }


    private boolean inMainProcess() {
        String packageName = this.getPackageName();
        String processName = SystemUtil.getProcessName(this);
        return packageName.equals(processName);
    }

    private void initUiKit() {
        // 初始化
        NimUIKit.init(this);
        // 设置地理位置提供者。如果需要发送地理位置消息，该参数必须提供。如果不需要，可以忽略。
        NimUIKit.setLocationProvider(new NimDemoLocationProvider());
        // 会话窗口的定制初始化。
        SessionHelper.init();
        // 通讯录列表定制初始化
//        ContactHelper.init();
        // 聊天界面 用户头像 点击事件 监听
        NimUIKit.setSessionListener(listener);
    }

    SessionEventListener listener = new SessionEventListener() {
        @Override
        public void onAvatarClicked(Context context, IMMessage message) {
              //   Log.i("SQW", "头像被点击");
        }

        @Override
        public void onAvatarLongClicked(Context context, IMMessage message) {
              //    Log.i("SQW", "头像被长按");
        }
    };


    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

}
