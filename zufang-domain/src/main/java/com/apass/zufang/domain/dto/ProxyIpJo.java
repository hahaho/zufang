package com.apass.zufang.domain.dto;

/**
 * Created by DELL on 2018/5/9.
 */
public class ProxyIpJo {
    private String proxyHost;
    private int proxyPort;
    private float  speed; //速度
    private float connectSpeed; //连接速度

    public ProxyIpJo(String proxyHost,int proxyPort,float  speed,float connectSpeed){
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        this.speed = speed;
        this.connectSpeed = connectSpeed;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getConnectSpeed() {
        return connectSpeed;
    }

    public void setConnectSpeed(float connectSpeed) {
        this.connectSpeed = connectSpeed;
    }
}
