package com.apass.zufang.web.banner;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.apass.gfb.framework.exception.BusinessException;
import com.apass.gfb.framework.security.toolkit.SpringSecurityUtils;
import com.apass.gfb.framework.utils.CommonUtils;
import com.apass.gfb.framework.utils.GsonUtils;
import com.apass.gfb.framework.utils.HttpWebUtils;
import com.apass.gfb.framework.utils.ImageUtils;
import com.apass.zufang.common.utils.MapEntryOrConverUtils;
import com.apass.zufang.domain.Response;
import com.apass.zufang.domain.dto.BannerQueryParams;
import com.apass.zufang.domain.vo.BannerVo;
import com.apass.zufang.service.apartment.ImageService;
import com.apass.zufang.service.banner.BannerService;
import com.apass.zufang.utils.ResponsePageBody;
import com.apass.zufang.utils.ValidateUtils;

@Path("/banner")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class BannerController{
    /*** 日志*/
    private static final Logger logger = LoggerFactory.getLogger(BannerController.class);
   
    @Autowired
    private BannerService bannerService;
    
    private static final String BANNER_TYPE = "bannerType";
    /*** 图片服务器地址*/
    @Value("${nfs.rootPath}")
    private String rootPath;

   
    @POST
	@Path("/queryBanner")
	public Response queryBannerList(Map<String,Object> paramMap){
		ResponsePageBody<BannerVo> respBody = new ResponsePageBody<>();
		try {
			BannerQueryParams dto = (BannerQueryParams) MapEntryOrConverUtils.converMap(BannerQueryParams.class, paramMap);
			if(dto.getPage() == null || dto.getRows() == null){
        		dto.setPage(1);
        		dto.setRows(10);
        	}
			respBody = bannerService.getBannerListsExceptDelete(dto);
        	respBody.setMsg("banner信息列表查询成功!");
			return Response.success("查询banner信息成功！", respBody);
		} catch (Exception e) {
			 logger.error("getBannerListsExceptDelete EXCEPTION --- --->{}", e);
	         respBody.setMsg("banner信息列表查询失败!");
			 return Response.fail("查询banner信息失败!");
		}
		
    }
    
    @POST
	@Path("/addBanner")
	public Response addBanner(Map<String, Object> paramMap){
    	try {
			logger.info("add/edit banner paramMap--->{}",GsonUtils.toJson(paramMap));
			BannerVo vo = (BannerVo) MapEntryOrConverUtils.converMap(BannerVo.class, paramMap);
			
			ValidateUtils.isNotBlank(vo.getType(), "banner类型不能为空!");
			ValidateUtils.isNotBlank(vo.getSort(),"banner排序不能为空!");
			ValidateUtils.isPositiveNumber(vo.getSort(), "banner排序");
			ValidateUtils.isNotBlank(vo.getActivityUrl(),"banner链接不能为空!");
			ValidateUtils.isNotBlank(vo.getImgUrl(),"banner图片不能为空!");
			
			vo.setOperationName(SpringSecurityUtils.getCurrentUser());
			bannerService.saveOrUpdateBanner(vo);
			return Response.success("add banner信息成功！");
		}catch (BusinessException e){
			logger.error("add/edit banner businessException---->{}",e);
			return Response.fail(e.getErrorDesc());
		}catch (Exception e) {
			logger.error("新增/编辑banner信息失败，错误原因", e);
		    return Response.fail("新增/编辑banner信息失败！");
		}
    }
    
    /**
	 * 删除房屋信息
	 * @param paramMap
	 * @return
	 */
	@POST
	@Path("/delBanner")
	public Response delBanner(Map<String, Object> paramMap){
		try {
			logger.info("del banner paramMap--->{}",GsonUtils.toJson(paramMap));
			String id = CommonUtils.getValue(paramMap, "id");
			ValidateUtils.isNotBlank(id, "bannerId为空！");
			bannerService.deleteBanner(id, SpringSecurityUtils.getCurrentUser());
			return Response.success("删除banner信息成功！");
		}catch (BusinessException e){
			logger.error("delete banner businessException---->{}",e);
			return Response.fail(e.getErrorDesc());
		}catch (Exception e) {
			logger.error("删除banner信息失败，错误原因", e);
		    return Response.fail("删除banner信息失败！");
		}
	}

    /**
     * 查看图片
     * 
     * @param request
     * @param response
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/getPoto")
    public void getPoto(HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
        } catch (IOException e1) {
            logger.error("系统出错:", e1);
        }
        if (null == writer) {
            return;
        }
        // 从缓存获取图片
        JSONObject obj = new JSONObject();

        String bannerImgUrl = HttpWebUtils.getValue(request, "bannerImgUrl");
        if (!StringUtils.isBlank(bannerImgUrl)) {
            obj.put("showPicture", ImageUtils.imageToBase64(rootPath + bannerImgUrl));
        }
        writer.write(obj.toString());
    }
    
}
