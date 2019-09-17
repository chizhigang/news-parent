package com.business.start.service.impl;

import com.business.start.constants.NewsTypeEnum;
import com.business.start.constants.SourceEnum;
import com.business.start.domain.NewsImage;
import com.business.start.domain.NewsTitle;
import com.business.start.service.DataService;
import com.business.start.utils.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service(value = "baoShanService")
public class BaoShanServiceImpl implements DataService {

//    @Resource
    private RestTemplate restTemplate = new RestTemplate();

    private static String url = "https://feedapi.zhangxinhulian.com/h5/v1/news/list?appKey=56bad9a0&channel=yule&num=10&page=1";

    private static final String targetDir = "E:/image/";

    @Override
    public ServiceResponse read() {
        tuijian();
        return null;
    }

    private void tuijian() {

        JSONObject resultJson = restTemplate.getForObject(url, JSONObject.class);
        Object status = resultJson.get("status");
        Object result = resultJson.get("result");
        if (ObjectUtils.isEmpty(status) || !"200".equals(status.toString()) ||
                ObjectUtils.isEmpty(result)) {
            return;
        }
        JSONObject result1 = resultJson.getJSONObject("result");
        JSONArray data = result1.getJSONArray("data");
        if (ObjectUtils.isEmpty(data)) {
            return;
        }
        String yearMonthDay = targetDir + DateUtils.getYearMonthDay();
        data.forEach(l -> {
            String groupId = UUIDUtils.getId(); // 消息id
            JSONObject json = (JSONObject) l;
            Object imageObj = json.get("images");
            String title = json.getString("title");
            String source = json.getString("source");
            String date = json.getString("date");
            String pk = json.getString("pk");
            String url = json.getString("url");
            Long aLong = DateUtils.strToLong(date);
            List<NewsImage> imageList = new ArrayList<>();
            if (null != imageObj) {
                JSONArray images = json.getJSONArray("images");
                images.forEach(image -> {
                    String imageUrl = image.toString();
                    NewsImage newsImage = ImageUtils.getImageInfoToUrl(imageUrl);
                    if (newsImage == null)
                        return;
                    String imageName = UUIDUtils.getUuidFor8() + newsImage.getIamgeSuffix();
                    newsImage.setIamgeName(imageName);
                    newsImage.setGroupId(groupId);
                    ImageUtils.imageDownload(imageUrl,yearMonthDay, imageName);
                    imageList.add(newsImage);
                });
            }
            QinBaoShanUtils.getContent(url, imageList);
            NewsTitle.builder()
                    .groupId(groupId)
                    .title(title)
                    .source(source)
                    .dataSource(SourceEnum.QINBAOSHAN.getCode().toString())
                    .category(NewsTypeEnum.REDIAN.getCode().toString())
                    .releasTeime(aLong)
                    .category("");
        });

    }

    public static void main(String[] args) {
        DataService dataService = new BaoShanServiceImpl() ;
        dataService.read();
        System.out.printf("完成");
    }

}
