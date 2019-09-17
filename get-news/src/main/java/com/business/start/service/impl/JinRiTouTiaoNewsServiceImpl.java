package com.business.start.service.impl;

import com.business.start.constants.NewsTypeEnum;
import com.business.start.constants.SourceEnum;
import com.business.start.domain.IPBean;
import com.business.start.domain.NewsImage;
import com.business.start.domain.NewsTitle;
import com.business.start.service.DataService;
import com.business.start.utils.HtmlUtils;
import com.business.start.utils.JinRiTouTiaoUtils;
import com.business.start.utils.ProxyUtils;
import com.business.start.utils.ServiceResponse;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service(value = "jinRiTouTiaoNewsService")
public class JinRiTouTiaoNewsServiceImpl implements DataService {

    private String NEWS_HOT_URL = "https://www.toutiao.com/api/pc/feed/?category=news_hot&utm_source=toutiao&widen=1&max_behot_time=0&max_behot_time_tmp=0&tadrequire=true&cp=5D6E263BC070BE1";
    private String YULE_URL = "https://www.toutiao.com/api/pc/feed/?category=news_entertainment&utm_source=toutiao&widen=1&max_behot_time=0&max_behot_time_tmp=0&tadrequire=true";

    @Resource
    private RestTemplate restTemplate;

    private static final String jinritoutiaoUrl = "https://www.toutiao.com";

    @Override
    public ServiceResponse read() {
        String as = "A1C5AD77334080D";
        String cp = "5D737008F0BD2E1";
        String signature = "3302ZwAAghWUEAR8eO7wlN99Nn";
        readReDian(as, cp, signature);
        return null;
    }

    private void readReDian(String as, String cp, String signature) {
        String url = YULE_URL + "&as=" + as + "&cp=" + cp + "&_signature=" + signature;
        IPBean ipBean = IPBean.builder()
                .ip("114.215.139.19")
                .port(8118)
                .type(IPBean.TYPE_HTTP)
                .build();
        String responseContent;
        try {
            responseContent = ProxyUtils.getResponseContent(url, null, ipBean);
            JSONObject jsonObject = JSONObject.fromObject(responseContent);
            log.info(responseContent);
        } catch (Exception e) {
            return;
        }


        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Resource> httpEntity = new HttpEntity<>(headers);
        headers.set(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/14.0.835.163 Safari/535.1");
        ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.GET,
                httpEntity, byte[].class);
        byte[] body = response.getBody();
        String s = new String(body);
        log.info(">> {}", s);

        JSONObject result = restTemplate.getForObject(url, JSONObject.class);
        log.info("url={},result={}", url, result.toString());
        if (!result.containsKey("message")) {
            return;
        }
        String message = result.getString("message");
        if (StringUtils.isEmpty(message) || !message.equals("success")) {
            return;
        }
        if (!result.containsKey("data")) {
            return;
        }
        JSONArray data = result.getJSONArray("data");
        if (null == data || data.size() == 0) {
            return;
        }

        data.stream().forEach(jsonObject -> {
            JSONObject titleJson = (JSONObject) jsonObject;
            Object groupIdObj = titleJson.get("group_id");
            Object title = titleJson.get("title");
            Object source = titleJson.get("source"); // 来源 今日头条 注册名称
            Object imageList = titleJson.get("image_list");
            Object sendTime = titleJson.get("behot_time"); // 发布时间
            Object sourceUrl = titleJson.get("source_url"); // 推荐内容地址
            if (ObjectUtils.isEmpty(groupIdObj) ||
                    ObjectUtils.isEmpty(title) ||
                    ObjectUtils.isEmpty(source) ||
                    ObjectUtils.isEmpty(imageList) ||
                    ObjectUtils.isEmpty(sendTime) ||
                    ObjectUtils.isEmpty(sourceUrl)) {
                return;
            }
            List<NewsImage> list = new ArrayList<>();
            String groupId = groupIdObj.toString();
            JSONArray imageJson = JSONArray.fromObject(imageList);
            // 娱乐新闻内容
            String yuleUrl = jinritoutiaoUrl + "/" + groupId;
            // 娱乐新闻原始内容
            String yuleContent = restTemplate.getForObject(yuleUrl, String.class);
            String content = JinRiTouTiaoUtils.getYuLeContent(yuleContent);

            // 娱乐新闻解密内容
            for (int i = 0; i < imageJson.size(); i++) {
                JSONObject json = (JSONObject) imageJson.get(i);
                String imageurl = "http" + json.getString("url");
//                NewsImage build = NewsImage.builder()
//                        .url(imageurl)
//                        .groupId(groupId)
//                        .source(String.valueOf(SourceEnum.JINRITOUTIAO.getCode())) // 来源
//                        .type(String.valueOf(NewsTypeEnum.YULE.getCode())) // 类别
//                        .build();
//                list.add(build);
                String imageLabel = HtmlUtils.getImageLabel(imageurl);
                content.replace("此处有图片", imageLabel);
            }
            JSONObject jsonObject1 = JSONObject.fromObject(list);
            System.out.println("iamgeList=" + jsonObject1);
            NewsTitle build = NewsTitle.builder()
                    .groupId(groupId)
                    .title(title.toString())
                    .category(NewsTypeEnum.YULE.getCode().toString())
                    .dataSource(SourceEnum.JINRITOUTIAO.getCode().toString())
                    .source(source.toString())
                    .content(content)
                    .releasTeime(Long.parseLong(sendTime.toString()))
                    .build();
            System.out.println("NewsTitle=" + build.toString());
        });
    }
}
