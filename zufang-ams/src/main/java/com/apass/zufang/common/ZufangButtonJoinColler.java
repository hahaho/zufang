package com.apass.zufang.common;
import com.apass.gfb.framework.exception.BusinessException;
import com.apass.gfb.framework.utils.CommonUtils;
import com.apass.gfb.framework.utils.GsonUtils;
import com.apass.zufang.domain.Response;
import com.apass.zufang.domain.vo.HouseVo;
import com.apass.zufang.service.apartment.ApartmentService;
import com.apass.zufang.service.house.HouseService;
import com.apass.zufang.utils.ValidateUtils;
import com.apass.zufang.web.house.HouseControler;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Map;
/**
 * Created by xiaohai on 2018/3/19.
 * 公寓使用方接口对接
 *
 */
@Path("/api/house")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class ZufangButtonJoinColler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZufangButtonJoinColler.class);
    @Autowired
    private HouseService houseService;
    @Autowired
    private ApartmentService apartmentService;
    /**
     * createHouse 新增房源
     * @param paramMap
     * @return
     */
    @POST
    @Path("/create")
    public Response createHouse(Map<String, Object> paramMap){
        Map<String,Object> returnMap = Maps.newHashMap();
        try{
            //房源信息
            HouseVo houseVo = new HouseControler().getVoByParams(paramMap);
            Long apartmentId = apartmentService.getApartmentByUserId(paramMap.get("userId").toString());
            houseVo.setApartmentId(apartmentId);
            String houseCode = houseService.addHouse(houseVo);
            returnMap.put("houseCode",houseCode);
        }catch (Exception e){
            LOGGER.error("添加房源信息失败!",e);
            return Response.fail("添加房源信息失败");
        }
        return Response.success("添加房源信息成功",returnMap);
    }
    /**
     * downHouse  下架
     * @param map
     * @return
     */
    @POST
    @Path("/downHouse")
    public Response downHouse(Map<String, Object> map){
        try{
        	LOGGER.info("downHouse house map--->{}",GsonUtils.toJson(map));
        	String idStr = CommonUtils.getValue(map, "idStr");
        	ValidateUtils.isNotBlank(idStr, "房屋Id字符串为空！");
        	String strarr[] = idStr.split(",");
        	String user = map.get("userId").toString();
        	for(String str : strarr){
        		if(StringUtils.isNotBlank(str)){
        			houseService.downHouse(str, user);
        		}
        	}
        	return Response.success("下架房源信息成功");
        }catch (BusinessException e){
        	LOGGER.error("downHouse businessException---->{}",e);
			return Response.fail(e.getErrorDesc());
        }catch (Exception e){
        	LOGGER.error("downHouse Exception---->{}",e);
            return Response.fail("下架房源信息失败!");
        }
    }
    /**
     * downHouse  上架
     * @param map
     * @return
     */
    @POST
    @Path("/upHouse") 
    public Response upHouse(Map<String, Object> map){
        try{
        	LOGGER.info("upHouse house map--->{}",GsonUtils.toJson(map));
        	String idStr = CommonUtils.getValue(map, "idStr");
        	ValidateUtils.isNotBlank(idStr, "房屋Id字符串为空！");
        	String strarr[] = idStr.split(",");
        	StringBuffer sb = new StringBuffer();
        	String message = null;
        	String user = map.get("userId").toString();
        	for(String str : strarr){
        		if(StringUtils.isNotBlank(str)){
        			sb.append("房源信息：").append(str).append(",").append("上架详情：");
        			message = houseService.upHouse(str, user);
        			sb.append(message).append("。");
        		}
        	}
        	return Response.success(sb.toString());
        }catch (BusinessException e){
        	LOGGER.error("upHouse businessException---->{}",e);
			return Response.fail(e.getErrorDesc());
        }catch (Exception e){
        	LOGGER.error("upHouse Exception---->{}",e);
            return Response.fail("上架房源信息失败!");
        }
    }
}