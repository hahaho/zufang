package com.apass.zufang.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.apass.gfb.framework.utils.GsonUtils;
import com.apass.zufang.domain.common.CoordinateAddress;
import com.apass.zufang.domain.common.GaodeLocation;
import com.apass.zufang.domain.common.WorkSubwayContent;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 坐标 位置 信息转换
 * 
 * @author zhanwendong
 *
 */
@Component
public class ObtainGaodeLocation {

	/**
	 * key值
	 */
	@Value("${gaode.location.key}")
	private String KEY;

	/**
	 * 获取坐标信息
	 * 
	 * @param address
	 * @return
	 */
	public GaodeLocation addressToGPS(String address) {

		try {

			String url = String.format("http://restapi.amap.com/v3/geocode/geo?&s=rsv3&address=%s&key=%s", address,
					KEY);

			URL myURL = null;

			URLConnection httpsConn = null;

			try {

				myURL = new URL(url);

			} catch (MalformedURLException e) {

				e.printStackTrace();

			}

			InputStreamReader insr = null;

			BufferedReader br = null;

			httpsConn = (URLConnection) myURL.openConnection();// 不使用代理

			if (httpsConn != null) {

				insr = new InputStreamReader(httpsConn.getInputStream(), "UTF-8");

				br = new BufferedReader(insr);

				String data = "";

				String line = null;

				while ((line = br.readLine()) != null) {

					data += line;

				}

				GaodeLocation jsonGaode = GsonUtils.convertObj(data, GaodeLocation.class);

				return jsonGaode;
			}
		} catch (Exception e) {

			e.printStackTrace();

			return null;
		}

		return null;

	}

	/**
	 * 根据坐标获取详细地址
	 * 
	 * @param log
	 * @param lat
	 * @return
	 */
	public static CoordinateAddress getAdd(String log, String lat) {

		CoordinateAddress result = new CoordinateAddress();
		// lat 小 log 大
		// 参数解释: 纬度,经度 type 001 (100代表道路，010代表POI，001代表门址，111可以同时显示前三项)
		String urlString = "http://gc.ditu.aliyun.com/regeocoding?l=" + lat + "," + log + "&type=010";
		String res = "";
		try {
			URL url = new URL(urlString);
			java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			java.io.BufferedReader in = new java.io.BufferedReader(
					new java.io.InputStreamReader(conn.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				res += line + "\n";
			}
			in.close();
		} catch (Exception e) {
			System.out.println("error in wapaction,and e is " + e.getMessage());
		}
		JSONObject jsonObject = JSONObject.fromObject(res);
		JSONArray jsonArray = JSONArray.fromObject(jsonObject.getString("addrList"));
		JSONObject j_2 = JSONObject.fromObject(jsonArray.get(0));
		int status = j_2.getInt("status");
		if (status == 1) {
			result.setName(j_2.getString("name"));
			result.setId(j_2.getString("id"));
			result.setAdmCode(j_2.getString("admCode"));

			String allAdd = j_2.getString("admName");
			String arr[] = allAdd.split(",");
			result.setProvince(arr[0]);
			result.setCity(arr[1]);
			result.setArea(arr[2]);
			result.setAddr(j_2.getString("addr"));
			result.setDistance(j_2.getDouble("distance"));

		} else {
			return null;
		}
		return result;
	}

	/**
	 * 根据 获取具体坐标
	 * 
	 * @param address
	 * @return
	 */
	public String[] getLocation(String address) {

		String[] result = null;
		GaodeLocation resultDto = addressToGPS(address);
		if (resultDto.getStatus().equals("0")) {
			return result;
		}
		result = resultDto.getGeocodes().get(0).getLocation().split(",");

		return result;
	}
	
	
	public static GaodeLocation addressToGPSKey(String address) {

		try {

			String url = String.format("http://restapi.amap.com/v3/geocode/geo?&s=rsv3&address=%s&key=%s", address,
					"29fe2f62be073f4005f5da10459ccce7");

			URL myURL = null;

			URLConnection httpsConn = null;

			try {

				myURL = new URL(url);

			} catch (MalformedURLException e) {

				e.printStackTrace();

			}

			InputStreamReader insr = null;

			BufferedReader br = null;

			httpsConn = (URLConnection) myURL.openConnection();// 不使用代理

			if (httpsConn != null) {

				insr = new InputStreamReader(httpsConn.getInputStream(), "UTF-8");

				br = new BufferedReader(insr);

				String data = "";

				String line = null;

				while ((line = br.readLine()) != null) {

					data += line;

				}

				GaodeLocation jsonGaode = GsonUtils.convertObj(data, GaodeLocation.class);

				return jsonGaode;
			}
		} catch (Exception e) {

			e.printStackTrace();

			return null;
		}

		return null;

	}
	public static String[] getLocationKey(String address) {

		String[] result = null;
		GaodeLocation resultDto = addressToGPSKey(address);
		if(null==resultDto){
			resultDto = addressToGPSKey(address+"地铁站");
		}
		if (resultDto.getStatus().equals("0")) {
			return result;
		}
		if(!resultDto.getGeocodes().isEmpty()){
			result = resultDto.getGeocodes().get(0).getLocation().split(",");
		}else{
			result = "0.00,0.00".split(",");
		}
		

		return result;
	};
	
	/**
	 * 获取地铁数据
	 * 
	 * @param log
	 * @param lat
	 * @return
	 */
	public static List<WorkSubwayContent> getWorksubwayObject(String qt, String c, String t) {

		String urlString = "http://map.baidu.com/?qt=" + qt + "&c=" + c + "&t=" + t;

		String res = "";
		try {
			URL url = new URL(urlString);
			java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			java.io.BufferedReader in = new java.io.BufferedReader(
					new java.io.InputStreamReader(conn.getInputStream(), "utf-8"));
			String line;
			while ((line = in.readLine()) != null) {
				res += line + "\n";
			}
			in.close();
		} catch (Exception e) {
			System.out.println("error in wapaction,and e is " + e.getMessage());
		}
		JSONObject jsonObject = JSONObject.fromObject(res);
		List<WorkSubwayContent> jsonGaode = GsonUtils.convertList(jsonObject.getString("content"),
				WorkSubwayContent.class);
		for (WorkSubwayContent workSubwayContent : jsonGaode) {
			// 删除带括号数据
			workSubwayContent
					.setLine_name(workSubwayContent.getLine_name().replaceAll("\\(.*?\\)|\\{.*?}|\\[.*?]|（.*?）", ""));
			workSubwayContent
					.setLine_name(workSubwayContent.getLine_name().replace("地铁", ""));
		}
		//去重
		for (int i = 0; i < jsonGaode.size() - 1; i++) {
			for (int j = jsonGaode.size() - 1; j > i; j--) {
				if (jsonGaode.get(j).getLine_name().equals(jsonGaode.get(i).getLine_name())) {
					jsonGaode.remove(j);
				}
			}
		}
		return jsonGaode;
	}

	public static void main(String[] args) {

//		getWorksubwayObject("bsi", "289", "1519794878878");

		// GaodeLocation data =
//		 new ObtainGaodeLocation().addressToGPS("上海市东方明珠广播电视塔有限公司");
		//
		// CoordinateAddress add = getAdd("121.499361", "31.240229");
		//
		// System.out.println("经度,纬度:" +
		// data.getGeocodes().get(0).getLocation());

	}

}
