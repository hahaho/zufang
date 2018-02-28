package com.apass.zufang.noauth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.apass.gfb.framework.exception.BusinessException;
import com.apass.zufang.domain.Response;
import com.apass.zufang.service.common.UpLoadFileService;
@Controller
@RequestMapping("/application/")
public class UpLoadPictureController {

	private static final Logger logger = LoggerFactory.getLogger(UpLoadPictureController.class);
	
	@Autowired
	private UpLoadFileService fileService;
	
	@ResponseBody
    @RequestMapping(value = "/uppicture", method = RequestMethod.POST)
    public Response uploadPicture(MultipartFile file){
    	try{
    		if(null == file){
    			throw new BusinessException("上传文件不能为空!");
    		}
    		String url = fileService.uploadHousePic(file);
            return Response.success("success",url);
        }catch (BusinessException e){
			logger.error("delpicture businessException---->{}",e);
			return Response.fail(e.getErrorDesc());
		}catch (Exception e) {
        	logger.error("上传商品大图失败!", e);
            return Response.fail("上传商品大图失败!");
        }
    }
}
