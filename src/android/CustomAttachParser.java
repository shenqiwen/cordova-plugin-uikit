package com.rsc.yim.plugin;

import com.alibaba.fastjson.JSONObject;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachmentParser;

/**
 * Created by DN on 2017/7/28.
 */

public class CustomAttachParser implements MsgAttachmentParser {
    private static final String KEY_TYPE = "type";
    private static final String KEY_DATA = "data";
    @Override
    public MsgAttachment parse(String s) {
        return null;
    }
    public static String packData(int type, JSONObject data) {
        JSONObject object = new JSONObject();
        object.put(KEY_TYPE, type);
        if (data != null) {
            object.put(KEY_DATA, data);
        }

        return object.toJSONString();
    }
}
