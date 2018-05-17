package com.apass.zufang.web;

import com.apass.zufang.domain.Response;
import com.apass.zufang.service.spider.HouseSpiderService;
import com.apass.zufang.web.house.HouseControler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 2018/4/8.
 */
@Controller
@RequestMapping("/noauth/test")
public class TestZFAmsController {
    private static final Logger logger = LoggerFactory.getLogger(HouseControler.class);
    @Autowired
    private HouseSpiderService spiderService;

    @RequestMapping(value = "/batchMogoRoom", method = RequestMethod.GET)
    public Response batchMogoRoom(){
        try {
            //http://www.mogoroom.com/list
            List<String> urls = new ArrayList<>();
            urls.add("http://www.mogoroom.com/room/2538094.shtml");
            urls.add("http://www.mogoroom.com/room/1198344.shtml");
            urls.add("http://www.mogoroom.com/room/286005.shtml");
            urls.add("http://www.mogoroom.com/room/1648015.shtml");
            urls.add("http://www.mogoroom.com/room/3223045.shtml");
            urls.add("http://www.mogoroom.com/room/464375.shtml");
            urls.add("http://www.mogoroom.com/room/1347488.shtml");
            spiderService.batchParseMogoroomHouse(urls,"http://www.mogoroom.com");
            return Response.successResponse();
        }catch (Exception e) {
            logger.error("batchMogoRoom EXCEPTION --- --->{}", e);
            return Response.fail("添加房屋信息失败！");
        }
    }
    @RequestMapping(value = "/batchMogoRoomList", method = RequestMethod.GET)
    public Response batchMogoRoomList(){
        try {
            spiderService.spiderMogoroomPageList("http://www.mogoroom.com/list",1);
            return Response.successResponse();
        }catch (Exception e) {
            logger.error("batchMogoRoom EXCEPTION --- --->{}", e);
            return Response.fail("添加房屋信息失败！");
        }
    }
}
