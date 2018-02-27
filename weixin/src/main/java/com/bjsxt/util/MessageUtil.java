package com.bjsxt.util;

import com.thoughtworks.xstream.XStream;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.ServletInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * created by chenyichang on 2018/2/24
 */
public class MessageUtil {


    /**
     * 使用dom4j 将请求流中的xml转换成map 方便后期分析处理数据
     * @param inputStream
     * @return
     */
    public static Map toMap(ServletInputStream inputStream) throws DocumentException, IOException {
        //获取SaxReader
        SAXReader reader = new SAXReader();
        //获取文档
        Document document = reader.read(inputStream);
        //获取根元素
        Element rootElement = document.getRootElement();
        //获取其他元素
        List<Element> elements = rootElement.elements();
        //遍历封装到Map中
        Map<String, String> map = new HashMap<>();
        for (Element e : elements) {
            map.put(e.getName(), e.getText());
        }
        inputStream.close();
        return map;
    }

    /**
     * 将map中的数据转换成对象
     * @param xmlMap
     * @return
     */
    public static Message toMessage(Map<String, String> xmlMap) {
        Message message = new Message();
        message.setToUserName(xmlMap.get("ToUserName"));
        message.setFromUserNam(xmlMap.get("FromUserNam"));
        message.setCreateTime(Long.valueOf(Integer.valueOf(xmlMap.get("CreateTime"))));
        message.setContent(xmlMap.get("Content"));
        message.setMsgId(xmlMap.get("MsgId"));
        message.setMsgType(xmlMap.get("MsgType"));
        return message;
    }

    /**
     * 使用xStream将对象转换成xml格式
     * @param message
     * @return
     */
    public static String toXml(Message message) {
        //实例化xSteam
        XStream xStream = new XStream();
        //使用别名作为根元素，否则根元素为包名.类名，即替换根元素名称
        xStream.alias("xml", message.getClass());
        //转成xml
        return xStream.toXML(message);
    }
}
