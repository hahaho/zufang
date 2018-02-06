package com.apass.esp.schedule;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.apass.esp.service.order.OrderService;
import com.apass.esp.service.talkingdata.TalkDataService;
import com.apass.esp.utils.ExportDomainFor;
import com.apass.esp.utils.ExportDomainFor4;
import com.apass.esp.utils.mailUtils.MailSenderInfo;
import com.apass.esp.utils.mailUtils.MailUtil;
import com.apass.gfb.framework.environment.SystemEnvConfig;
import com.apass.gfb.framework.utils.DateFormatUtil;
import com.google.common.collect.Lists;
import org.apache.poi.hssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Configurable
@EnableScheduling
@Profile("Schedule")
public class TalkingDataScheduleTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(TalkingDataScheduleTask.class);

    /**
     * 数据维度，即数据分组方式
     */
    public static String groupby = "daily";
    
    
    public static String metrics = "activeuser";//查询活跃用户数
    public static String newuser = "newuser";//新增用户数
    public static String session = "session";//启动次数
    public static String avgsessionlength = "avgsessionlength";//平均每次启动使用时长
    public static String day1retention = "day1retention";//新增用户次日留存率
    public static String dauday1retention = "dauday1retention";//活跃用户次日留存率
    public static String totaluser = "totaluser";
    
    @Autowired
    private SystemEnvConfig systemEnvConfig;
    
    @Autowired
    private TalkDataService talkingDataService;

    @Autowired
    private OrderService orderService;

    @Value("${monitor.send.address}")
    public String sendAddress;

    @Value("${monitor.send.password}")
    public String sendPassword;

    @Value("${orderflow.daily.sendto}")
    public String sendToAddressFlow;

    @Value("${orderflow.daily.copyto}")
    public String copyToAddressFlow;
    
    @Value("${orderstatistics.daily.sendto}")
    public String sendToAddressStatis;

    @Value("${orderstatistics.daily.copyto}")
    public String copyToAddressStatis;

    /**
     * 电商流量日报
     */
    @Scheduled(cron = "0 0 7 * * *")
    public void schedule() {
    	if (!systemEnvConfig.isPROD()) {
            return;
        }
        List<ExportDomainFor> lists = Lists.newArrayList();
        String[] terminalTypes = {"ios","android"};  /**终端类型*/
        //查询时间段
        Date endDate = DateFormatUtil.addDays(new Date(), -1);
        Date beginDate = DateFormatUtil.addDays(endDate, -14);
        for (String type : terminalTypes) {
            try {
                // 去talkingDate中获取UV(查询活跃用户数)
                String talkingData1metrics = talkingDataService.getTalkingData1(beginDate, endDate, metrics, groupby, type);
                String talkingData1newuser = talkingDataService.getTalkingData1(beginDate, endDate, newuser, groupby, type);
                String talkingData1session = talkingDataService.getTalkingData1(beginDate, endDate, session, groupby, type);
                String talkingData1avgsessionlength = talkingDataService.getTalkingData1(beginDate, endDate, avgsessionlength, groupby, type);
                String talkingData1day1retention = talkingDataService.getTalkingData1(beginDate, endDate, day1retention, groupby, type);
                String talkingData1dauday1retention = talkingDataService.getTalkingData1(beginDate, endDate, dauday1retention, groupby, type);
                for(int i = 0;i < 15 ; i++ ){
                	JSONObject iosObj = (JSONObject) JSONArray.parseArray(
                            JSONObject.parseObject(talkingData1metrics).getString("result")).get(i);
                    JSONObject iosObj2 = (JSONObject) JSONArray.parseArray(
                            JSONObject.parseObject(talkingData1newuser).getString("result")).get(i);
                    JSONObject iosObj3 = (JSONObject) JSONArray.parseArray(
                            JSONObject.parseObject(talkingData1session).getString("result")).get(i);
                    JSONObject iosObj4 = (JSONObject) JSONArray.parseArray(
                            JSONObject.parseObject(talkingData1avgsessionlength).getString("result")).get(i);
                    JSONObject iosObj5 = (JSONObject) JSONArray.parseArray(
                            JSONObject.parseObject(talkingData1day1retention).getString("result")).get(i);
                    JSONObject iosObj6 = (JSONObject) JSONArray.parseArray(
                            JSONObject.parseObject(talkingData1dauday1retention).getString("result")).get(i);
                    Integer iuv = Integer.valueOf(iosObj.getString(metrics));
                    Integer newuser1 = Integer.valueOf(iosObj2.getString(newuser));
                    Integer session1 = Integer.valueOf(iosObj3.getString(session));
                    String avgsessionlength1 = iosObj4.getString(avgsessionlength);
                    String day1retention1 = iosObj5.getString(day1retention);
                    String dauday1retention1 = iosObj6.getString(dauday1retention);
                    LOGGER.info(iosObj.getString(groupby) + "号 metrics：" + iuv);

                    ExportDomainFor exportDomainFor = new ExportDomainFor();
                    exportDomainFor.setDate(iosObj.getString(groupby));
                    exportDomainFor.setActiveUser(iuv);
                    exportDomainFor.setNewUser(newuser1);
                    exportDomainFor.setQidongTime(session1);
                    exportDomainFor.setUserTime(converInteger(avgsessionlength1));
                    exportDomainFor.setType(type);
                    exportDomainFor.setDay1retention1(convert(day1retention1));
                    exportDomainFor.setDauday1retention1(convert(dauday1retention1));
                    lists.add(exportDomainFor);
                }
            } catch (Exception e) {
                LOGGER.error("error i {}", e);
            }
        }
        // 导出
        try {
            generateFile(lists);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MailSenderInfo mailSenderInfo = new MailSenderInfo();
        mailSenderInfo.setMailServerHost("SMTP.263.net");
        mailSenderInfo.setMailServerPort("25");
        mailSenderInfo.setValidate(true);
        mailSenderInfo.setUserName(sendAddress);
        mailSenderInfo.setPassword(sendPassword);// 您的邮箱密码
        mailSenderInfo.setFromAddress(sendAddress);
        mailSenderInfo.setSubject("电商流量日报");
        mailSenderInfo.setContent("请查收电商流量日报..");
        mailSenderInfo.setToAddress(sendToAddressFlow);
        mailSenderInfo.setCcAddress(copyToAddressFlow);


        Multipart msgPart = new MimeMultipart();
        MimeBodyPart body = new MimeBodyPart(); //正文
        MimeBodyPart attach = new MimeBodyPart(); //附件
        try {
            attach.setDataHandler(new DataHandler(new FileDataSource("/percent.xls")));
            attach.setFileName(MimeUtility.encodeText("percent.xls"));
            msgPart.addBodyPart(attach);
            body.setContent(mailSenderInfo.getContent(), "text/html; charset=utf-8");
            msgPart.addBodyPart(body);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        mailSenderInfo.setMultipart(msgPart);
        MailUtil mailUtil = new MailUtil();
        mailUtil.sendTextMail(mailSenderInfo);

    }


    /**
     * 电商交易明细统计日报
     * @throws InterruptedException
     */
    @Scheduled(cron = "0 0 8 * * *")
    public void schedule2() throws InterruptedException {
    	if (!systemEnvConfig.isPROD()) {
            return;
        }
    	Date endDate = DateFormatUtil.addDays(new Date(), -1);
        Date beginDate = DateFormatUtil.addDays(endDate, -14);
        List<ExportDomainFor4> lists = Lists.newArrayList();
        
        String talkingData1metrics = talkingDataService.getTalkingData(beginDate, endDate, metrics, groupby);
        TimeUnit.SECONDS.sleep(11);
        String talkingData1newuser = talkingDataService.getTalkingData(beginDate, endDate, newuser, groupby);
        TimeUnit.SECONDS.sleep(11);
        
        String talkingData1totaluser = talkingDataService.getTalkingData(beginDate, endDate, totaluser, groupby);
        
        for(int i = 0 ;i < 15; i++ ){
        	JSONObject iosObj = (JSONObject) JSONArray.parseArray(
                    JSONObject.parseObject(talkingData1metrics).getString("result")).get(i);
            JSONObject iosObj2 = (JSONObject) JSONArray.parseArray(
                    JSONObject.parseObject(talkingData1newuser).getString("result")).get(i);
            JSONObject iosObj3 = (JSONObject) JSONArray.parseArray(
                    JSONObject.parseObject(talkingData1totaluser).getString("result")).get(i);
            Integer iuv = Integer.valueOf(iosObj.getString(metrics));
            Integer newuser1 = Integer.valueOf(iosObj2.getString(newuser));
            Integer session1 = Integer.valueOf(iosObj3.getString(totaluser));

            Date startTime = DateFormatUtil.string2date(iosObj.getString(groupby));
            Date endTime = DateFormatUtil.addDays(startTime, 1);
            
            Integer confirmCount = orderService.getConfirmOrderCount(startTime, endTime);// 下单买家数
            Integer confirmPayCount = orderService.getConfirmPayCount(startTime, endTime);// 支付成功买家数
            BigDecimal orderAmtAll = orderService.getSumOrderamt(startTime, endTime);// 下单金额
            BigDecimal orderAmtForPaySuccess = orderService.getSumOrderamtForPaySuccess(startTime,
            		endTime);// 支付成功金额
            ExportDomainFor4 exportDomainFor4 = new ExportDomainFor4();
            exportDomainFor4.setDate(iosObj.getString(groupby));
            exportDomainFor4.setIuv(iuv);
            exportDomainFor4.setConfirmCount(confirmCount);
            exportDomainFor4.setConfirmPayCount(confirmPayCount);
            exportDomainFor4.setNewuser1(newuser1);
            exportDomainFor4.setOrderAmtAll(orderAmtAll);
            exportDomainFor4.setOrderAmtForPaySuccess(orderAmtForPaySuccess);
            exportDomainFor4.setSession1(session1);
            lists.add(exportDomainFor4);
        }
        
        try {
            generateFile1(lists);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MailSenderInfo mailSenderInfo = new MailSenderInfo();
        mailSenderInfo.setMailServerHost("SMTP.263.net");
        mailSenderInfo.setMailServerPort("25");
        mailSenderInfo.setValidate(true);
        mailSenderInfo.setUserName(sendAddress);
        mailSenderInfo.setPassword(sendPassword);// 您的邮箱密码
        mailSenderInfo.setFromAddress(sendAddress);
        mailSenderInfo.setSubject("电商交易明细统计日报");
        mailSenderInfo.setContent("请查收电商交易明细统计日报..");
        mailSenderInfo.setToAddress(sendToAddressStatis);
        mailSenderInfo.setCcAddress(copyToAddressStatis);


        Multipart msgPart = new MimeMultipart();
        MimeBodyPart body = new MimeBodyPart(); //正文
        MimeBodyPart attach = new MimeBodyPart(); //附件
        try {
            attach.setDataHandler(new DataHandler(new FileDataSource("/percentx.xls")));
            attach.setFileName(MimeUtility.encodeText("percentx.xls"));
            msgPart.addBodyPart(attach);
            body.setContent(mailSenderInfo.getContent(), "text/html; charset=utf-8");
            msgPart.addBodyPart(body);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        mailSenderInfo.setMultipart(msgPart);
        MailUtil mailUtil = new MailUtil();
        mailUtil.sendTextMail(mailSenderInfo);
    }


    private void generateFile1(List<ExportDomainFor4> dataList) throws IOException {
        // 第一步：声明一个工作薄
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步：声明一个单子并命名
        HSSFSheet sheet = wb.createSheet("sheet");
        // 获取标题样式，内容样式
        List<HSSFCellStyle> hssfCellStyle = getHSSFCellStyle(wb);
        String[] headArr = {"日期", "总用户数", "活跃用户数",
                "新增用户数", "下单买家数", "支付买家数","下单金额","支付金额"};
        String[] countKeyArr = {"date", "session1", "iuv", "newuser1",
                "confirmCount", "confirmPayCount","orderAmtAll","orderAmtForPaySuccess"};
        // 第三步：创建第一行（也可以称为表头）
        HSSFRow row = sheet.createRow(0);

        for (int i = 0; i < headArr.length; i++) {
            HSSFCell cell = row.createCell(i);
            sheet.autoSizeColumn(i, true);
            cell.setCellStyle(hssfCellStyle.get(0));
            cell.setCellValue(headArr[i]);
        }

        // 向单元格里填充数据
        for (int i = 0; i < dataList.size(); i++) {
            row = sheet.createRow(i + 1);
            net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(dataList.get(i));

            for (int j = 0; j < countKeyArr.length; j++) {
                HSSFCell cellContent = row.createCell(j);
                cellContent.setCellStyle(hssfCellStyle.get(1));
                if (i == 1) {
                    sheet.autoSizeColumn(j, true);
                }
                cellContent.setCellValue(jsonObject.get(countKeyArr[j]) + "");
            }

        }

        // 判断文件是否存在 ,没有创建文件
        FileOutputStream out = new FileOutputStream("/percentx.xls");
        wb.write(out);
        out.flush();
        out.close();
    }




    private void generateFile(List<ExportDomainFor> dataList) throws IOException {
        // 第一步：声明一个工作薄
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步：声明一个单子并命名
        HSSFSheet sheet = wb.createSheet("sheet");
        // 获取标题样式，内容样式
        List<HSSFCellStyle> hssfCellStyle = getHSSFCellStyle(wb);
        String[] headArr = {"日期", "终端", "活跃用户数","活跃用户次日留存率",
                "新增用户数", "新增用户次日留存率","启动次数", "平均使用时长"};
        String[] countKeyArr = {"date", "type", "activeUser","dauday1retention1", "newUser",
                "day1retention1","qidongTime", "userTime"};
        // 第三步：创建第一行（也可以称为表头）
        HSSFRow row = sheet.createRow(0);

        for (int i = 0; i < headArr.length; i++) {
            HSSFCell cell = row.createCell(i);
            sheet.autoSizeColumn(i, true);
            cell.setCellStyle(hssfCellStyle.get(0));
            cell.setCellValue(headArr[i]);
        }

        // 向单元格里填充数据
        for (int i = 0; i < dataList.size(); i++) {
            row = sheet.createRow(i + 1);
            net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(dataList.get(i));

            for (int j = 0; j < countKeyArr.length; j++) {
                HSSFCell cellContent = row.createCell(j);
                cellContent.setCellStyle(hssfCellStyle.get(1));
                if (i == 1) {
                    sheet.autoSizeColumn(j, true);
                }
                cellContent.setCellValue(jsonObject.get(countKeyArr[j]) + "");
            }

        }

        // 判断文件是否存在 ,没有创建文件
        FileOutputStream out = new FileOutputStream("/percent.xls");
        wb.write(out);
        out.flush();
        out.close();
    }

    private List<HSSFCellStyle> getHSSFCellStyle(HSSFWorkbook workbook) {
        List<HSSFCellStyle> styleList = new ArrayList<>();
        // 生成一个标题样式
        HSSFCellStyle headStyle = workbook.createCellStyle();
        headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中

        // 设置表头标题样式:宋体，大小11，粗体显示，
        HSSFFont headfont = workbook.createFont();
        headfont.setFontName("微软雅黑");
        headfont.setFontHeightInPoints((short) 11);// 字体大小
        headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 粗体显示

        /**
         * 边框
         */
        headStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
        headStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
        headStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
        headStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
        headStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
        headStyle.setFont(headfont);// 字体样式
        styleList.add(headStyle);

        // 生成一个内容样式
        HSSFCellStyle contentStyle = workbook.createCellStyle();
        contentStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
        contentStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
        /**
         * 边框
         */
        contentStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 下边框
        contentStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
        contentStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
        contentStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框

        HSSFFont contentFont = workbook.createFont();
        contentFont.setFontName("微软雅黑");
        contentFont.setFontHeightInPoints((short) 11);// 字体大小
        contentStyle.setFont(contentFont);// 字体样式
        styleList.add(contentStyle);

        return styleList;
    }

    private String convert(String number){
        double result1=Double.valueOf(number);
        DecimalFormat df = new DecimalFormat("0.00%");
        df.setRoundingMode(RoundingMode.CEILING );
        String r = df.format(result1);
        return r;
    }
    
    @SuppressWarnings("unused")
	private String converInteger(String number){
    	double result1=Double.valueOf(number);
        DecimalFormat df = new DecimalFormat("######0");
        return df.format(result1);
    }
}
