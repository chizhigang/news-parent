package com.business.start.utils;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.client.RestTemplate;

/**
 * 解析今日头条工具类
 */
public class JinRiTouTiaoUtils {

    /**
     * 解析娱乐新闻
     *
     * @param yuleContent
     * @return
     */
    public static String getYuLeContent(String yuleContent) {
        StringBuffer sb = new StringBuffer();
        String[] split = yuleContent.split("\\\n");
        String substring = split[52].substring(22, split[52].length() - 25);
        String s = StringEscapeUtils.unescapeJava(substring);
        Document parse = Jsoup.parse(s);
        Element body = parse.body();
        Element child = body.child(0);
        Elements children = child.children();
        for (Element element : children) {
            String nodeName = element.nodeName();
            if ("div".equals(nodeName)) {
                Elements children1 = element.children();
                children1.forEach(l -> {
                    String text = l.nodeName();
                    if ("img".equals(text)) {
                        sb.append("此处有图片\n");
                    }
                });
            } else if ("p".equals(nodeName)) {
                String text = element.text();
                if (StringUtils.isNotBlank(text)) {
                    sb.append(text + "\n");
                }
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://www.toutiao.com/a6732682872043340291/";
        String yuleContent = restTemplate.getForObject(url, String.class);
        String yuLeContent = getYuLeContent(yuleContent);
        System.out.println(yuLeContent);
    }
}
