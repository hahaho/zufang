package com.apass.esp.schedule;

import com.apass.esp.common.utils.JsonUtil;
import com.apass.esp.domain.entity.MonitorEntity;
import com.apass.esp.domain.extentity.MonitorEntityStatistics;
import com.apass.esp.service.monitor.MonitorService;
import com.apass.esp.utils.mailUtils.MailSenderInfo;
import com.apass.esp.utils.mailUtils.MailUtil;
import com.apass.gfb.framework.cache.CacheManager;
import com.apass.gfb.framework.environment.SystemEnvConfig;
import com.apass.gfb.framework.utils.DateFormatUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by xianzhi.wang on 2017/5/18.
 */

@Component
@Configurable
@EnableScheduling
@Profile("Schedule")
public class MonitorScheduleTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorScheduleTask.class);

    @Autowired
    private MonitorService monitorService;

    @Autowired
    private CacheManager cacheManager;


    @Value("${monitor.receive.emails}")
    public String receiveEmails;

    @Value("${monitor.send.address}")
    public String sendAddress;

    @Value("${monitor.send.password}")
    public String sendPassword;

    @Value("${monitor.env}")
    public String env;

    @Autowired
    private SystemEnvConfig systemEnvConfig;

    @Scheduled(cron = "* 0/30 * * * *")
    public void monitorSchedule() {

        if(!systemEnvConfig.isPROD()){
            return;
        }
        String time = cacheManager.get("monitor_time");//间隔时间
        String times = cacheManager.get("monitor_times");//该时间的次数
        // time="10";
        //times="1";
        if (StringUtils.isAnyEmpty(time, times)) {
            time = "5";
            times = "5";
        }
        Date date = new Date();
        date = DateFormatUtil.addDMinutes(date, Integer.valueOf(time) * (-1));
        List<MonitorEntityStatistics> monitorEntityStatisticsList = monitorService.getMonitorEntitybyTime(date, env);
        if (CollectionUtils.isEmpty(monitorEntityStatisticsList)) {
            return;
        }
        LOGGER.info("date {}，time {},times{} ,monitorEntityStatisticsList {},monitorEntityStatisticsList.size {}", date, time, JsonUtil.toJsonString(monitorEntityStatisticsList), monitorEntityStatisticsList.size());
        int confTimes = Integer.valueOf(times) / 2 + 1;
        for (MonitorEntityStatistics monitorEntityStatistics : monitorEntityStatisticsList
                ) {

            if (confTimes > monitorEntityStatistics.getTotalMonitorNum()) {
                continue;
            }
            List<MonitorEntity> list = monitorService.getMonitorEntityByMethodName(date, monitorEntityStatistics.getMethodName(), env, monitorEntityStatistics.getApplication());
            LOGGER.info("date {}，time {},monitorEntityStatisticsList {},list {]", date, time, JsonUtil.toJsonString(monitorEntityStatisticsList), JsonUtil.toJsonString(list));

            if (CollectionUtils.isEmpty(list)) {
                continue;
            }
            MailSenderInfo mailSenderInfo = new MailSenderInfo();
            mailSenderInfo.setMailServerHost("SMTP.263.net");
            mailSenderInfo.setMailServerPort("25");
            mailSenderInfo.setValidate(true);
            mailSenderInfo.setUserName(sendAddress);
            mailSenderInfo.setPassword(sendPassword);// 您的邮箱密码
            mailSenderInfo.setFromAddress(sendAddress);
            mailSenderInfo.setSubject(env + "环境下 " + monitorEntityStatistics.getApplication() + "应用" + monitorEntityStatistics.getMethodDescrption() + "接口在" + time + "分钟内出错" + list.size() + "次");//邮箱标题


            StringBuffer sb = new StringBuffer(0);

            sb.append("详细信息如下:\n");
            for (int i = 0; i < list.size(); i++
                    ) {
                sb.append("信息: " + (i + 1) + "\n");
                sb.append("应用: " + list.get(i).getApplication() + " \n");
                sb.append("方法名称: " + list.get(i).getMethodName() + " \n");
                sb.append("调用时间 " + list.get(i).getInvokeDate() + "\n");
                sb.append("方法用时" + list.get(i).getTime() + "ms" + "\n");
                sb.append("堆栈信息 ：" + list.get(i).getMessage() + "\n");
                sb.append("errMessage ：" + list.get(i).getErrorMessage() + "\n");
                if (i != list.size() - 1) {
                    sb.append("-----------------------------\n");
                }
                if (i == 10) {
                    break;
                }
            }
            String[] emailAddress = receiveEmails.split(";");
            LOGGER.info("date{} ,mailSenderInfo{} ", date, sb.toString());
            mailSenderInfo.setContent(sb.toString());
            mailSenderInfo.setToAddress(emailAddress[0]);
            String[] emailAddress1;
            if(env.equals("prod")){
                 emailAddress1 = new String[4];
            }else{
                 emailAddress1 = new String[1];
            }
            for (int i = 1; i < emailAddress.length; i++) {
                emailAddress1[i - 1] = emailAddress[i];
            }
            if (emailAddress.length > 1) {
                mailSenderInfo.setCcAddress(getMailList(emailAddress1));
            } else {
                mailSenderInfo.setCcAddress(null);
            }
            MailUtil mailUtil = new MailUtil();
            mailUtil.sendTextMail(mailSenderInfo);
            for (int i = 0; i < list.size(); i++) {
                MonitorEntity monitorEntity = list.get(i);
                monitorEntity.setNotice(-1);
                monitorEntity.setInvokeDate(new Date());
                monitorService.updateMonitor(monitorEntity);
            }

        }
    }

    private String getMailList(String[] mailArray) {
        StringBuffer toList = new StringBuffer();
        int length = mailArray.length;
        if (mailArray != null && length < 2) {
            toList.append(mailArray[0]);
        } else {
            for (int i = 0; i < length; i++) {
                toList.append(mailArray[i]);
                if (i != (length - 1)) {
                    toList.append(",");
                }

            }
        }
        return toList.toString();
    }
}
