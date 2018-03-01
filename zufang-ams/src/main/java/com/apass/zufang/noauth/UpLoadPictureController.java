package com.apass.zufang.noauth;
import java.io.IOException;
import java.io.InputStream;
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
    @RequestMapping(value = "/uppicture", method = RequestMethod.POST)
	public Response uploadPicture(MultipartFile file){
    	try{
    		if(null == file){
        		throw new BusinessException("上传文件不能为空!");
        	}
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
            return Response.success("success",url);
        }catch (BusinessException e){
			logger.error("delpicture businessException---->{}",e);
			return Response.fail(e.getErrorDesc());
		}catch (Exception e) {
			logger.error("上传house logo失败!", e);
            return Response.fail("上传商品大图失败!");
        }
    }
	/**
     * 编辑精选商品的排序和图片
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
            boolean checkSiftGoodsImgSize = ImageTools.checkSiftGoodsImgSize(file);// 尺寸
            boolean checkImgType = ImageTools.checkImgType(file);// 类型
            if (!(checkSiftGoodsImgSize && checkImgType)) {
                return Response.fail("图片尺寸格式不符,尺寸要求:120*120,格式要求：.jpg,.png");
            } else if (size > 1024 * 500) {
                return Response.fail("图片不能大于501kb!");
            }
            FileUtilsCommons.uploadFilesUtil(rootPath, url, file);
            return Response.success("上传公司LOGO成功！",url);
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
}