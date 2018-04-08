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
            urls.add("http://www.mogoroom.com/room/477627.shtml");
            urls.add("http://www.mogoroom.com/room/657403.shtml");
            urls.add("http://www.mogoroom.com/room/2475583.shtml");
            urls.add("http://www.mogoroom.com/room/2468677.shtml");
            urls.add("http://www.mogoroom.com/room/61675.shtml");
            urls.add("http://www.mogoroom.com/room/4368112.shtml");
            urls.add("http://www.mogoroom.com/room/613443.shtml");
            urls.add("http://www.mogoroom.com/room/1770568.shtml");
            urls.add("http://www.mogoroom.com/room/328802.shtml");

            spiderService.batchParseMogoroomHouse(urls);
            return Response.successResponse();
        }catch (Exception e) {
            logger.error("batchMogoRoom EXCEPTION --- --->{}", e);
            return Response.fail("添加房屋信息失败！");
        }
    }
}
