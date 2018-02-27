package com.apass.zufang.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 * @description Logger Utils
 *
 * @author lixining
 * @version $Id: LoggerUtils.java, v 0.1 2016年7月25日 上午9:40:48 lixining Exp $
 */
public class GfbLogUtils {
    /**
     * 业务行为日志
     */
    private static final Logger LOG = LoggerFactory.getLogger("com.apass.zufang.bizlog");
    
    private GfbLogUtils() {

    }

    public static void info(String message) {
        LOG.info(message);
    }
    
    public static void info(String message, Object... args) {
        LOG.info(message, args);
    }
}
