package com.apass.zufang.noauth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.apass.gfb.framework.exception.BusinessException;
import com.apass.zufang.domain.Response;
import com.apass.zufang.utils.FileUtilsCommons;
import com.apass.zufang.utils.ImageTools;
@Controller
@RequestMapping("/application/")
public class UpLoadPictureController {

	private static final Logger logger = LoggerFactory.getLogger(UpLoadPictureController.class);
	
	/** * 图片服务器地址*/
    @Value("${nfs.rootPath}")
    private String rootPath;
    
    /*** 房屋图片存放地址*/
    @Value("${nfs.house}")
    private String nfsHouse;
	
	@ResponseBody
    @RequestMapping(value = "/uppicture320", method = RequestMethod.POST)
	public Response uploadPicture320(MultipartFile file){
		try {
			String url = uploadImg(file, 750, 320);
			return Response.success("success",url);
		} catch (BusinessException e) {
			return Response.fail(e.getErrorDesc());
		}
    }
	
	@ResponseBody
    @RequestMapping(value = "/uppicture562", method = RequestMethod.POST)
	public Response uploadPicture562(MultipartFile file){
		try {
			String url = uploadImg(file, 750, 562);
			return Response.success("success",url);
		} catch (BusinessException e) {
			return Response.fail(e.getErrorDesc());
		}
    }
	
	public String uploadImg(MultipartFile file,int widths,int heights) throws BusinessException{
		try{
    		if(null == file){
        		throw new BusinessException("上传文件不能为空!");
        	}
    		boolean checkImgType = ImageTools.checkImgType(file);// 图片类型
        	boolean checkImgSize = ImageTools.checkImgSize(file,widths,heights);// 尺寸
        	int size = file.getInputStream().available();
        	
        	if(!(checkImgType && checkImgSize)){
        		throw new BusinessException("文件尺寸不符,上传图片尺寸必须是宽："+widths+"px,高："+heights+"px,格式：.jpg,.png");
        	}else if(size > 1024 * 1024 * 2){
        		file.getInputStream().close();
        		throw new BusinessException("文件不能大于2MB!");
        	}
        	String fileName = "logo_" + System.currentTimeMillis() + file.getName();
            String url = nfsHouse + fileName;
            /*** 上传文件*/
            FileUtilsCommons.uploadFilesUtil(rootPath, url, file);
            return url;
        }catch (BusinessException e){
			logger.error("delpicture businessException---->{}",e);
			throw new BusinessException("上传图片失败!");
		}catch (Exception e) {
			logger.error("上传house logo失败!", e);
			throw new BusinessException("上传图片失败!");
        }
	}
}