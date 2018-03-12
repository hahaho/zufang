package com.apass.zufang.web.schedule;

import com.apass.zufang.domain.entity.House;
import com.apass.zufang.search.entity.HouseEs;
import com.apass.zufang.search.enums.IndexType;
import com.apass.zufang.search.manager.IndexManager;
import com.apass.zufang.service.house.HouseService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * type: class
 * es初始化
 *
 * @author xianzhi.wang
 * @date 2017/8/21
 * @see
 * @since JDK 1.8
 */
//@Component
//@Configurable
//@EnableScheduling
//@Profile("Schedule")
@Controller
@RequestMapping("/es/zufang")
public class EsInitScheduleTask {

    private static final Logger logger = LoggerFactory.getLogger(EsInitScheduleTask.class);

    @Autowired
    private HouseService houseService;

    /**
     * 0 0 12 ? * WED 表示每个星期三中午12点
     */
//    @Scheduled(cron = "0 0 12 ? * WED")
    @RequestMapping("/init")
    public void esInitScheduleTask() {
        int index = 1;
        final int BACH_SIZE = 500;
        while (true) {
            List<House> houses = houseService.selectUpGoods(index, BACH_SIZE);
            if (CollectionUtils.isEmpty(houses)) {
                break;
            }
            List<HouseEs> list = houseService.getHouseEsList(houses);
            index += houses.size();
            IndexManager.createIndex(list, IndexType.HOUSE);
        }
    }
}
