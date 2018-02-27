package com.bjsxt;

import com.bjsxt.util.Message;
import com.bjsxt.util.MessageType;
import com.bjsxt.util.MessageUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.dom4j.DocumentException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * created by chenyichang on 2018/2/11
 */
public class WxServlet extends HttpServlet {
    /**
     * 参考文档
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //设置字符集
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");
        //获取输出
        PrintWriter out = resp.getWriter();
        //连接  调用方法处理接入
        connection(req, out);

    }

    /**
     * 接受消息，post方式
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //设置字符集
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");


        //回送消息
        echoMessage(req, resp);
    }

    /**
     * 自动回复
     * 1.获取xml数据
     * 2.xml---解析---存放map中
     * 3.xml解析、转换成对象
     * 4.回复消息（调换from与to，时间，内容）
     * 5.将对象转为xml
     * 6.resp发送出去
     * @param req
     * @param resp
     */
    private void echoMessage(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter writer = resp.getWriter();
        Map<String, String> xmlMap = null;
        try {
            //解析xml，封装成map
            xmlMap = MessageUtil.toMap(req.getInputStream());
            //将map转换成对象
            Message message = MessageUtil.toMessage(xmlMap);
            //回复消息
            String from = message.getFromUserNam();
            String to = message.getToUserName();
            message.setFromUserNam(to);
            message.setToUserName(from);
            message.setCreateTime(System.currentTimeMillis());
            String content = message.getContent();
            content = null == content ? "" : content.trim();

            //返回数据
            String replay = "";

            //获取消息类型
            String msgType = xmlMap.get("MsgType");
            if (msgType.equals(MessageType.TEXT)) {
                if (content.equals("1") || content.equalsIgnoreCase("sxt")) {
                    replay = sxtText();
                } else if (content.equals("2") || content.equalsIgnoreCase("joke")) {
                    replay = jokeText();
                } else if (content.equals("?") || content.equals("？")) {
                    replay = mainMenu();
                } else {
                    replay = "其他";
                }
            } else if (msgType.equals(MessageType.EVENT)) {
                String eventType = xmlMap.get("Event").toLowerCase();

                if (eventType.equals(MessageType.SUBSCRIBE)) {
                    //关注
                    replay = mainMenu();
                    message.setMsgType(MessageType.TEXT);
                } else if (eventType.equals(MessageType.UNSUBSCRIBE)) {
                    //取消关注
                    System.out.println("有人取消了关注");
                }
            }
            //设置回送的信息
            message.setContent(replay);
            //将message对象转换为xml格式
            String xmlStr = MessageUtil.toXml(message);
            System.out.println(xmlStr);
            //使用write发送出去
            writer.print(xmlStr);
            writer.flush();

        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }

    private String mainMenu() {
        String sxtText = sxtText();
        return sxtText;
    }

    private String jokeText() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("xxxxxxxxxxxxxx");
        return buffer.toString();
    }

    //返回固定的文本内容
    private String sxtText() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("欢迎\n");
        buffer.append("1.xxx");
        buffer.append("2.xxx");
        buffer.append("3.xxx");
        buffer.append("4.xxx");
        return buffer.toString();
    }

    /**
     * 接入
     * @param req
     * @param out
     */
    private void connection(HttpServletRequest req, PrintWriter out) {

        //首先获取4个参数
        String signature = req.getParameter("signature");//加密签名
        String timestamp = req.getParameter("timestamp");//时间搓
        String nonce = req.getParameter("nonce");//随机数
        String echostr = req.getParameter("echostr");//随机字符串

        //校验逻辑
        List<String> list = new ArrayList<>();
        list.add("token");//token,需要再微信平台后台填写
        list.add(timestamp);
        list.add(nonce);
        //排序
        Collections.sort(list);
        //拼接字符串-----shal加密
        StringBuffer buffer = new StringBuffer();
        for (String str : list) {
            buffer.append(str);
        }
        //加密  使用Apache commons codec
        String shalStr = DigestUtils.sha1Hex(buffer.toString());
        //对比
        boolean flag = shalStr.equals(signature);
        if (flag) {
            System.out.println("接入成功");
            out.print(echostr);
            out.flush();
        }
    }
}
