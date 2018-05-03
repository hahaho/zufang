package com.apass.zufang.noauth;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.apass.gfb.framework.exception.BusinessException;
import com.apass.zufang.domain.Response;
import com.apass.zufang.utils.FileUtilsCommons;
import com.apass.zufang.utils.ImageTools;
import com.google.common.collect.Maps;
@Controller
@RequestMapping("/application")
public class UpLoadPictureController {
	private static final Logger logger = LoggerFactory.getLogger(UpLoadPictureController.class);
	/** * 图片服务器地址*/
    @Value("${nfs.rootPath}")
    private String rootPath;
    /*** 房屋图片存放地址*/
    @Value("${nfs.house}")
    private String nfsHouse;
    
    /*** 房屋图片存放地址*/
    @Value("${nfs.banner}")
    private String nfsBanner;
    
    @Value("${zufang.image.uri}")
    private String imageUri;
    
	@ResponseBody
    @RequestMapping(value = "/uppicture320", method = RequestMethod.POST)
	public Response uploadPicture320(@ModelAttribute("file") MultipartFile file){
		return uploadImg(nfsHouse,1024*1024*2,file, 750,850,562,662);
    }
	
	@ResponseBody
    @RequestMapping(value = "/uppicture428", method = RequestMethod.POST)
	public Response uploadPicture428(@ModelAttribute("file") MultipartFile file){
		return uploadImg(nfsBanner,1024*500,file, 750,750,428,428);
    }
	/**
	 * 
	 * @param fileFolder 文件夹
	 * @param fileSize 文件大小
	 * @param file 文件
	 * @param minWidths 最小宽度
	 * @param maxWidths 最大宽度
	 * @param minHeights最小高度
	 * @param maxHeights最大高度
	 * @return
	 */
	public Response uploadImg(String fileFolder,long fileSize,MultipartFile file,int minWidths,int maxWidths,int minHeights,int maxHeights){
		try{
    		if(null == file){
        		throw new BusinessException("上传文件不能为空!");
        	}
    		boolean checkImgType = ImageTools.checkImgType(file);// 图片类型
        	boolean checkImgSize = ImageTools.checkImgSize(file,minWidths,maxWidths,minHeights,maxHeights);// 尺寸
        	int size = file.getInputStream().available();
        	
        	if(!(checkImgType && checkImgSize)){
        		String width = "";
        		String height = "";
        		if(minWidths == maxWidths){
        			width = minWidths+"";
        		}else{
        			width = minWidths+"~"+maxWidths+"";
        		}
        		if(minHeights == maxHeights){
        			height = minHeights+"";
        		}else{
        			height = minHeights+"~"+maxHeights+"";
        		}
        		throw new BusinessException("文件尺寸不符,上传图片尺寸必须是宽：["+width+"]px,高：["+height+"]px,格式：.jpg,.png");
        	}else if(size > fileSize){
        		file.getInputStream().close();
        		throw new BusinessException("文件不能大于"+fileSize+"K!");
        	}
        	String imgType = ImageTools.getImgType(file);
        	String fileName = "logo_" + System.currentTimeMillis()+"_"+ file.getName()+ "." + imgType;
            String url = fileFolder + fileName;
            /*** 上传文件*/
            FileUtilsCommons.uploadFilesUtil(rootPath, url, file);
            Map<String,Object> values = Maps.newHashMap();
            values.put("url",url);
            values.put("fullurl",imageUri+url);
            return Response.success("success",values);
        }catch (BusinessException e){
			logger.error("delpicture businessException---->{}",e);
			return Response.fail(e.getErrorDesc());
		}catch (Exception e) {
			logger.error("上传 logo失败!", e);
			return Response.fail("上传图片失败!");
        }
    }
	
	/**
     * 上传公司LOGO成功
     * @param siftGoodFileModel
     * @return
	 * @throws BusinessException 
     */
    @ResponseBody
    @RequestMapping(value = "/upLoadCompanyLogo", method = RequestMethod.POST)
    public Response upLoadCompanyLogo(@ModelAttribute("file") MultipartFile file) {
        InputStream is = null;
        try {
        	if(file == null){
        		throw new BusinessException("上传图片为空!");
        	}
            is = file.getInputStream();
            int size = is.available();// 大小
            String imgType = ImageTools.getImgType(file);
            String fileName = "companyLogo_" + System.currentTimeMillis()+ "." + imgType;
            String url = nfsHouse + fileName;
            //图片校验
            boolean checkCompanyLogoSize = ImageTools.checkCompanyLogoSize(file);// 尺寸
            boolean checkImgType = ImageTools.checkImgType(file);// 类型
            if (!checkCompanyLogoSize) {
                return Response.fail("图片尺寸格式不符,尺寸要求:120*120");
            }else if (!checkImgType) {
                return Response.fail("图片尺寸格式不符,格式要求：.jpg,.png");
            } else if (size > 1024 * 500) {
                return Response.fail("图片不能大于500kb!");
            }
            FileUtilsCommons.uploadFilesUtil(rootPath, url, file);
            Map<String,Object> map = Maps.newHashMap();
            map.put("url",url);
            map.put("fullurl",imageUri+url);
            return Response.success("上传公司LOGO成功！",map);
        } catch (Exception e) {
        	logger.error("upLoadCompanyLogo Exception---->{}",e);
            return Response.fail("上传公司LOGO失败!");
        }finally{
            try {
                if(is!=null){
                    is.close();
                }
            } catch (IOException e) {
                logger.error("IOException businessException---->{}",e);
            }
        }
    }
    /**
     * 热门房源图片上传
     * @param file
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/upLoadHotHouseImg", method = RequestMethod.POST)
    public Response upLoadHotHouseImg(@ModelAttribute("file") MultipartFile file) {
        InputStream is = null;
        try {
        	if(file == null){
        		throw new BusinessException("上传图片为空!");
        	}
            is = file.getInputStream();
            int size = is.available();// 大小
            String imgType = ImageTools.getImgType(file);
            String fileName = "companyLogo_" + System.currentTimeMillis()+ "." + imgType;
            String url = nfsHouse + fileName;
            //图片校验
            boolean checkHotHouseImgSize = ImageTools.checkHotHouseImgSize(file);// 尺寸
            boolean checkImgType = ImageTools.checkImgType(file);// 类型
            if (!checkHotHouseImgSize) {
                return Response.fail("图片尺寸格式不符,尺寸要求：750*320");
            }else if (!checkImgType) {
                return Response.fail("图片尺寸格式不符,格式要求：.jpg,.png");
            } else if (size > 1024 * 300) {
                return Response.fail("图片不能大于300kb!");
            }
            FileUtilsCommons.uploadFilesUtil(rootPath, url, file);
            return Response.success("上传热门图片成功！",url);
        } catch (Exception e) {
        	logger.error("upLoadCompanyLogo Exception---->{}",e);
            return Response.fail("上传热门图片失败!");
        }finally{
            try {
                if(is!=null){
                    is.close();
                }
            } catch (IOException e) {
                logger.error("IOException businessException---->{}",e);
            }
        }
    }
}