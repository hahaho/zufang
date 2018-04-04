package com.apass.zufang.service.spider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 2018/4/2.
 */
@Service
public class HouseSpiderService {
    public static final Logger log = LoggerFactory.getLogger(HouseSpiderService.class);

    /**
     * 【蘑菇租房】解析房源详情页
     */
    public void parseMogoroomHouseDetail(String houseUrl){
        try {
            houseUrl = "http://www.mogoroom.com/room/605086.shtml?page=list";
            Document doc = Jsoup.connect(houseUrl).get();
            System.out.println(doc.text());
            Elements titleElements = doc.select("span.room-info-tit");
            String title = titleElements.get(0).text(); //标题
            Elements rentElements = doc.select("span.tx-middle");
            String rentTypeStr =  rentElements.get(0).text();//合租方式
            String zujinTypeStr = rentElements.get(2).text();//租金类型
            Elements rentAmtEles = doc.select("span.xianjia.darkorange");
            String rentAmt = rentAmtEles.get(0).text();//租金
            Elements housekeeperTelEles = doc.select("p.phone-number");
            String housekeeperTel = housekeeperTelEles.get(0).text();//管家联系方式

            Elements detailRoomEles = doc.select("div.room-rs");
            Elements ulEle = detailRoomEles.select("li");
            Element huxingEle = ulEle.get(2);
            String huxingStr = huxingEle.text();// 6室1厅2卫
            Element acreageEle = ulEle.get(3);
            String acreageStr = acreageEle.text();//面积： 28.0㎡/130.0㎡
            Element floorEle = ulEle.get(5);;
            String floorStr = floorEle.text();//楼层：1/6层

            //图片url
            Elements scriptEles = doc.getElementsByTag("script");
            List<String> imgUrls = new ArrayList<>();
          outer:  for(Element ele : scriptEles) {
                List<DataNode> children = ele.dataNodes();
                for(DataNode cld : children){
                   Document scriptDoc = Jsoup.parse(children.get(0).getWholeData());
                   if(scriptDoc.select("div.ms-stage").size()>0){
                     Elements imgEles =  scriptDoc.select("img");
                     for (Element imgEle : imgEles){
                         if(imgEle.hasClass("swiper-mobile-img")){
                             imgUrls.add(imgEle.attr("data-src"));
                         }
                     }
                     break outer;

                   }
                }
        }



        }catch (Exception e){
            log.error("parseMogoroomHouseDetail error.......",e);
        }

    }

    public static void main(String[] args) {
        HouseSpiderService s = new HouseSpiderService();
        s.parseMogoroomHouseDetail("");
    }
}
