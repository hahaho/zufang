package com.apass.zufang.web.common;

import java.io.FileInputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.apass.gfb.framework.cache.CacheManager;
import com.apass.gfb.framework.environment.SystemEnvConfig;
import com.apass.gfb.framework.utils.GsonUtils;
import com.apass.zufang.domain.Response;
import com.apass.zufang.domain.common.AppVersionInfo;
import com.apass.zufang.domain.common.NotifyInfo;
import com.google.common.collect.Lists;

/**
 * app安装包版本信息检查
 * 
 * @author zwd
 *
 */
@Path("/checkVersion")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class AppVersionCheckController {

	private static final Logger logger = LoggerFactory.getLogger(AppVersionCheckController.class);

	private static final String APPVERSION = "APP_VERSION_CODE";

	@Autowired
	private CacheManager cacheManage;
	
    @Autowired
    private SystemEnvConfig systemEnv;

	/**
	 * versionName string 版本名称 versionCode int 比较版本号 downloadUrl string
	 * 安装包官方下载网址 appSize float 安装包大小（单位：M） upgradeFlag boolean 是否强制升级 explain
	 * string 版本介绍
	 */
	@POST
	@Path("/app")
	public Response checkVersion() {
		try {
			AppVersionInfo info = new AppVersionInfo();
			info.setVersionName("1.2.6");
			info.setVersionCode(126);
			info.setMd5(fileMD5("/usr/local/nginx/html/download/files/ajqhapp_android.apk"));
			info.setAppSize(16.95);
			info.setUpgradeFlag(false);
			info.setExplain("增加618福袋活动功能");
			info.setDistribution(true);

			info.setIosVersionName("1.2.8");
			info.setIosVersionCode(128);
			info.setIosAppSize(16.95);
			info.setIosUpgradeFlag(false);
			info.setExplain("修复已知bug");
			info.setIosDistribution(true);
			info.setIosDistributionNew(true);

			info.setDownloadUrl("http://ajqh.download.apass.cn/files/ajqhapp_android.apk");
			info.setIndexBanner("http://download.apass.vcash.cn/appbannerfile/indexBanner.png?v=1.3");
			info.setMyBanner("http://download.apass.vcash.cn/appbannerfile/myBanner.png?v=1.3");
			info.setCustomerServiceHotline("4001017700");
			info.setAuditPhone("021-60467491");
			info.setWorkTime("周一至周日8:45-20:00");
			
			info.setNotifyList(getNotifyList());

			String appVersionCache = cacheManage.getStr(APPVERSION);

			if (StringUtils.isNotBlank(appVersionCache)) {

				Map<String, String> map = GsonUtils.convertMap(appVersionCache);
				if (map.containsKey("versionName") && StringUtils.isNotBlank(map.get("versionName"))) {
					info.setVersionName(map.get("versionName"));
				}
				if (map.containsKey("versionCode") && StringUtils.isNotBlank(map.get("versionCode"))) {
					info.setVersionCode(Integer.parseInt(map.get("versionCode")));
				}
				if (map.containsKey("fileRoute") && StringUtils.isNotBlank(map.get("fileRoute"))) {
					info.setMd5(fileMD5(map.get("fileRoute")));
				}
				if (map.containsKey("appSize") && StringUtils.isNotBlank(map.get("appSize"))) {
					info.setAppSize(Double.valueOf(map.get("appSize")));
				}
				if (map.containsKey("upgradeflag") && StringUtils.isNotBlank(map.get("upgradeflag"))) {
					info.setUpgradeFlag("1".equals(map.get("upgradeflag")) ? true : false);
				}
				if (map.containsKey("explains") && StringUtils.isNotBlank(map.get("explains"))) {
					info.setExplain(map.get("explains"));
				}
				if (map.containsKey("distribution") && StringUtils.isNotBlank(map.get("distribution"))) {
					info.setDistribution("1".equals(map.get("distribution")) ? true : false);
				}

				if (map.containsKey("iosVersionname") && StringUtils.isNotBlank(map.get("iosVersionname"))) {
					info.setIosVersionName(map.get("iosVersionname"));
				}
				if (map.containsKey("iosVersioncode") && StringUtils.isNotBlank(map.get("iosVersioncode"))) {
					info.setIosVersionCode(Integer.parseInt(map.get("iosVersioncode")));
				}
				if (map.containsKey("iosAppsize") && StringUtils.isNotBlank(map.get("iosAppsize"))) {
					info.setIosAppSize(Double.valueOf(map.get("iosAppsize")));
				}
				if (map.containsKey("iosUpgradeflag") && StringUtils.isNotBlank(map.get("iosUpgradeflag"))) {
					info.setIosUpgradeFlag("1".equals(map.get("iosUpgradeflag")) ? true : false);
				}
				if (map.containsKey("iosExplains") && StringUtils.isNotBlank(map.get("iosExplains"))) {
					info.setIosExplain(map.get("iosExplains"));
				}
				if (map.containsKey("iosDistribution") && StringUtils.isNotBlank(map.get("iosDistribution"))) {
					info.setIosDistributionNew("1".equals(map.get("iosDistribution")) ? true : false);
				}

				if (map.containsKey("downloanurl") && StringUtils.isNotBlank(map.get("downloanurl"))) {
					info.setDownloadUrl(map.get("downloanurl"));
				}
				if (map.containsKey("indexbanner") && StringUtils.isNotBlank(map.get("indexbanner"))) {
					info.setIndexBanner(map.get("indexbanner"));
				}
				if (map.containsKey("mybanner") && StringUtils.isNotBlank(map.get("mybanner"))) {
					info.setMyBanner(map.get("mybanner"));
				}
			}
			logger.info("checkVersion_[{}]", GsonUtils.toJson(info));
			return Response.successResponse(info);
		} catch (Exception e) {
			logger.error("app安装包版本信息检查失败.", e);
			return Response.fail("请求失败!");
		}
	}

	public String fileMD5(String inputFile) {
		// 缓冲区大小（这个可以抽出一个参数）
		int bufferSize = 256 * 1024;
		FileInputStream fileInputStream = null;
		DigestInputStream digestInputStream = null;
		try {
			// 拿到一个MD5转换器（同样，这里可以换成SHA1）
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			// 使用DigestInputStream
			fileInputStream = new FileInputStream(inputFile);
			digestInputStream = new DigestInputStream(fileInputStream, messageDigest);
			// read的过程中进行MD5处理，直到读完文件
			byte[] buffer = new byte[bufferSize];
			while (digestInputStream.read(buffer) > 0)
				;
			// 获取最终的MessageDigest
			messageDigest = digestInputStream.getMessageDigest();
			// 拿到结果，也是字节数组，包含16个元素
			byte[] resultByteArray = messageDigest.digest();
			// 同样，把字节数组转换成字符串
			return byteArrayToHex(resultByteArray);
		} catch (Exception e) {
			logger.error("获取MD5出错了：{}", e.getMessage());
			return "";
		} finally {
			try {
				digestInputStream.close();
				fileInputStream.close();
			} catch (Exception e) {

			}
		}
	}

	public String byteArrayToHex(byte[] byteArray) {
		// 首先初始化一个字符数组，用来存放每个16进制字符
		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		// new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））
		char[] resultCharArray = new char[byteArray.length * 2];
		// 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去
		int index = 0;
		for (byte b : byteArray) {
			resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
			resultCharArray[index++] = hexDigits[b & 0xf];
		}
		// 字符数组组合成字符串返回
		return new String(resultCharArray);
	}
	
    public List<NotifyInfo> getNotifyList(){
        List<NotifyInfo> notifyList = Lists.newArrayList();
        NotifyInfo notify1 = new NotifyInfo();
        notify1.setDesc("客户谨慎还款重要提示");
        notify1.setTitle("安家派");
        notify1.setUrl("https://ajp-app-sit.apass.cn/#/Announcement");
        if (systemEnv.isPROD()) {
            notify1.setUrl("https://ajp-app-prod.apass.cn/#/Announcement");
        }
        notifyList.add(notify1);
        NotifyInfo notify2 = new NotifyInfo();
        notify2.setDesc("客户谨慎还款重要提示");
        notify2.setTitle("安家派");
        notify2.setUrl("https://ajp-app-sit.apass.cn/#/Announcement");
        if (systemEnv.isPROD()) {
            notify2.setUrl("https://ajp-app-prod.apass.cn/#/Announcement");
        }
        notifyList.add(notify2);
        return notifyList;
    }
}
