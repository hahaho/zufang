package com.apass.zufang.domain.common;

import java.util.List;

import com.google.common.collect.Lists;

public class AppVersionInfo {

	/*************** android start ***************/
	/**
	 * 版本号
	 */
	private String versionName;
	/**
	 * 版本编码
	 */
	private int versionCode;
	/**
	 * 文件路径
	 */
	private String md5;
	/**
	 * 文件大小
	 */
	private double appSize;
	/**
	 * 是否强制升级 false 否 true 是
	 */
	private boolean upgradeFlag;
	/**
	 * 升级描述
	 */
	private String explain;

	private boolean distribution;

	/*************** android end ***************/

	/*************** ios start ***************/
	/**
	 * ios版本号
	 */
	private String iosVersionName;
	/**
	 * ios版本编码
	 */
	private int iosVersionCode;
	/**
	 * ios 包大小
	 */
	private double iosAppSize;
	/**
	 * ios 是否强制升级 0 否 1 是
	 */
	private boolean iosUpgradeFlag;
	/**
	 * ios 升级描述
	 */
	private String iosExplain;

	private boolean iosDistribution;

	private boolean iosDistributionNew;

	/*************** ios end ***************/

	private String downloadUrl;

	private String indexBanner;

	private String myBanner;

	/**
	 * 客户热线
	 */
	private String customerServiceHotline;

	/**
	 * 工作时间
	 */
	private String workTime;

	/**
	 * 电审电话
	 */
	private String auditPhone;
	
	private List<NotifyInfo> notifyList = Lists.newArrayList();

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public double getAppSize() {
		return appSize;
	}

	public void setAppSize(double appSize) {
		this.appSize = appSize;
	}

	public boolean isUpgradeFlag() {
		return upgradeFlag;
	}

	public void setUpgradeFlag(boolean upgradeFlag) {
		this.upgradeFlag = upgradeFlag;
	}

	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}

	public boolean isDistribution() {
		return distribution;
	}

	public void setDistribution(boolean distribution) {
		this.distribution = distribution;
	}

	public String getIosVersionName() {
		return iosVersionName;
	}

	public void setIosVersionName(String iosVersionName) {
		this.iosVersionName = iosVersionName;
	}

	public int getIosVersionCode() {
		return iosVersionCode;
	}

	public void setIosVersionCode(int iosVersionCode) {
		this.iosVersionCode = iosVersionCode;
	}

	public double getIosAppSize() {
		return iosAppSize;
	}

	public void setIosAppSize(double iosAppSize) {
		this.iosAppSize = iosAppSize;
	}

	public boolean isIosUpgradeFlag() {
		return iosUpgradeFlag;
	}

	public void setIosUpgradeFlag(boolean iosUpgradeFlag) {
		this.iosUpgradeFlag = iosUpgradeFlag;
	}

	public String getIosExplain() {
		return iosExplain;
	}

	public void setIosExplain(String iosExplain) {
		this.iosExplain = iosExplain;
	}

	public boolean isIosDistribution() {
		return iosDistribution;
	}

	public void setIosDistribution(boolean iosDistribution) {
		this.iosDistribution = iosDistribution;
	}

	public boolean isIosDistributionNew() {
		return iosDistributionNew;
	}

	public void setIosDistributionNew(boolean iosDistributionNew) {
		this.iosDistributionNew = iosDistributionNew;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getIndexBanner() {
		return indexBanner;
	}

	public void setIndexBanner(String indexBanner) {
		this.indexBanner = indexBanner;
	}

	public String getMyBanner() {
		return myBanner;
	}

	public void setMyBanner(String myBanner) {
		this.myBanner = myBanner;
	}

	public String getCustomerServiceHotline() {
		return customerServiceHotline;
	}

	public void setCustomerServiceHotline(String customerServiceHotline) {
		this.customerServiceHotline = customerServiceHotline;
	}

	public String getWorkTime() {
		return workTime;
	}

	public void setWorkTime(String workTime) {
		this.workTime = workTime;
	}

	public String getAuditPhone() {
		return auditPhone;
	}

	public void setAuditPhone(String auditPhone) {
		this.auditPhone = auditPhone;
	}

    public List<NotifyInfo> getNotifyList() {
        return notifyList;
    }

    public void setNotifyList(List<NotifyInfo> notifyList) {
        this.notifyList = notifyList;
    }

}
