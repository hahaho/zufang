package com.apass.zufang.service.spider;

import com.apass.gfb.framework.logstash.LOG;
import com.apass.gfb.framework.utils.DateFormatUtil;
import com.apass.gfb.framework.utils.GsonUtils;
import com.apass.gfb.framework.utils.RandomUtils;
import com.apass.zufang.common.utils.MyStringUtil;
import com.apass.zufang.domain.common.Geocodes;
import com.apass.zufang.domain.dto.ProxyIpJo;
import com.apass.zufang.domain.entity.Apartment;
import com.apass.zufang.domain.entity.ZfangSpiderHouseEntity;
import com.apass.zufang.domain.enums.BusinessHouseTypeEnums;
import com.apass.zufang.domain.enums.IsDeleteEnums;
import com.apass.zufang.domain.enums.SpiderCityUrlsEnum;
import com.apass.zufang.domain.vo.HouseVo;
import com.apass.zufang.mapper.zfang.ApartmentMapper;
import com.apass.zufang.mapper.zfang.ZfangSpiderHouseEntityMapper;
import com.apass.zufang.service.house.HouseService;
import com.apass.zufang.utils.ObtainGaodeLocation;
import com.apass.zufang.utils.ToolsUtils;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Created by DELL on 2018/4/2.
 */
@Service
public class HouseSpiderService {
    public static final Logger log = LoggerFactory.getLogger(HouseSpiderService.class);

    @Autowired
    private ProxyIpHandler proxyIpHandler;


    /**
     * 蘑菇公寓id
     */
    //TODO 生产环境会有改动
    private static final Long APARTMENT_ID = Long.valueOf(100);
    /**
     * 最近跑job时间的house和minute:每天00:10跑job
     */
    private static final int CRON_HOUSE = 0;
    private static final int CRON_MINUTE = 10;

    @Autowired
    private ObtainGaodeLocation otainGaodeLocation;

    @Autowired
    private HouseService houseService;

    @Autowired
    private ApartmentMapper apartmentMapper;

    @Autowired
    private ZfangSpiderHouseEntityMapper spiderHouseMapper;

    /**
     *获取随机等待时间(毫秒)
     *1~11 秒
     */
    private long getSleepTime(){
       return RandomUtils.getRandomInt(1000,11000);
    }

    private WebDriver getWebDriver() throws Exception{
        List<ProxyIpJo> proxyIpJoList = proxyIpHandler.getIpListFromRedis();
        if(CollectionUtils.isEmpty(proxyIpJoList)){
            proxyIpJoList = proxyIpHandler.putIntoRedis();
        }
        int size = proxyIpJoList.size();
        int random = RandomUtils.getRandomInt(0,size);
        ProxyIpJo proxyIpJo = proxyIpJoList.get(random);
        log.info("-------getWebDriver， current proxyIp:{},port:{}--------",proxyIpJo.getProxyHost(),proxyIpJo.getProxyPort());
        String ip = proxyIpJo.getProxyHost() +":"+proxyIpJo.getProxyPort();
        return new PhantomPoolBuilder().buildSingleDriver(ip);
    }

    private String  getPorxyIp() throws Exception{
        List<ProxyIpJo> proxyIpJoList = proxyIpHandler.getIpListFromRedis();
        if(CollectionUtils.isEmpty(proxyIpJoList)){
            proxyIpJoList = proxyIpHandler.putIntoRedis();
        }
        int size = proxyIpJoList.size();
        int random = RandomUtils.getRandomInt(0,size);
        ProxyIpJo proxyIpJo = proxyIpJoList.get(random);
        log.info("-------getPorxyIp， current proxyIp:{},port:{}--------",proxyIpJo.getProxyHost(),proxyIpJo.getProxyPort());
        String ip = proxyIpJo.getProxyHost() +":"+proxyIpJo.getProxyPort();
        return ip;
    }

    public void batchParseMogoroomHouse(List<String> urls,String host){
        for(String url : urls){
            parseMogoroomHouseDetail(url,host);
        }
    }

    @Transactional(value="transactionManager",rollbackFor = { Exception.class,RuntimeException.class})
    public void spiderMogoroomPageList(String baseUrl,Integer page){
        List<ZfangSpiderHouseEntity> zfangSpiderHouseEntities = parseMogoroomHouseList(baseUrl, page);
        LOG.info("第{}页添加的房源内容有:{}",page.toString(), GsonUtils.toJson(zfangSpiderHouseEntities));
    }


    /**
     * 【蘑菇租房】解析房源详情页
     */
    @Transactional(value="transactionManager",rollbackFor = { Exception.class,RuntimeException.class})
    public void parseMogoroomHouseDetail(String houseUrl,String host){
        WebDriver webDriver = null;
        try {
            log.info("-------start visiting mogo room detail,url: {} ,--------",houseUrl);
            Thread.sleep(getSleepTime());

            String ref = host + "/list";
            String headerHost = host.substring("http://".length());
            String htmlStr = "";
            String proxyIp = null;
            for(int i = 0;i<10;i++){
                try {
                   htmlStr = PhantomPoolBuilder.getHtmlByPhantomJs(houseUrl,headerHost,ref,proxyIp);
                   if(StringUtils.isNotEmpty(htmlStr)){
                       break;
                   }else{
                       proxyIp =getPorxyIp();
                   }
                }catch (RuntimeException e){
                    log.error("----parseMogoroomHouseDetail Exception -----{}",e);
                } catch (Exception e2){
                    log.error("----parseMogoroomHouseDetail Exception -----{}",e2);
                }
            }
            if(StringUtils.isEmpty(htmlStr)){
                return ;
            }

            Document doc = Jsoup.parse(htmlStr);

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
                  Element e =  Jsoup.parse(ele.html().replace("//<![CDATA[",""));
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
                if(curEles.size() == 0 || curEles.select("li").size() == 0){

                } else{
                    chaoxiang = curEles.select("li").get(3).text();
                }
            }

            Elements addrEle = doc.select("span.roomInfo-mark");
            String address = "";
            if(addrEle.size()>0){
                address = addrEle.get(0).text(); //翰盛家园（上海市浦东新区创新西路195号）
            }else{
                String addrMark = "<span class=\"roomInfo-mark\"><i class=\"mark-arrow\"></i>";
                int i = htmlStr.indexOf(addrMark);
                int j = htmlStr.substring(i+addrMark.length()).indexOf("）");
                address = htmlStr.substring(i+addrMark.length()).substring(0,j+1);
            }


            //小区名称
            int index = StringUtils.indexOf(address,"（");
            String communityName = null;
            if(index != -1){
                communityName = StringUtils.substring(address,0,index);
            }
            SpiderCityUrlsEnum spiderCityUrlsEnum =  SpiderCityUrlsEnum.getEnum(houseUrl);
            String[] titleArray = title.split("-");
            address = spiderCityUrlsEnum.getProvince() + spiderCityUrlsEnum.getCity() + titleArray[0] + address;
            Geocodes geocodes = otainGaodeLocation.getLocationAddress(address);
            String[] locationArray = StringUtils.split(geocodes.getLocation(),",");
            String lon = locationArray[0];//经度
            String lat = locationArray[1];//纬度

            //数据库插入房源信息
            HouseVo houseVo = new HouseVo();
            String extHouseId = MyStringUtil.getNumFromStr(houseUrl);
            houseVo.setExtHouseId(extHouseId);
            houseVo.setApartmentId(APARTMENT_ID);
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
            houseVo.setCity(spiderCityUrlsEnum.getCity());
            houseVo.setProvince(spiderCityUrlsEnum.getProvince());
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
            if(rentAmt.contains("-")){
                rentAmt = rentAmt.split("-")[0];
            }
            houseVo.setRentAmt(new BigDecimal(rentAmt));
            houseVo.setLatitude(Double.valueOf(lat));
            houseVo.setLongitude(Double.valueOf(lon));
            houseVo.setCreatedUser("spiderAdmin");
            houseVo.setUpdatedUser("spiderAdmin");
            houseVo.setHouseStatus("1");
            Map<String,Object> result =  houseService.addHouse(houseVo);
            if(result.get("houseId") != null ) {
                //把t_zfang_spider_house对应此条数据标记为删除
                ZfangSpiderHouseEntity entity = new ZfangSpiderHouseEntity();
                entity.setExtHouseId(MyStringUtil.getNumFromStr(houseUrl));
                entity.setIsDelete(IsDeleteEnums.IS_DELETE_01.getCode());
                spiderHouseMapper.updateByExtHouseIdSelective(entity);
            }

            log.info("-------end visit mogo room,houseId: {}--------",result.get("houseId"));
        }catch (Exception e){
            log.error("parseMogoroomHouseDetail error.......",e);
        }finally {
            if(webDriver != null){
                webDriver.quit();
            }
        }
    }

    /**
     * 【蘑菇租房】解析房源列表
     * @param pageNum,页码
     */
    @Transactional(value="transactionManager",rollbackFor = { Exception.class,RuntimeException.class})
    public List<ZfangSpiderHouseEntity> parseMogoroomHouseList(String baseUrl,Integer pageNum) {
        //Map的key是id,value是url
        WebDriver webDriver = null;
        List<ZfangSpiderHouseEntity> zfangSpiderHouseEntities = Lists.newArrayList();
        try {
            String houseUrl = baseUrl+"?page="+pageNum;
            log.info("-------start visiting mogo room list,url: {} ,--------", houseUrl);
            Thread.sleep(getSleepTime());

            String htmlStr = "";
            String host = baseUrl.substring(0,"/list".length());
            String ref = host + "/index.shtml";
            String headerHost = host.substring("http://".length());
            String proxyIp = null;
            for(int i = 0;i<10;i++){
                try {
                    htmlStr = PhantomPoolBuilder.getHtmlByPhantomJs(houseUrl,headerHost,ref,proxyIp);
                    if(StringUtils.isNotEmpty(htmlStr)){
                        break;
                    }else{
                        proxyIp =getPorxyIp();
                    }
                }catch (RuntimeException e){
                    log.error("----parseMogoroomHouseDetail Exception -----{}",e);
                } catch (Exception e2){
                    log.error("----parseMogoroomHouseDetail Exception -----{}",e2);
                }
            }
            if(StringUtils.isEmpty(htmlStr)){
                return  null;
            }

            System.out.println(htmlStr);
            Document doc = Jsoup.parse(htmlStr);
            //当前城市
            String city = doc.select("div.current-city").get(0).text();
            Elements ulElement = doc.select("ul.list-room.add-new-listroom.by-list");
            Elements roomConfigs = ulElement.select("li");
            for(int i=0; i<roomConfigs.size(); i++){
                ZfangSpiderHouseEntity zfangSpiderHouseEntity = new ZfangSpiderHouseEntity();
                zfangSpiderHouseEntity.setCity(city);
                zfangSpiderHouseEntity.setApartmentId(APARTMENT_ID);
                zfangSpiderHouseEntity.setCreatedTime(new Date());
                zfangSpiderHouseEntity.setUpdatedTime(new Date());
                Date jobTime = DateFormatUtil.mergeHouseAndMinute(new Date(),CRON_HOUSE,CRON_MINUTE);
                zfangSpiderHouseEntity.setLastJobTime(jobTime);
                Element element = roomConfigs.get(i);
                //外部房源id
                String idKey = element.select("a.inner").attr("data-roomid-md");
                zfangSpiderHouseEntity.setExtHouseId(idKey);
                //外部房源url
                String hrefValue = element.select("a.inner").attr("href");
                zfangSpiderHouseEntity.setUrl(hrefValue);
                zfangSpiderHouseEntity.setHost(host);
                zfangSpiderHouseEntity.setIsDelete(IsDeleteEnums.IS_DELETE_00.getCode());

                zfangSpiderHouseEntities.add(zfangSpiderHouseEntity);
                ZfangSpiderHouseEntity entity = spiderHouseMapper.selectByExtHouseId(idKey);
                if(entity != null){
                    continue;
                }
                spiderHouseMapper.insertSelective(zfangSpiderHouseEntity);
            }

            return zfangSpiderHouseEntities;
        } catch (Exception e) {
            log.error("爬取蘑菇租房列表页异常,----Splider Exception -----{}",e);
            return null;
        } finally {
            if(webDriver != null){
                webDriver.quit();
            }
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



    public List<ZfangSpiderHouseEntity> listAllExtHouse() {

        return spiderHouseMapper.listAllExtHouse();
    }
}
