package com.business.start.service.impl;

import com.business.start.domain.IPBean;
import com.business.start.service.DataService;
import com.business.start.utils.ProxyUtils;
import com.business.start.utils.ServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service(value = "xiCiService")
public class XiCiServiceImpl implements DataService {

    @Override
    public ServiceResponse read() {
        String url = "http://www.xicidaili.com/nn/";
        try {
            Elements elements = getRequestMethod(url);
            List<IPBean> ipList = getElementsByTags(elements);
            return ServiceResponse.ok(ipList);
        } catch (Exception e) {
            log.error("读取西刺异常", e);
            return ServiceResponse.sysTemError("系统异常");
        }
    }


    private Elements getRequestMethod(String url) throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        //setConnectTimeout(10000)连接超时时间（单位豪秒）
        //setSocketTimeout(10000)读取超时时间（单位豪秒）
        RequestConfig config = RequestConfig.custom().setConnectTimeout(10000).setSocketTimeout(10000).build();
        httpGet.setConfig(config);
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64)"
                + " AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36");
        CloseableHttpResponse response = client.execute(httpGet);
        HttpEntity entity = response.getEntity();    //获取返回实体
        String html = "";
        if (entity != null) {
            html = EntityUtils.toString(entity);
        }
        response.close();            //关闭流和和释放资源
        Document doc = Jsoup.parse(html);        //解析网页得到文档对象
        /**
         * getElementById(String id) 根据id来查询DOM
         * getElementsByTag(String tagName) 根据tag名称来查询DOM
         * getElementsByClass(String className) 根据样式名称来查询DOM
         * getElementsByAttribute(String key) 根据属性名来查询DOM
         * getElementsByAttributeValue(String key,String value)  根据属性名和属性值来查询DOM
         */
        Elements tags = doc.select("#ip_list > tbody > tr");

        return tags;
    }

    public List<IPBean> getElementsByTags(Elements tags) {
        List<IPBean> ipList = new ArrayList<>();
        int count = 0;
        //遍历tbody子节点
        for (Element element : tags) {
            //取得ip地址节点
            Elements tdChilds = element.select("tr > td:nth-child(2)");
            //取得端口号节点
            Elements tcpd = element.select("tr > td:nth-child(3)");
            Elements http = element.select("tr > td:nth-child(6)");
            String ip = tdChilds.text();
            String port = tcpd.text();
            String type = http.text();
            if (StringUtils.isEmpty(ip) || StringUtils.isEmpty(port) || StringUtils.isEmpty(type)) {
                continue;
            }
            IPBean ipBean = IPBean.builder()
                    .ip(tdChilds.text())
                    .port(Integer.parseInt(tcpd.text()))
                    .type(http.text())
                    .build();
            if(!ProxyUtils.isValid(ipBean)){
                continue;
            }
            log.info(ipBean.toString());
            ipList.add(ipBean);
        }
        return ipList;
    }

}
