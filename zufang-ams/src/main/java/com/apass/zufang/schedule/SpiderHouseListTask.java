package com.apass.zufang.schedule;
import com.apass.zufang.domain.entity.ZfangSpiderHouseEntity;
import com.apass.zufang.service.spider.HouseSpiderService;
import com.apass.zufang.service.spider.ProxyIpHandler;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
/**
 * @author xiaohai
 * @date 2018/4/19.
 */
@Component
@Configurable
@EnableScheduling
@Controller
@RequestMapping("/noauth/spider")
public class SpiderHouseListTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpiderHouseListTask.class);
    //蘑菇房源列表页根路径
    private static final String[] BASE_URLLIST = {"http://www.mogoroom.com/list",
        "http://bj.mogoroom.com/list", "http://sz.mogoroom.com/list", "http://hz.mogoroom.com/list",
        "http://nj.mogoroom.com/list", "http://cd.mogoroom.com/list", "http://cq.mogoroom.com/list",
        "http://xa.mogoroom.com/list", "http://gz.mogoroom.com/list", "http://tj.mogoroom.com/list"
    };
    //嗨住租房列表页根路径
    private static final String[] BASE_HZ_URLLIST = {
        "http://www.hizhu.com/shanghai/shangquan.html",
        "http://www.hizhu.com/beijing/shangquan.html",
        "http://www.hizhu.com/hangzhou/shangquan.html",
        "http://www.hizhu.com/nanjing/shangquan.html",
        "http://www.hizhu.com/shenzhen/shangquan.html",
        "http://www.hizhu.com/guangzhou/shangquan.html",
        "http://www.hizhu.com/zhengzhou/shangquan.html",
        "http://www.hizhu.com/suzhou/shangquan.html",
        "http://www.hizhu.com/wuhan/shangquan.html",
        "http://www.hizhu.com/tianjin/shangquan.html"
    };
    //要爬的页面
    private static final Integer PAGENUM = 50;
    @Autowired
    private HouseSpiderService houseSpiderService;
    @Autowired
    private ProxyIpHandler proxyIpHandler;
    /**
     * 每天00:10分跑此job
     * 思路：1，根据url和page去查对应页数数据，放入数据库存
     * 2，放之前判断是否已经存在，根据
     */
    @Scheduled(cron = "0 10 0 * * *")
    public void initExtHouseList(){
        try{
            for(String listUrl : BASE_URLLIST){
                for (int i=0; i<PAGENUM; i++){
                    houseSpiderService.spiderMogoroomPageList(listUrl,i);
                }

            }
        }catch (Exception e){
            LOGGER.error("获取数据失败！------Exception=====>{}",e);
        }
    }
    @RequestMapping("/houseList2")
    public void initExtHouseList2(){
        try{
            for(String listUrl : BASE_URLLIST){
                for (int i=0; i<PAGENUM; i++){
                    houseSpiderService.spiderMogoroomPageList(listUrl,i);
                }
            }
        }catch (Exception e){
            LOGGER.error("获取数据失败！------Exception=====>{}",e);
        }
    }
    /**
     * 初始化何蘑菇租房房源详情表
     * 思路：1，从t_zfang_spider_house表中获取所有未被删除的url，
     * 2，拼接BASE_URLDETAIL爬取相关数据，插入t_zfang_house表中
     * 3，插入成功后，删除t_zfang_spider_house表中对应数据
     */
    @Scheduled(cron = "0 0 0/12 * * ?")
    public void initExtHouseDetail(){
        try{
            //去查询spider表，获取其中中的url放入urls中
            List<ZfangSpiderHouseEntity> list = houseSpiderService.listAllExtHouse();
            if(CollectionUtils.isNotEmpty(list)){
                for(ZfangSpiderHouseEntity entity : list){
                    String linkUrl = entity.getHost() + entity.getUrl();
                    houseSpiderService.parseMogoroomHouseDetail(linkUrl,entity.getHost());
                }

            }

        }catch (Exception e){
            LOGGER.error("获取数据失败！------Exception=====>{}",e);
        }
    }
    /**
     * 刷新缓存  put代理IP集合INTORedis 
     */
    @Scheduled(cron = "0 0/20 * * * ?")
    public void initProxyIpList(){
    	try {
			proxyIpHandler.putIntoRedis();
		} catch (Exception e) {
			LOGGER.error("initProxyIpList FAIL Exception ==>{}",e);
		}
    }
    @RequestMapping("/initExtHouseDetail2")
    public void initExtHouseDetail2(){
        try{
            //去查询spider表，获取其中中的url放入urls中
            List<ZfangSpiderHouseEntity> list = houseSpiderService.listAllExtHouse();
            if(CollectionUtils.isNotEmpty(list)){
                for(ZfangSpiderHouseEntity entity : list){
                    String linkUrl = entity.getHost() + entity.getUrl();
                    houseSpiderService.parseMogoroomHouseDetail(linkUrl,entity.getHost());
                }

            }
        }catch (Exception e){
            LOGGER.error("获取数据失败------Exception=====>{}",e);
        }
    }


    @RequestMapping("/initHZList")
    public void initHZList(){
        try{
            for(String listUrl : BASE_HZ_URLLIST){
                for (int i=0; i<PAGENUM; i++){
                    houseSpiderService.spiderHiZhuPageList(listUrl,i);
                }
            }
        }catch (Exception e){
            LOGGER.error("获取数据失败！------Exception=====>{}",e);
        }
    }


    @RequestMapping("/initHZExtHouseDetail")
    public void initHZExtHouseDetail(){
        try{
            //去查询spider表，获取其中中的url放入urls中
            List<ZfangSpiderHouseEntity> list = houseSpiderService.listAllHZExtHouse();
            if(CollectionUtils.isNotEmpty(list)){
                for(ZfangSpiderHouseEntity entity : list){
                    houseSpiderService.parseHiZhuroomHouseDetail(entity.getUrl(),entity.getHost());
                }

            }
        }catch (Exception e){
            LOGGER.error("获取数据失败------Exception=====>{}",e);
        }
    }

}