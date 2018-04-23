package com.apass.zufang.web.operations;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.apass.gfb.framework.utils.CommonUtils;
import com.apass.gfb.framework.utils.GsonUtils;
import com.apass.zufang.domain.dto.ApprintmentQueryParams;
import com.apass.zufang.domain.vo.SubmitMessageVo;
import com.apass.zufang.service.operation.SubmitMessageService;
import com.apass.zufang.utils.ResponsePageBody;
/**
 * 意见反馈-意见反馈管理
 * @author haotian
 *
 */
@Path("/operations/SubmitMessageController")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class SubmitMessageController {
	private static final Logger LOGGER  = LoggerFactory.getLogger(SubmitMessageController.class);
	@Autowired
	public SubmitMessageService submitMessageService;
	/**
	 * 品牌公寓热门房源配置页面
	 * @return
	 */
    @GET
	@Path("/init")
    public String init() {
        return "operations/submitMessage";
    }
    /**
     * 意见反馈列表查询
     * @param map
     * @return
     */
    @POST
	@Path("/getSubmitMessageList")
    public ResponsePageBody<SubmitMessageVo> getSubmitMessageList(Map<String,Object> map) {
        ResponsePageBody<SubmitMessageVo> respBody = new ResponsePageBody<SubmitMessageVo>();
        try {
        	LOGGER.info("getSubmitMessageList map--->{}",GsonUtils.toJson(map));
        	String page = CommonUtils.getValue(map, "page");
        	String rows = CommonUtils.getValue(map, "rows");
        	ApprintmentQueryParams entity = new ApprintmentQueryParams();
        	entity.setPage(Integer.parseInt(page));
        	entity.setRows(Integer.parseInt(rows));
        	respBody = submitMessageService.getSubmitMessageList(entity);
        } catch (Exception e) {
            LOGGER.error("getSubmitMessageList EXCEPTION --- --->{}", e);
            respBody.setMsg("意见反馈列表查询失败");
        }
        return respBody;
    }
}