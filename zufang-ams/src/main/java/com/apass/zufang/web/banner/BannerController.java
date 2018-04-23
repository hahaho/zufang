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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.apass.gfb.framework.utils.HttpWebUtils;
import com.apass.gfb.framework.utils.ImageUtils;
import com.apass.zufang.common.utils.MapEntryOrConverUtils;
import com.apass.zufang.domain.Response;
import com.apass.zufang.domain.dto.BannerQueryParams;
import com.apass.zufang.domain.vo.BannerVo;
import com.apass.zufang.service.banner.BannerService;
import com.apass.zufang.utils.ResponsePageBody;

@Path("/banner")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class BannerController{
    /*** 日志*/
    private static final Logger logger = LoggerFactory.getLogger(BannerController.class);
   
    @Autowired
    private BannerService bannerService;
    
    private static final String BANNER_TYPE = "bannerType";
    /**
     * 图片服务器地址
     */
//    @Value("${nfs.rootPath}")
    private String rootPath;

//    @Value("${nfs.banner}")
    private String nfsBanner;

   
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

    /**
     * 添加banner信息 上传图片文件
     * 
     * @param pageModel
     * @return
     */
//    @ResponseBody
//    @RequestMapping(value = "/addBannerFile", method = RequestMethod.POST)
//    @LogAnnotion(operationType = "添加banner信息", valueType = LogValueTypeEnum.VALUE_DTO)
//    public Response addBannerInfor(AddBannerInfoEntity pageModel) {
//        Banner entity = new Banner();
//        try {
//            String bannerName = pageModel.getBannerName();
//            String bannerType = pageModel.getBannerType();
//            String bannerOrder = pageModel.getBannerOrder();
//            String activityUrl = pageModel.getActivityUrl();
//            String activityName =pageModel.getActivityName();
//            if (StringUtils.isAnyBlank(bannerType, bannerOrder) || !StringUtils.isNumeric(bannerOrder)) {
//                throw new BusinessException("参数有误.");
//            }
//
//            if (pageModel.getBannerFile().getInputStream().available() == 0 && pageModel.getBannerId() == null) {
//                throw new BusinessException("请选择文件.");
//            }
//            //电商3期511 20170519 banner 添加设置商品链接
//            if(StringUtils.isNotBlank(activityUrl)){
//            	if("activity".equals(activityName)){
//                  entity.setAttrVal(activityUrl);
//            		activityUrl="ajqh://cn.apass.ajqh/web?url="+activityUrl;
//                  entity.setAttr("activity");
//            	}else if("goodId".equals(activityName)){
//                  //这里由原来的goodId 改为 商品编号或skuid
//                  entity.setAttrVal(activityUrl);
//                  //GoodsBasicInfoEntity goodsInfo=goodsService.getByGoodsBySkuIdOrGoodsCode2(activityUrl, SourceType.WZ);
//                  GoodsBasicInfoEntity goodsInfo=goodsService.getByGoodsBySkuIdOrGoodsCode2(activityUrl);
//                  if(goodsInfo == null){
//                      return Response.fail("请添加已上架的商品");
//                  }
//            		if(StringUtils.isNotBlank(goodsInfo.getSource()) ){
//            			activityUrl="ajqh://cn.apass.ajqh/goods?id="+goodsInfo.getGoodId()+"&source=jd";
//            		}else{
//            			activityUrl="ajqh://cn.apass.ajqh/goods?id="+goodsInfo.getGoodId()+"&source=notJd";
//            		}
//                  entity.setAttr("goodId");
//
//            		
//            	}
//
//                entity.setActivityUrl(activityUrl);
//            }
//            entity.setBannerName(bannerName);
//            entity.setBannerType(bannerType);
//            entity.setBannerOrder(Long.valueOf(bannerOrder));
//
//            //图片验证
//            MultipartFile file = pageModel.getBannerFile();
//            if(file.getInputStream().available() >0){
//                String imgType = ImageTools.getImgType(file);
//                String fileName = FILE_NAME_PREFIX + bannerOrder + "_" + System.currentTimeMillis() + "." + imgType;
//                String fileUrl = nfsBanner + bannerType + "/" + fileName;
//
//                boolean checkHomePageBannerImgSize = false;
//                if("index".equals(bannerType)){
//                    checkHomePageBannerImgSize = ImageTools.checkHomePageBannerImgSize(file);
//                }else{
//                    checkHomePageBannerImgSize = ImageTools.checkHomePageBannerImgSizeForSift(file);
//                }
//                boolean checkImgType = ImageTools.checkImgType(file);// 尺寸
//                int size = file.getInputStream().available();
//
//                if (!(checkHomePageBannerImgSize && checkImgType)) {// 750*420;.png,.jpg
//                    file.getInputStream().close();
//                    if("index".equals(bannerType)){
//                        return Response.fail("文件尺寸不符,上传图片尺寸必须是宽：750px,高：300px,格式：.jpg,.png");
//                    }else{
//                        return Response.fail("文件尺寸不符,上传图片尺寸必须是宽：750px,高：300px,格式：.jpg,.png");
//                    }
//                } else if (size > 1024 * 512) {
//                    file.getInputStream().close();
//                    return Response.fail("文件不能大于500kb!");
//                }
//
//                FileUtilsCommons.uploadFilesUtil(rootPath, fileUrl, file);
//
//                pageModel.setBannerFile(null);
//                entity.setBannerImgUrl(fileUrl);
//            }
//
//            Integer result = null;
//            if(pageModel.getBannerId() == null){
//                String respon = isAdd(bannerType, Long.valueOf(bannerOrder));
//                if (!"ok".equals(respon)) {
//                    return Response.fail(respon);
//                }
//                entity.setCreateUser(SpringSecurityUtils.getLoginUserDetails().getUsername());
//                entity.setUpdateUser(SpringSecurityUtils.getLoginUserDetails().getUsername());
//                result = bannerInfoService.addBannerInfor(entity);
//                if (result == 1) {
//                    return Response.success("上传banner成功！");
//                } else {
//                    return Response.fail("上传banner失败！");
//                }
//            }else{
//                entity.setId(pageModel.getBannerId());
//                entity.setUpdateUser(SpringSecurityUtils.getLoginUserDetails().getUsername());
//                result = bannerInfoService.update(entity);
//                if (result == 1) {
//                    return Response.success("编辑操作成功！");
//                } else {
//                    return Response.fail("上传banner失败！");
//                }
//            }
//
//        } catch (BusinessException e) {
//            LOGGER.error("上传banner失败！", e);
//            return Response.fail(e.getErrorDesc());
//        } catch (Exception e) {
//            LOGGER.error("上传banner失败！", e);
//            return Response.fail("上传banner失败！");
//        }
//    }

    /**
     * 判断是否可以添加图片
     * 
     * @param bannerType
     * @param bannerOrder
     * @return
     */
    private String isAdd(String bannerType, Long bannerOrder) {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put(BANNER_TYPE, bannerType);
//        List<Banner> banners = bannerInfoService.loadBannersList(map); // 判断是否大于等于5
//        if (banners.size() >= 0 && banners.size() < 5) {
//            for (Banner banner : banners) {
//                if (bannerOrder == banner.getBannerSort()) { // 判断是否存在重复
//                    return  "该排序值已经存在！";
//                }
//            }
//        } else {
//            return "该banner类型超过5条，请删除后再添加！";
//        }
        return "ok";
    }
}
