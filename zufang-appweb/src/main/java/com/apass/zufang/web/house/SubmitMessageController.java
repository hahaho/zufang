package com.apass.zufang.web.house;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.apass.zufang.domain.Response;
import com.apass.zufang.domain.entity.SubmitMessage;
import com.apass.zufang.service.operation.SubmitMessageService;
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
    		SubmitMessage entity = new SubmitMessage();
    		return submitMessageService.addSubmitMessage(entity);
    	}catch(Exception e){
    		LOGGER.error("getSubmitMessageList EXCEPTION --- --->{}", e);
    		return Response.fail("意见反馈新增失败");
    	}
    }
}