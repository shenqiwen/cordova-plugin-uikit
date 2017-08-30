# 基于网易云信 uikit库 和 官方IM Demo集成的 Cordova 聊天插件(云信IM SDK4.0.0)

# 即将更新
  从聊天界面返回时的回调
  
# 支持的平台
  仅Android

# 主要功能
 1. 开启聊天界面(单聊与群聊)
 
 2. 支持发送的消息类型: 文字 语音 表情 图片 小视频 位置 
 
# 使用方法
```

 //登录  参数：云信ID、云信密码
 cordova.plugins.yimPlugin.login('云信ID','云信密码',function(e){alert(e);},function(e){console.log(e);});
 
// 聊天 参数 : 对方云信ID、0为单聊、1为群聊 后两个参数暂时没有用上 
 cordova.plugins.yimPlugin.chat('对方云信ID',0,'leftRole','rightRole',function(e){alert(e);},function(e){console.log(e);});
 
```
# 注意事项(需要手动更改)
 1. 插件安装完毕后需要修改插件内里所有文件的.R引用改为自己项目的包名 本插件包名为: com.rsc.yim.plugin
 
 2. 插件安装完毕后在plugin.xml 配置文件内 添加 高德地图和云信IM的key
 
 3. 在将此插件添加至Android平台后需要在 platforms/android 路径下找到清单文件 AndroidManifest.xml 在此文件内将 application组件 添加名字如:             android:name="com.rsc.yim.plugin.NimApplication"
