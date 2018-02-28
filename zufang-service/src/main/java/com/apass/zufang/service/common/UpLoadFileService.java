package com.apass.zufang.service.common;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.apass.gfb.framework.exception.BusinessException;
import com.apass.zufang.domain.entity.HouseImg;
import com.apass.zufang.mapper.zfang.HouseImgMapper;
import com.apass.zufang.utils.FileUtilsCommons;
import com.apass.zufang.utils.ImageTools;

@Component
public class UpLoadFileService {
	
	private static final Logger logger = LoggerFactory.getLogger(UpLoadFileService.class);
	
	/** * 图片服务器地址*/
    @Value("${nfs.rootPath}")
    private String rootPath;
    
    /*** 房屋图片存放地址*/
    @Value("${nfs.house}")
    private String nfsHouse;
    
    
    public String uploadHousePic(MultipartFile file) throws BusinessException{
    	
    	if(null == file){
    		throw new BusinessException("上传文件不能为空!");
    	}
    	
    	try {
    		boolean checkImgType = ImageTools.checkImgType(file);// 图片类型
        	boolean checkImgSize320 = ImageTools.checkImgSize(file,750,320);// 尺寸
        	boolean checkImgSize562 = ImageTools.checkImgSize(file, 750, 562);//尺寸
        	int size = file.getInputStream().available();
        	
        	if(!((checkImgType && checkImgSize320) || (checkImgType && checkImgSize562))){
        		throw new BusinessException("文件尺寸不符,上传图片尺寸必须是宽：750px,高：562px或者320px,格式：.jpg,.png");
        	}else if(size > 1024 * 1024 * 2){
        		file.getInputStream().close();
        		throw new BusinessException("文件不能大于2MB!");
        	}
        	String fileName = "logo_" + System.currentTimeMillis() + file.getName();
            String url = nfsHouse + fileName;
            /*** 上传文件*/
            FileUtilsCommons.uploadFilesUtil(rootPath, url, file);
            return url;
		} catch (Exception e) {
			logger.error("上传house logo失败!", e);
			throw new BusinessException("上传图片失败!");
		}
    }
}
