package com.apass.zufang.service.spider;
import com.gargoylesoftware.htmlunit.BrowserVersion;
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
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
            houseUrl = "http://www.mogoroom.com/room/605086.shtml";
            final WebClient webClient = new WebClient(BrowserVersion.CHROME);
            webClient.getOptions().setCssEnabled(false);//关闭css
            webClient.getOptions().setJavaScriptEnabled(true);
            final HtmlPage page = webClient.getPage(houseUrl);
            Thread.sleep(10000);
            System.out.println(page.asXml());
            Document doc = Jsoup.parse(page.asXml());
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
            
            List<String> huxinglist = getMatcheNum(huxingStr);
            String room = huxinglist.get(0);
            String hall = huxinglist.get(1);
            String wei = huxinglist.get(2);
            List<String> acreagelist = getMatcheNum(acreageStr);
            String roomAcreage = acreagelist.get(0);
            String acreage = acreagelist.get(1);
            List<String> floorlist = getMatcheNum(floorStr);
            String floor = floorlist.get(0);
            String totalFloor = floorlist.get(1);
            
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
            Elements curEles = roomMatesEle.select("li.cur-rm");
            String chaoxiang = curEles.select("li").get(3).text();

            Elements addrEle = doc.select("span.roomInfo-mark");
            String address = addrEle.get(0).text(); //翰盛家园（上海市浦东新区创新西路195号）

            //小区名称
            int index = StringUtils.indexOf(address,"（");
            String communityName = null;
            if(index != -1){
                communityName = StringUtils.substring(address,0,index);
            }

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
