package com.apass.zufang.service.spider;

import com.apass.gfb.framework.utils.HttpClientUtils;
import com.apass.zufang.domain.dto.ProxyIpJo;
import org.apache.commons.collections.CollectionUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.redis.connection.ConnectionUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by DELL on 2018/5/9.
 * 获取代理ip，端口
 */
@Component
public class ProxyIpHandler {

    /**
     * 爬取http://www.xicidaili.com/nn/1 代理ip
     * page 默认从1开始
     */
    public static  List<ProxyIpJo> catchProxyIp(int page) throws Exception{
        List<ProxyIpJo> result = new ArrayList<>();
        String address = "http://www.xicidaili.com/nn/" + page;
        String content = HttpClientUtils.getMethodGetResponse(address);

        Document document = Jsoup.parse(content);
        Element element = document.getElementById("ip_list");
        Elements elements = element.getElementsByTag("tr");

        for (Element ele : elements) {
            Elements tds = ele.getElementsByTag("td");
            if (tds.size() < 5) {
                continue;
            }
            String ip = tds.get(1).text();
            String port = tds.get(2).text();
            String speed = tds.get(6).getElementsByAttribute("title").attr("title");
            String connectSpeed = tds.get(7).getElementsByAttribute("title").attr("title");
            int index1 = speed.indexOf("秒");
            int index2 = connectSpeed.indexOf("秒");
            float speedF = Float.valueOf(speed.substring(0,index1));
            float connectSpeedF = Float.valueOf(connectSpeed.substring(0,index2));
            if(speedF > 2.5F){
                continue;
            }
            result.add(new ProxyIpJo(ip,Integer.valueOf(port),speedF,connectSpeedF));
        }
        if(CollectionUtils.isEmpty(result)){
            //查询第二页
            return catchProxyIp(2);
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
         catchProxyIp(1);

    }

}
