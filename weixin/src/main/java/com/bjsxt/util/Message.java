package com.bjsxt.util;

/**
 * 文本的类型的javabean
 * created by chenyichang on 2018/2/24
 */
public class Message {
    //开发者微信号
    private String ToUserName;
    //关注者
    private String FromUserNam;
    //消息创建时间
    private Long CreateTime;
    //消息类型
    private String MsgType;
    //内容
    private String Content;
    //消息id
    private String MsgId;

    public Message() {

    }

    public String getToUserName() {
        return ToUserName;
    }

    public void setToUserName(String toUserName) {
        ToUserName = toUserName;
    }

    public String getFromUserNam() {
        return FromUserNam;
    }

    public void setFromUserNam(String fromUserNam) {
        FromUserNam = fromUserNam;
    }

    public Long getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(Long createTime) {
        CreateTime = createTime;
    }

    public String getMsgType() {
        return MsgType;
    }

    public void setMsgType(String msgType) {
        MsgType = msgType;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getMsgId() {
        return MsgId;
    }

    public void setMsgId(String msgId) {
        MsgId = msgId;
    }
}
