package com.apass.esp.schedule;

import com.alibaba.fastjson.JSONObject;
import com.apass.esp.domain.dto.goods.GoodsInfoInOrderDto;
import com.apass.esp.domain.dto.order.OrderDetailInfoDto;
import com.apass.esp.domain.entity.goods.GoodsStockInfoEntity;
import com.apass.esp.domain.entity.order.OrderInfoEntity;
import com.apass.esp.search.entity.Goods;
import com.apass.esp.service.goods.GoodsService;
import com.apass.esp.service.goods.GoodsStockInfoService;
import com.apass.esp.service.order.OrderService;
import com.apass.esp.utils.ExportDomain;
import com.apass.esp.utils.ExportDomain1;
import com.apass.esp.utils.mailUtils.MailSenderInfo;
import com.apass.esp.utils.mailUtils.MailUtil;
import com.apass.esp.web.commons.JsonDateValueProcessor;
import com.apass.gfb.framework.exception.BusinessException;
import com.apass.gfb.framework.utils.DateFormatUtil;
import net.sf.json.JsonConfig;
import org.apache.commons.collections.CollectionUtils;
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
import org.springframework.web.bind.annotation.RequestMapping;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * type: class
 *
 * @author xianzhi.wang
 * @date 2017/9/5
 * @see
 * @since JDK 1.8
 */
@Component
@Configurable
@EnableScheduling
@Profile("Schedule")
public class MailStatis2ScheduleTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailStatis2ScheduleTask.class);

    @Value("${monitor.receive.emails}")
    public String receiveEmails;

    @Value("${monitor.send.address}")
    public String sendAddress;

    @Value("${monitor.send.password}")
    public String sendPassword;

    @Value("${monitor.env}")
    public String env;


    @Autowired
    private OrderService orderService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsStockInfoService goodsStockInfoService;


    @Scheduled(cron = "0 0 8 * * ?")
    public void mailStatisSchedule() {
        String currentDate = DateFormatUtil.getCurrentTime("YYYY-MM-dd");//当天
        Date date = DateFormatUtil.addDays(new Date(), -1);//前一天
        String dateBefore = DateFormatUtil.dateToString(date, "YYYY-MM-dd");
        String dateBeforeDate = dateBefore.substring(0, 8);
        //当天是1号  发上个月的
        String beginDate = dateBeforeDate + "01";

        //订单list
        List<OrderInfoEntity> orderInfoEntityListD02 =  orderService.selectOrderByStatus("D02", beginDate, currentDate);
        List<OrderInfoEntity> orderInfoEntityListD03 =  orderService.selectOrderByStatus("D03", beginDate, currentDate);
        List<OrderInfoEntity> orderInfoEntityListD04 =  orderService.selectOrderByStatus("D04", beginDate, currentDate);
        List<OrderInfoEntity> orderInfoEntityListD05 =  orderService.selectOrderByStatus("D05", beginDate, currentDate);
        List<OrderInfoEntity> orderInfoEntityListD09 =  orderService.selectOrderByStatus("D09", beginDate, currentDate);
        List<OrderInfoEntity> orderInfoEntityListD10 =  orderService.selectOrderByStatus("D10", beginDate, currentDate);
        BigDecimal amt = new BigDecimal(0);
        BigDecimal totalPrice = new BigDecimal(0);
        BigDecimal xieYiPrice = new BigDecimal(0);

        HashMap<String,BigDecimal> mapD02  =  convert(orderInfoEntityListD02);
        HashMap<String,BigDecimal> mapD03  =  convert(orderInfoEntityListD03);
        HashMap<String,BigDecimal> mapD04  =  convert(orderInfoEntityListD04);
        HashMap<String,BigDecimal> mapD05  =  convert(orderInfoEntityListD05);
        HashMap<String,BigDecimal> mapD09  =  convert(orderInfoEntityListD09);
        HashMap<String,BigDecimal> mapD10  =  convert(orderInfoEntityListD10);

        List<ExportDomain1> list = new ArrayList<>();
        ExportDomain1 exportDomain1 = new ExportDomain1();
        exportDomain1.setType("成交金额");
        exportDomain1.setD02(String.valueOf(mapD02.get("totalPrice")));
        exportDomain1.setD03(String.valueOf(mapD03.get("totalPrice")));
        exportDomain1.setD04(String.valueOf(mapD04.get("totalPrice")));
        exportDomain1.setD05(String.valueOf(mapD05.get("totalPrice")));
        exportDomain1.setD09(String.valueOf(mapD09.get("totalPrice")));
        exportDomain1.setD10(String.valueOf(mapD10.get("totalPrice")));
        ExportDomain1 exportDomain2 = new ExportDomain1();
        exportDomain2.setType("采购金额");
        exportDomain2.setD02(String.valueOf(mapD02.get("xieYiPrice")));
        exportDomain2.setD03(String.valueOf(mapD03.get("xieYiPrice")));
        exportDomain2.setD04(String.valueOf(mapD04.get("xieYiPrice")));
        exportDomain2.setD05(String.valueOf(mapD05.get("xieYiPrice")));
        exportDomain2.setD09(String.valueOf(mapD09.get("xieYiPrice")));
        exportDomain2.setD10(String.valueOf(mapD10.get("xieYiPrice")));
        list.add(exportDomain1);
        list.add(exportDomain2);
        try {
            generateFile(list);
        } catch (IOException e) {
            LOGGER.error("mailStatisSchedule generateFile error .... ", e);
        }
        MailSenderInfo mailSenderInfo = new MailSenderInfo();
        mailSenderInfo.setMailServerHost("SMTP.263.net");
        mailSenderInfo.setMailServerPort("25");
        mailSenderInfo.setValidate(true);
        mailSenderInfo.setUserName(sendAddress);
        mailSenderInfo.setPassword(sendPassword);// 您的邮箱密码
        mailSenderInfo.setFromAddress(sendAddress);
        mailSenderInfo.setSubject("安家趣花电商订单统计(成交金额，统计成本)【" + beginDate + " ~ " + dateBefore + "】");
        mailSenderInfo.setContent("请查收最新统计报表..");
        mailSenderInfo.setToAddress("xujie@apass.cn");
        if ("prod".equals(env)) {
            mailSenderInfo.setToAddress("huangbeifang@apass.cn,xujie@apass.cn");
            mailSenderInfo.setCcAddress("maoyanping@apass.cn,yangxiaoqing@apass.cn");
        }

        Multipart msgPart = new MimeMultipart();
        MimeBodyPart body = new MimeBodyPart(); //正文
        MimeBodyPart attach = new MimeBodyPart(); //附件
        try {
            attach.setDataHandler(new DataHandler(new FileDataSource("/reportings1.xlsx")));
            attach.setFileName("reportings1.xls");
            msgPart.addBodyPart(attach);
            body.setContent(mailSenderInfo.getContent(), "text/html; charset=utf-8");
            msgPart.addBodyPart(body);
        } catch (MessagingException e) {
            LOGGER.error("mailStatisSchedule msgPart   body error.... ", e);
        }
        mailSenderInfo.setMultipart(msgPart);
        MailUtil mailUtil = new MailUtil();
        mailUtil.sendTextMail(mailSenderInfo);

    }

    private HashMap<String,BigDecimal> convert(List<OrderInfoEntity> orderInfoEntityList){
        BigDecimal totalPrice = new BigDecimal(0);
        BigDecimal xieYiPrice = new BigDecimal(0);
        HashMap<String,BigDecimal> map = new HashMap<>();
        for (OrderInfoEntity orderInfoEntity : orderInfoEntityList
                ) {
            OrderDetailInfoDto orderDetailInfoDto = null;
            try {
                orderDetailInfoDto = orderService.getOrderDetailInfoDto("", orderInfoEntity.getOrderId());
            } catch (BusinessException e) {
                LOGGER.error("orderId {} dto null ....", orderInfoEntity.getOrderId());
            }
            if (orderDetailInfoDto == null) {
                continue;
            }
            //订单明细list
            List<GoodsInfoInOrderDto> goodsInfoInOrderDtoList = orderDetailInfoDto.getOrderDetailInfoList();
            if (CollectionUtils.isEmpty(goodsInfoInOrderDtoList)) {
                LOGGER.info("goodsInfoInOrderDtoList is empty ,orderId {}", orderInfoEntity.getOrderId());
                continue;
            }


            for (GoodsInfoInOrderDto goodsInfoInOrderDto :
                    goodsInfoInOrderDtoList) {
                List<GoodsStockInfoEntity> goodsStockInfoEntityList = goodsService.loadDetailInfoByGoodsId(goodsInfoInOrderDto.getGoodsId());
                if (CollectionUtils.isEmpty(goodsStockInfoEntityList)) {
                    continue;
                }
                GoodsStockInfoEntity  goodsStockInfoEntity = goodsStockInfoService.getById(goodsInfoInOrderDto.getGoodsStockId());
                //协议价
                xieYiPrice = xieYiPrice.add(goodsStockInfoEntity.getGoodsCostPrice().multiply(new BigDecimal(goodsInfoInOrderDto.getBuyNum())));
                //成交价
                totalPrice = totalPrice.add(goodsInfoInOrderDto.getGoodsPrice().multiply(new BigDecimal(goodsInfoInOrderDto.getBuyNum())));
//                // 暂时不需要判断是否存在使用优惠券的情况
//                if(goodsInfoInOrderDto.getOrderDetailCouponDisCountAmt() != null){
//                    totalPrice = totalPrice.subtract(goodsInfoInOrderDto.getOrderDetailCouponDisCountAmt());
//                }
//                //判断是否是参加优惠活动的情况
//                if(goodsInfoInOrderDto.getOrderDetailDisCountAmt() != null){
//                    totalPrice = totalPrice.subtract(goodsInfoInOrderDto.getOrderDetailDisCountAmt());
//                }
            }
        }
        map.put("xieYiPrice",xieYiPrice);
        map.put("totalPrice",totalPrice);
        return map;
    }

    private void generateFile(List list) throws IOException {
        // 第一步：声明一个工作薄
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步：声明一个单子并命名
        HSSFSheet sheet = wb.createSheet("sheet");

        // 获取标题样式，内容样式
        List<HSSFCellStyle> hssfCellStyle = getHSSFCellStyle(wb);
        HSSFRow createRow = sheet.createRow(0);
        String[] rowHeadArr = {"统计维度", "待发货", "待收货","交易完成","售后服务中","退款处理中","交易关闭"};
        String[] headKeyArr = {"type", "d02", "d03", "d04", "d05", "d09", "d10"};
        for (int i = 0; i < rowHeadArr.length; i++) {
            HSSFCell cell = createRow.createCell(i);
            sheet.autoSizeColumn(i, true);
            cell.setCellStyle(hssfCellStyle.get(0));
            cell.setCellValue(rowHeadArr[i]);
        }
        for (int i = 0; i < list.size(); i++) {
            HSSFRow createRowContent = sheet.createRow(i + 1);
            Object object = list.get(i);
            // json日期转换配置类
            JsonConfig jsonConfig = new JsonConfig();
            jsonConfig.registerJsonValueProcessor(java.util.Date.class, new JsonDateValueProcessor());
            net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(object, jsonConfig);
            for (int j = 0; j < rowHeadArr.length; j++) {
                HSSFCell cellContent = createRowContent.createCell(j);
                cellContent.setCellStyle(hssfCellStyle.get(1));
                if (i == 1) {
                    sheet.autoSizeColumn(j, true);
                }
                cellContent.setCellValue(jsonObject.get(headKeyArr[j]) + "");
            }
        }
        FileOutputStream fileOutputStream = new FileOutputStream("/reportings1.xlsx");
        wb.write(fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
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
}
