package com.business.start.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsoupUtils {

    public static Document getDoc(String strUrl){
        Document doc = null;
        InputStream in = null;
        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            //使用代理且需要登录，添加这段代码
            /*conn.setRequestProperty("Proxy-Authorization", " Basic " +
            new BASE64Encoder().encode("用户名:密码".getBytes()));*/
            //该项必须配置，很多网站会拒绝非浏览器的访问，不设置会返回403，访问被服务器拒绝
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)");
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "text/html");
            conn.setRequestProperty("Connection", "close");
            conn.setUseCaches(false);
            conn.setConnectTimeout(5 * 1000);
            String encode = getEncode(conn.getHeaderField("Content-Type"));
            in = conn.getInputStream();
            doc = Jsoup.parse(in,encode,strUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(null != in){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return doc;
    }

    public static String getEncode(String headerField) {
        String encode = "utf-8";
        if(null == headerField || "".equals(headerField)){
            return encode;
        }
        headerField = headerField.toLowerCase();
        if(headerField.contains("charset=") && !headerField.contains("charset=utf-8")){
            if(headerField.contains("charset=gbk")){
                encode = "gbk";
            }else if(headerField.contains("charset=gb2312")){
                encode = "gb2312";
            }else if(headerField.contains("charset=iso-8859-1")){
                encode = "iso-8859-1";
            }
        }
        return encode;
    }

}
