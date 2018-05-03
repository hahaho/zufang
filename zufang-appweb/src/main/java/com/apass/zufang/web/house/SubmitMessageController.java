package com.apass.zufang.web.house;
import java.beans.IntrospectionException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.apass.gfb.framework.exception.BusinessException;
import com.apass.gfb.framework.security.toolkit.SpringSecurityUtils;
import com.apass.gfb.framework.utils.GsonUtils;
import com.apass.zufang.common.utils.MapEntryOrConverUtils;
import com.apass.zufang.domain.Response;
import com.apass.zufang.domain.entity.SubmitMessage;
import com.apass.zufang.service.operation.SubmitMessageService;
import com.apass.zufang.utils.FileUtilsCommons;
import com.apass.zufang.utils.ImageTools;
import com.apass.zufang.utils.ValidateUtils;
import com.google.common.collect.Maps;
/**
 * 意见反馈-意见反馈管理  前端接口
 * @author haotian
 *
 */
@Path("/house/submitMessageController")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class SubmitMessageController {
	private static final Logger LOGGER  = LoggerFactory.getLogger(SubmitMessageController.class);
	//图片服务器地址
    @Value("${nfs.rootPath}")
    private String rootPath;
    //房屋图片存放地址
    @Value("${nfs.house}")
    private String nfsHouse;
	@Autowired
	public SubmitMessageService submitMessageService;
    /**
     * 意见反馈新增
     * @param map
     * @return
     */
    @POST
	@Path("/addSubmitMessage")
    public Response addSubmitMessage(Map<String,Object> map) {
    	try{
    		LOGGER.info("addSubmitMessage map--->{}",GsonUtils.toJson(map));
    		SubmitMessage entity = (SubmitMessage) MapEntryOrConverUtils.converMap(SubmitMessage.class, map);
    		ValidateUtils.isNotBlank(entity.getPhone(), "用户手机号码不可为空！");
    		ValidateUtils.isNotBlank(entity.getSubmitMessage(), "用户留言不可为空！");
//    		ValidateUtils.isNotBlank(entity.getPictureUrl(), "上传图片不可为空！");
    		if(entity.getSubmitMessage().length()>300){
    			throw new BusinessException("用户留言长度不可超过300字！");
    		}
    		entity.setUserId(1000L);
    		entity.setSubmitTime(new Date());
    		entity.setCreatedTime(new Date());
    		entity.setUpdatedTime(new Date());
    		entity.setIsDelete("00");
    		String username = SpringSecurityUtils.getCurrentUser();
    		entity.setUserName(username);
    		return submitMessageService.addSubmitMessage(entity,username);
    	}catch(IntrospectionException|InstantiationException|IllegalAccessException|IllegalArgumentException  e){
    		LOGGER.error("addSubmitMessage EXCEPTION --- --->{}", e);
    		return Response.fail("意见反馈新增失败");
    	}catch(BusinessException e){
    		LOGGER.error("addSubmitMessage EXCEPTION --- --->{}", e);
    		return Response.fail("意见反馈新增失败,"+e.getErrorDesc());
    	}catch(Exception e){
    		LOGGER.error("addSubmitMessage EXCEPTION --- --->{}", e);
    		return Response.fail("意见反馈新增失败");
    	}
    }
    /**
     * 意见反馈上传图片
     * @param siftGoodFileModel
     * @return
	 * @throws BusinessException 
     */
    @ResponseBody
    @RequestMapping(value = "/upLoadCompanyLogo", method = RequestMethod.POST)
    public Response upLoadSubmitMessagePicture(@ModelAttribute("file") MultipartFile file) {
        InputStream is = null;
        try {
        	if(file == null){
        		throw new BusinessException("上传图片为空!");
        	}
            is = file.getInputStream();
            int size = is.available();// 大小
            String imgType = ImageTools.getImgType(file);
            String fileName = "SubMess_" + System.currentTimeMillis()+ "." + imgType;
            String url = nfsHouse + fileName;
            //图片校验
            boolean checkImgType = ImageTools.checkImgType(file);// 类型
            if (!checkImgType) {
                return Response.fail("图片尺寸格式不符,格式要求：.jpg,.png");
            } else if (size > 1024 * 500) {
                return Response.fail("图片不能大于500kb!");
            }
            FileUtilsCommons.uploadFilesUtil(rootPath, url, file);
            Map<String,Object> map = Maps.newHashMap();
            map.put("url",url);
            return Response.success("意见反馈上传图片成功！",map);
        } catch (Exception e) {
        	LOGGER.error("upLoadSubmitMessagePicture Exception---->{}",e);
            return Response.fail("意见反馈上传图片失败!");
        }finally{
            try {
                if(is!=null){
                    is.close();
                }
            } catch (IOException e) {
            	LOGGER.error("upLoadSubmitMessagePicture IOException businessException---->{}",e);
            }
        }
    }
}