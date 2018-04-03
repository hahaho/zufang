package com.apass.zufang.common;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.apass.gfb.framework.jwt.TokenManager;
import com.apass.gfb.framework.utils.CommonUtils;
import com.apass.zufang.domain.Response;
import com.apass.zufang.domain.constants.ConstantsUtil;
import com.apass.zufang.domain.entity.rbac.UsersDO;
import com.apass.zufang.domain.enums.TokenCodeEnums;
import com.apass.zufang.service.rbac.UsersService;
import com.google.common.collect.Maps;
/**
 * 
 * 公寓使用方接口对接
 * @author Administrator
 *
 */
@Path("/api/TokenController")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class TokenController {
	private static final Logger LOGGER = LoggerFactory.getLogger(TokenController.class);
	@Autowired
	private UsersService usersService;
	@Autowired
	private TokenManager tokenManager;
	/**
     * getToken 获取token
     * @param paramMap
     * @return
     */
    @POST
    @Path("/token")
    public Response getToken(Map<String, Object> paramMap){
        String token = null;
        Map<String,Object> resultMap = Maps.newHashMap();
        try{
            //1,验证用户信息是否正确
            //用户名
            String username = CommonUtils.getValue(paramMap, "username");
            //密码
            String password = CommonUtils.getValue(paramMap, "password");
            UsersDO usersDO = usersService.selectByUsername(username);
            if(usersDO == null || !new BCryptPasswordEncoder().matches(password,usersDO.getPassword())){
                resultMap.put("code", TokenCodeEnums.TOKEN_INVALID.getCode());
                throw new RuntimeException("用户信息不存在，请联系管理员添加用户");
            }
            //2,生成token返回
            token = tokenManager.createToken(usersDO.getId().toString(),"", ConstantsUtil.TOKEN_EXPIRES_HOUSE);
            resultMap.put("access_token",token);
            resultMap.put("code", TokenCodeEnums.TOKEN_SUCCESS.getCode());
        }catch (Exception e){
            LOGGER.error("获取token失败!---Exception---{}",e);
            return Response.fail(e.getMessage(),resultMap);
        }
        return Response.success("获取token成功",resultMap);
    }
}