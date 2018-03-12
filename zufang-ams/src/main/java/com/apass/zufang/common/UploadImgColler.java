package com.apass.zufang.common;

import com.apass.zufang.domain.Response;
import com.apass.zufang.utils.FileUtilsCommons;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by xiaohai on 2018/3/12.
 */
@Controller
@RequestMapping("/noauth/upload/common")
public class UploadImgColler {
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadImgColler.class);

    @Value("${zufang.image.uri}")
    private String appWebDomain;

    @Value("${nfs.rootPath}")
    private String rootPath;
    @Value("${nfs.house}")
    private String nfsHouse;

    @RequestMapping("/img")
    @ResponseBody
    public Response uploadImg(MultipartFile file){
        try{
            String filepath = rootPath+nfsHouse+"common/";
            String fileName = file.getOriginalFilename();
            String[] strArr = fileName.split("\\.");
            if(!StringUtils.equals("jpg",strArr[strArr.length-1])
                    && !StringUtils.equals("png",strArr[strArr.length-1])){
                throw new RuntimeException("请上传，jpg或png格式图片");
            }

            //上传图片
            FileUtilsCommons.uploadImg(filepath,file.getOriginalFilename(),file);

            String url = nfsHouse+"common/"+file.getName();

            return Response.success("上传图片成功",url);
        }catch (Exception e){
            LOGGER.error("上传图片失败，Exception---->{}",e);
            return Response.fail(e.getMessage());
        }

    }
}
