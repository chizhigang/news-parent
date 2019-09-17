package com.business.start.utils;

import com.business.start.domain.NewsImage;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class QinBaoShanUtils {

    private static final String url = "https://feedapi.zhangxinhulian.com/h5/v1/news/content?key=%key%&channel=%channel%&appKey=56bad9a0" ;

    public static String getContent(String key,String channel, List<NewsImage> list){

        String replace = url.replace("%key%", key).replace("%channel%", channel);
        RestTemplate restTemplate = new RestTemplate() ;
        JSONObject forObject = restTemplate.getForObject(replace, JSONObject.class);
        if(200!=forObject.getInt("status")){
            return "" ;
        }
        JSONObject result = forObject.getJSONObject("result");
        JSONObject data = result.getJSONObject("data");
        // 原始的
        String html = data.getString("html");
        Document parse = Jsoup.parse(html);
        Element body = parse.body();
        Element child = body.child(0);
        Elements children = child.children();
        Map<String, NewsImage> collect = list.stream().collect(Collectors.toMap(NewsImage::getIamgeOldName, l -> l));
        for (Element element : children) {
            String nodeName = element.nodeName();
            if ("p".equals(nodeName)) {
                String html1 = element.html();
                System.out.println("p="+html1);
                Elements children1 = element.children();
                children1.forEach(l -> {
                    String text = l.nodeName();
                    if ("img".equals(text)) {
                        String src = l.attr("src");
                        NewsImage imageInfoToUrl = ImageUtils.getImageInfoToUrl(src);
                        NewsImage newsImage = collect.get(imageInfoToUrl.getIamgeOldName());
                        System.out.println("image="+newsImage.getIamgeName());
                    }
                });
            }
        }

        return "" ;
    }

    public static String getContent(String url,List<NewsImage> list){

        Document doc = JsoupUtils.getDoc(url);
        Element first = doc.select("section.contfather").first();
        Element child1 = first.child(0).child(0).child(0);
        Elements children2 = child1.children();
        Map<String, NewsImage> collect = list.stream().collect(Collectors.toMap(NewsImage::getIamgeOldName, l -> l));
        for (Element element : children2) {
            String nodeName = element.nodeName();
            if ("p".equals(nodeName)) {
                String html1 = element.html();
                System.out.println("p="+html1);
                Elements children1 = element.children();
                children1.forEach(l -> {
                    String text = l.nodeName();
                    if ("img".equals(text)) {
                        String src = l.attr("src");
                        NewsImage imageInfoToUrl = ImageUtils.getImageInfoToUrl(src);
                        NewsImage newsImage = collect.get(imageInfoToUrl.getIamgeOldName());
                        System.out.println("image="+newsImage.getIamgeName());
                    }
                });
            }
        }

        return "" ;
    }



}
