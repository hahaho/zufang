package com.apass.zufang.service.spider;

import com.google.common.collect.Maps;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

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

    private PhantomPoolBuilder() {
        this("D:/develop/phantomjs-2.1.1-windows/bin/phantomjs.exe");
    }

    private PhantomPoolBuilder(String location) {
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
     * 建议用该方法
     * @return
     */
    public WebDriver buildSingleDriver(String proxyIp) {
        String osname = System.getProperties().getProperty("os.name");
        if(osname.equals("Linux")){
            System.setProperty("phantomjs.binary.path", "/usr/local/phantomjs/bin/phantomjs");
        }else{
            System.setProperty("phantomjs.binary.path", "D:\\\\phantomjs-windows\\\\bin\\\\phantomjs.exe");//设置PhantomJs访问路径
        }
        DesiredCapabilities capabilities = DesiredCapabilities.iphone();
        capabilities.setJavascriptEnabled(true);
        capabilities.setCapability("phantomjs.page.settings.userAgent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36");
        capabilities.setCapability("phantomjs.page.customHeaders.User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36");
        org.openqa.selenium.Proxy proxy = new org.openqa.selenium.Proxy();
        proxy.setProxyType(Proxy.ProxyType.MANUAL);
        proxy.setAutodetect(false);
        proxy.setHttpProxy(proxyIp);

        capabilities.setCapability(CapabilityType.PROXY, proxy);

        return new PhantomJSDriver(capabilities);
    }
}