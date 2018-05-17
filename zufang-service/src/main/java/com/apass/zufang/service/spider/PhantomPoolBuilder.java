package com.apass.zufang.service.spider;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by DELL on 2018/5/17.
 */
public class PhantomPoolBuilder {

    private static  final Logger log = LoggerFactory.getLogger(PhantomPoolBuilder.class);

    /**
     * @return
     */
    @Deprecated
    public WebDriver buildSingleDriver(String proxyIp) {
        String osname = System.getProperties().getProperty("os.name");
        if(osname.equals("Linux")){
            System.setProperty("phantomjs.binary.path", "/usr/local/phantomjs/bin/phantomjs");
        }else{
            System.setProperty("phantomjs.binary.path", "D:\\\\phantomjs-windows\\\\bin\\\\phantomjs.exe");//设置PhantomJs访问路径
        }
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setJavascriptEnabled(true);
        capabilities.setCapability("phantomjs.page.customHeaders.User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36");
        capabilities.setCapability("phantomjs.page.customHeaders.Referer","http://www.mogoroom.com/list");
        capabilities.setCapability("phantomjs.page.customHeaders.Host","www.mogoroom.com");
        capabilities.setCapability("phantomjs.page.customHeaders.Proxy-Connection","keep-alive");
        capabilities.setCapability("phantomjs.page.customHeaders.Cache-Control","max-age=0");
        capabilities.setCapability("phantomjs.page.customHeaders.Accept-Language","zh-CN,zh;q=0.9");


        org.openqa.selenium.Proxy proxy = new org.openqa.selenium.Proxy();
        proxy.setProxyType(Proxy.ProxyType.MANUAL);
        proxy.setAutodetect(false);
        proxy.setHttpProxy(proxyIp);

        capabilities.setCapability(CapabilityType.PROXY, proxy);

        return new PhantomJSDriver(capabilities);
    }

    public static String getHtmlByPhantomJs(String htmlUrl,String host,String referer,String proxyIp) throws Exception{

        String osname = System.getProperties().getProperty("os.name");
        String jsPath = PhantomPoolBuilder.class.getResource("/").getPath() + "spider/" + "spider.js";
        String phantomJs = "";
        if(osname.equalsIgnoreCase("linux")){
            phantomJs = "/usr/local/phantomjs/bin/phantomjs";
        }else{
            phantomJs = "D:\\phantomjs-windows\\bin\\phantomjs.exe";
            jsPath = jsPath.substring(1);//去掉第一个"/"
        }
        String execStr = null;

        if(StringUtils.isNotEmpty(proxyIp)){
            execStr = phantomJs + " --proxy="+proxyIp +" "+ jsPath + " " + htmlUrl
                    + " " +host + " "+referer;
        }else{
            execStr = phantomJs  +" "+ jsPath + " " + htmlUrl
                    + " " +host + " "+referer;
        }

        Runtime rt = Runtime.getRuntime();
        InputStream is = null;
        BufferedReader br = null;
        try {
            Process p = rt.exec(execStr);
             is = p.getInputStream();
             br = new BufferedReader(new InputStreamReader(is));
            StringBuffer sbf = new StringBuffer();
            String tmp = "";
            while((tmp=br.readLine())!=null) {
                sbf.append(tmp + "\n");
            }
            String result = sbf.toString();
            if(result.contains("Unable to request url")){
                log.error(result);
                result = null;
            }
            String start = "<html>";
            String end = "</html>";
            result = result.substring(result.indexOf(start) + start.length(),
                    result.indexOf(end) + end.length());

            return result;
        }catch (Exception e){
            log.error("--------getHtmlByPhantomJs error :",e);
            return null;
        }finally {
            br.close();
            is.close();
        }


    }

    public static void main(String[] args) throws Exception{
        String proxyIp="";
        String s = getHtmlByPhantomJs("http://www.mogoroom.com/room/758305.shtml?page=list","www.mogoroom.com","http://www.mogoroom.com/list",proxyIp);
        System.out.println(s);
    }
}