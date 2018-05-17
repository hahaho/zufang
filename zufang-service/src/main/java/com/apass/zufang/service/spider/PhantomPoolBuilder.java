package com.apass.zufang.service.spider;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.xerces.dom.PSVIAttrNSImpl;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.logging.Level;

/**
 * PhantomPool Builder
 *
 * @author lixining
 * @version $Id: PhantomFactoryBean.java, v 0.1 2016年8月5日 下午5:51:10 lixining Exp $
 */
public class PhantomPoolBuilder {
    /**
     * PhantomJS 路径
     */
    private String phantomJsLocation;
    /**
     * 最小初始化实例个数
     */
    private Integer minInstances = 1;
    /**
     * 最大初始化实例个数
     */
    private Integer maxInstances = 5;
    /**
     * 空闲失效秒数
     */
    private Long idelTimeOut = 5 * 60L;
    /**
     * SSL证书支持
     */
    private boolean acceptSslCerts = true;
    /**
     * 截屏支持
     */
    private boolean takesScreenshot = true;
    /**
     * CSS搜索支持
     */
    private boolean cssSelectorsEnabled = true;
    /**
     * IP代理
     */
    private Proxy proxy = null;
    /**
     * HTTP请求头
     */
    private Map<String, String> headersMap = Maps.newHashMap();

    public PhantomPoolBuilder() {

    }
    public PhantomPoolBuilder(String location) {
        this.phantomJsLocation = location;
    }

    public static PhantomPoolBuilder getInstance() {
        return new PhantomPoolBuilder();
    }

    public static PhantomPoolBuilder getInstance(String location) {
        return new PhantomPoolBuilder(location);
    }

    public PhantomPoolBuilder withMinInstances(Integer minInstances) {
        this.minInstances = minInstances;
        return this;
    }

    public PhantomPoolBuilder withMaxInstances(Integer maxInstances) {
        this.maxInstances = maxInstances;
        return this;
    }

    public PhantomPoolBuilder withIdelTimeOut(Long idelTimeOut) {
        this.idelTimeOut = idelTimeOut;
        return this;
    }

    public PhantomPoolBuilder withHttpProxy(String ip, Integer port) {
        this.proxy = new Proxy().setHttpProxy(ip + ":" + port);
        proxy.setProxyType(Proxy.ProxyType.MANUAL);
        proxy.setAutodetect(false);
        return this;
    }

    public PhantomPoolBuilder withProxy(Proxy proxy) {
        this.proxy = proxy;
        return this;
    }

    public PhantomPoolBuilder addHeader(String headerName, String headerValue) {
        this.headersMap.put(headerName, headerValue);
        return this;
    }

    public PhantomPoolBuilder resetHeader() {
        this.headersMap.clear();
        return this;
    }

    private DesiredCapabilities getDesiredCapabilities() {
        DesiredCapabilities capabilities = DesiredCapabilities.iphone();
        capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, this.phantomJsLocation);
        capabilities.setJavascriptEnabled(true);
        capabilities.setCapability("acceptSslCerts", this.acceptSslCerts);  //ssl证书支持
        capabilities.setCapability("takesScreenshot", this.takesScreenshot); //截屏支持
        capabilities.setCapability("cssSelectorsEnabled", this.cssSelectorsEnabled); //css搜索支持
        capabilities.setCapability("phantomjs.page.settings.userAgent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36");
        capabilities.setCapability("phantomjs.page.customHeaders.User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36");
        if (this.proxy != null) {
            capabilities.setCapability(CapabilityType.PROXY, proxy);
        }
        for (Map.Entry<String, String> header : headersMap.entrySet()) {
            capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_CUSTOMHEADERS_PREFIX + header.getKey(), header.getValue());
        }
        return capabilities;
    }


    /**
     * @return
     */
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
        String jsPath = PhantomPoolBuilder.class.getResource("/").getPath() +"spider/" + "spider.js";
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
        Process p = rt.exec(execStr);
        InputStream is = p.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuffer sbf = new StringBuffer();
        String tmp = "";
        while((tmp=br.readLine())!=null) {
            sbf.append(tmp + "\n");
        }
        return sbf.toString();
    }

    public static void main(String[] args) throws Exception{
        String proxyIp="";
        String s = getHtmlByPhantomJs("http://www.mogoroom.com/room/2482348.shtml?","www.mogoroom.com","http://www.mogoroom.com/list",proxyIp);
        System.out.println(s);
    }
}