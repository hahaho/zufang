package com.apass.zufang.service.spider;
import com.apass.zufang.domain.common.Geocodes;
import com.apass.zufang.domain.entity.Apartment;
import com.apass.zufang.domain.enums.BusinessHouseTypeEnums;
import com.apass.zufang.domain.vo.HouseVo;
import com.apass.zufang.mapper.zfang.ApartmentMapper;
import com.apass.zufang.service.house.HouseService;
import com.apass.zufang.utils.ObtainGaodeLocation;
import com.apass.zufang.utils.ToolsUtils;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Created by DELL on 2018/4/2.
 */
@Service
public class HouseSpiderService {
    public static final Logger log = LoggerFactory.getLogger(HouseSpiderService.class);

    @Autowired
    private ObtainGaodeLocation otainGaodeLocation;

    @Autowired
    private HouseService houseService;

    @Autowired
    private ApartmentMapper apartmentMapper;



    public void batchParseMogoroomHouse(List<String> urls){
        for(String url : urls){
            parseMogoroomHouseDetail(url);
            try {
                Thread.sleep(2000);
            }catch (Exception e){

            }
        }
    }


    /**
     * 【蘑菇租房】解析房源详情页
     */
    public void parseMogoroomHouseDetail(String houseUrl){
        try {
            log.info("-------start visiting mogo room,url: {} ,--------",houseUrl);
            final WebClient webClient = new WebClient(BrowserVersion.CHROME);
            webClient.getOptions().setCssEnabled(false);//关闭css
            webClient.getOptions().setJavaScriptEnabled(true);

            final HtmlPage page = webClient.getPage(houseUrl);
            Thread.sleep(10000);
            System.out.println(page.asXml());
            Document doc = Jsoup.parse(page.asXml());

           Elements noHouseEle = doc.select("div.f30.white");
            if(noHouseEle.size() > 0){
                String text = noHouseEle.get(0).text();
                if(text.contains("已被出租")){
                    //该房源已被出租
                    return;
                }
            }


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
            
            List<String> huxinglist = getMatcheNum(huxingStr);
            String room = huxinglist.get(0);
            String hall = huxinglist.get(1);
            String wei = huxinglist.get(2);
            List<String> acreagelist = getMatcheNum(acreageStr);
            String roomAcreage = null;
            String acreage = null;
            if(acreagelist.size()  ==  1){
                 acreage = acreagelist.get(0);
            }else {
                roomAcreage = acreagelist.get(0);
                acreage = acreagelist.get(1);
            }
            List<String> floorlist = getMatcheNum(floorStr);
            String floor = floorlist.get(0);
            String totalFloor = floorlist.get(1);
            
            //图片url
            Elements scriptEles = doc.getElementsByTag("script");
            List<String> imgUrls = new ArrayList<>();
            outer:  for(Element ele : scriptEles) {
                  Element e =  ele.getElementById("swiperTemplate");
                  if(e != null){
                      if(e.select("div.ms-stage").size()>0){
                          Elements imgEles =  e.select("img");
                          for (Element imgEle : imgEles){
                              if(imgEle.hasClass("swiper-mobile-img")){
                                  imgUrls.add(imgEle.attr("data-src"));
                              }
                          }
                          break outer;
                      }
                  }

            }
           //房源配置信息
            List<String> roomConfigStrList = new ArrayList<>();
            Element roomConfigEle = doc.getElementById("roomConfig");
            Elements roomConfigs = roomConfigEle.select("li");
            for(Element con : roomConfigs) {
                if(con.hasClass("f12") && !con.hasClass("darkgray")){
                    roomConfigStrList.add(con.text());
                }
            }
            //朝向
            Element roomMatesEle = doc.getElementById("roomMates");
            String chaoxiang= "";
            if(roomMatesEle != null){
                Elements curEles = roomMatesEle.select("li.cur-rm");
                 chaoxiang = curEles.select("li").get(3).text();

            }

            Elements addrEle = doc.select("span.roomInfo-mark");
            String address = addrEle.get(0).text(); //翰盛家园（上海市浦东新区创新西路195号）

            //小区名称
            int index = StringUtils.indexOf(address,"（");
            String communityName = null;
            if(index != -1){
                communityName = StringUtils.substring(address,0,index);
            }
            String[] titleArray = title.split("-");
            address = titleArray[0] + address;
            Geocodes geocodes = otainGaodeLocation.getLocationAddress(address);
            String[] locationArray = StringUtils.split(geocodes.getLocation(),",");
            String lon = locationArray[0];//经度
            String lat = locationArray[1];//纬度

            //数据库插入房源信息
            HouseVo houseVo = new HouseVo();
            houseVo.setApartmentId(100L);
            if(acreage != null ){
                houseVo.setAcreage(new BigDecimal(acreage));
            }
            houseVo.setChaoxiang(Byte.valueOf(BusinessHouseTypeEnums.getCXCode(chaoxiang)));
            houseVo.setCommunityName(communityName);
            houseVo.setHall(Integer.valueOf(hall));
            houseVo.setFloor(Integer.valueOf(floor));
            houseVo.setConfigs(roomConfigStrList);
            houseVo.setHezuChaoxiang(Byte.valueOf(BusinessHouseTypeEnums.getCXCode(chaoxiang)));
            houseVo.setHousekeeperTel(housekeeperTel);
            houseVo.setRoom(Integer.valueOf(room));
            houseVo.setWei(Integer.valueOf(wei));
            houseVo.setTotalFloor(Integer.valueOf(totalFloor));
            houseVo.setRentType(Byte.valueOf(BusinessHouseTypeEnums.getHZCode(rentTypeStr)));
            houseVo.setZujinType(Byte.valueOf(BusinessHouseTypeEnums.getYJLXCode(zujinTypeStr)));
            houseVo.setPictures(imgUrls);
            houseVo.setCity(geocodes.getCity());
            houseVo.setProvince(geocodes.getProvince());
            Apartment part = apartmentMapper.selectByPrimaryKey(houseVo.getApartmentId());
            houseVo.setCode(ToolsUtils.getLastStr(part.getCode(), 2).concat(String.valueOf(ToolsUtils.fiveRandom())));
            houseVo.setCreatedTime(new Date());
            houseVo.setUpdatedTime(new Date());
            houseVo.setDetailAddr(address);
            houseVo.setDistrict(geocodes.getDistrict());
            houseVo.setTitle(title);
            if(roomAcreage != null){
                houseVo.setRoomAcreage(new BigDecimal(roomAcreage));
            }
            houseVo.setRentAmt(new BigDecimal(rentAmt));
            houseVo.setLatitude(Double.valueOf(lat));
            houseVo.setLongitude(Double.valueOf(lon));
            houseVo.setCreatedUser("spiderAdmin");
            houseVo.setUpdatedUser("spiderAdmin");
            houseVo.setHouseStatus("1");
            Map<String,Object> result =  houseService.addHouse(houseVo);
            log.info("-------end visit mogo room,houseId: {}--------",result.get("houseId"));
        }catch (Exception e){
            log.error("parseMogoroomHouseDetail error.......",e);
        }
    }
    /**
     * 截取数字
     * @param target
     * @return
     */
    private List<String> getMatcheNum(String target){
    	List<String> result = new ArrayList<String>();
    	Pattern p = Pattern.compile("([1-9]+[0-9]*|0)(\\.[\\d]+)?");
    	Matcher m = p.matcher(target);
    	while (m.find()) {
    		result.add(m.group());
    	}
    	return result;
    }
    public static void main(String[] args) {
        HouseSpiderService s = new HouseSpiderService();
        s.parseMogoroomHouseDetail("");
    	
    	
//    	String target = "楼层：1/6层";
//    	List<String> result = new ArrayList<String>();
//    	Pattern p = Pattern.compile("([1-9]+[0-9]*|0)(\\.[\\d]+)?");
//    	Matcher m = p.matcher(target);
//    	while (m.find()) {
//    		result.add(m.group());
//    	}
//    	System.out.println(result);
    }
}
