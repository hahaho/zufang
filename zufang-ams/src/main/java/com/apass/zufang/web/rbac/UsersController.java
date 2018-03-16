package com.apass.zufang.web.rbac;

import com.apass.gfb.framework.exception.BusinessException;
import com.apass.gfb.framework.mybatis.page.Page;
import com.apass.gfb.framework.security.toolkit.SpringSecurityUtils;
import com.apass.gfb.framework.utils.BaseConstants.CommonCode;
import com.apass.gfb.framework.utils.HttpWebUtils;
import com.apass.gfb.framework.utils.RegExpUtils;
import com.apass.zufang.domain.Response;
import com.apass.zufang.domain.entity.Apartment;
import com.apass.zufang.domain.entity.rbac.RolesDO;
import com.apass.zufang.domain.entity.rbac.UsersDO;
import com.apass.zufang.service.house.ApartHouseService;
import com.apass.zufang.service.rbac.UsersService;
import com.apass.zufang.utils.PaginationManage;
import com.apass.zufang.utils.ResponsePageBody;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

/**
 * 
 * @description 用户管理
 *
 * @author lixining
 * @version $Id: UsersController.java, v 0.1 2016年6月22日 上午11:15:57 lixining Exp
 *          $
 */
@Path("/application/rbac/user")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class UsersController {
	private static final Logger LOG = LoggerFactory.getLogger(UsersController.class);
	@Autowired
	private UsersService usersService;
	@Autowired
	private ApartHouseService apartHouseService;

	private static final String USER_PAGE = "rbac/users-page";

	private static final String SUCCESS = "success";
	private static final String USERNAME = "username";
	private static final String RESETPWD_FAIL = "重置密码失败";
	private static final String USER_ID = "userId";
	private static final String APARTMENT_CODE = "apartmentCode";
	private static final String LOADROLES_FAIL = "加载可用角色列表失败";

	/**
	 * 用户管理页面
	 */
	@POST
	@Path("/page")
	public String handleUsersPage() {
		return USER_PAGE;
	}

	/**
	 * 分页列表JSON
	 */
	@POST
	@Path("/pagelist")
	public ResponsePageBody<UsersDO> handlePageList(Map<String,String> paramMap) {
		ResponsePageBody<UsersDO> respBody = new ResponsePageBody<UsersDO>();
		try {
			// 分页参数
			// 页码
			String pageNo = paramMap.get("page");
			// 每页显示条数
			String pageSize = paramMap.get("rows");
			Integer pageNoNum = Integer.parseInt(pageNo);
			Integer pageSizeNum = Integer.parseInt(pageSize);
			Page page = new Page();
			page.setPage(pageNoNum <= 0 ? 1 : pageNoNum);
			page.setLimit(pageSizeNum <= 0 ? 1 : pageSizeNum);

			// 查询传递的参数
			// 用户帐号
			String username = paramMap.get(USERNAME);
			// 用户真实姓名
			String realName = paramMap.get("realName");
			UsersDO paramDO = new UsersDO();
			paramDO.setUserName(username);
			paramDO.setRealName(realName);

			// 调用service层分页查询
			PaginationManage<UsersDO> pagination = usersService.page(paramDO, page);
			respBody.setTotal(pagination.getTotalCount());
			respBody.setRows(pagination.getDataList());
			respBody.setStatus(CommonCode.SUCCESS_CODE);
		} catch (Exception e) {
			LOG.error("用户列表查询失败", e);
			respBody.setMsg("用户列表查询失败");
		}
		return respBody;
	}

	/**
	 * 重置密码
	 */
	@POST
	@Path("/resetpwd")
	public Response handleResetPassword(Map<String,String> paramMap) {
		try {
			String oldpassword = paramMap.get("oldpassword");
			String newpassword = paramMap.get("newpassword");
			String renewpassword = paramMap.get("renewpassword");
			if (StringUtils.isAnyBlank(oldpassword, newpassword, renewpassword)) {
				return Response.fail("旧密码、新密码、确认密码不能为空");
			}
			if (!StringUtils.equals(newpassword, renewpassword)) {
				return Response.fail("新密码和确认新密码不一致");
			}
			String username = SpringSecurityUtils.getCurrentUser();
			usersService.resetpassword(username, oldpassword, newpassword);
			return Response.success(SUCCESS);
		} catch (BusinessException e) {
			LOG.error(e.getErrorDesc(), e);
			return Response.fail(e.getErrorDesc());
		} catch (Exception e) {
			LOG.error(RESETPWD_FAIL, e);
			return Response.fail(RESETPWD_FAIL);
		}
	}

	/**
	 * 重置密码
	 */
	@POST
	@Path("/forceresetpwd")
	public Response handleForceResetPassword(Map<String,String> paramMap) {
		try {
			String username = paramMap.get(USERNAME);
			String newpassword = paramMap.get("newpassword");
			String renewpassword = paramMap.get("renewpassword");
			if (StringUtils.isAnyBlank(username, newpassword, renewpassword)) {
				return Response.fail("用户账号、新密码、确认密码不能为空");
			}
			if (!RegExpUtils.length(newpassword, 1, 50)) {
				return Response.fail("密码长度不合法");
			}
			if (!StringUtils.equals(newpassword, renewpassword)) {
				return Response.fail("新密码和确认新密码不一致");
			}
			usersService.forceResetpassword(username, newpassword);
			return Response.success(SUCCESS);
		} catch (Exception e) {
			LOG.error(RESETPWD_FAIL, e);
			return Response.fail(RESETPWD_FAIL);
		}
	}

	/**
	 * 加载基本信息
	 */
	@POST
	@Path("/loadbasic")
	public Response handleLoadBasic() {
		try {
			return Response.success(SUCCESS, usersService.loadBasicInfo());
		} catch (Exception e) {
			LOG.error("基本信息加载失败", e);
			return Response.fail("基本信息加载失败");
		}
	}

	/**
	 * 保存基本信息
	 */
	@POST
	@Path("/savebasic")
	public Response handleSaveBasic(Map<String,String> paramMap) {
		try {
			String realName = paramMap.get("realname");
			String mobile = paramMap.get("mobile");
			String email = paramMap.get("email");
			if (!RegExpUtils.length(realName, 1, 100)) {
				return Response.fail("真实姓名长度不合法");
			}
			if (StringUtils.isNotBlank(mobile) && !RegExpUtils.mobile(mobile)) {
				return Response.fail("手机号不合法");
			}
			if (!RegExpUtils.length(email, 0, 50)) {
				return Response.fail("邮箱长度不合法");
			}
			UsersDO usersDO = new UsersDO();
			usersDO.setRealName(realName);
			usersDO.setMobile(mobile);
			usersDO.setEmail(email);
			usersService.saveBasicInfo(usersDO);
			return Response.success(SUCCESS);
		} catch (Exception e) {
			LOG.error("load basic fail", e);
			return Response.fail("基本信息加载失败");
		}
	}

	/**
	 * 保存用户
	 */
	@POST
	@Path("/save")
	public Response handleSave(Map<String,String> paramMap) {
		try {
			String username = paramMap.get(USERNAME);
			String password = paramMap.get("password");
			String repassword = paramMap.get("repassword");
			String realName = paramMap.get("realName");
			String mobile = paramMap.get("mobile");
			String email = paramMap.get("email");
			if (!RegExpUtils.length(username, 1, 50)) {
				return Response.fail("用户账号长度不合法");
			}
			if (!RegExpUtils.length(password, 1, 50)) {
				return Response.fail("密码长度不合法");
			}
			if (!StringUtils.equals(password, repassword)) {
				return Response.fail("密码和确认密码不一致");
			}
			if (!RegExpUtils.length(realName, 1, 100)) {
				return Response.fail("真实姓名长度不合法");
			}
			if (StringUtils.isNotBlank(mobile) && !RegExpUtils.mobile(mobile)) {
				return Response.fail("手机号不合法");
			}
			if (!RegExpUtils.length(email, 0, 50)) {
				return Response.fail("邮箱长度不合法");
			}
			UsersDO usersDO = new UsersDO();
			usersDO.setUserName(username);
			usersDO.setPassword(new BCryptPasswordEncoder().encode(password));
			usersDO.setRealName(realName);
			usersDO.setMobile(mobile);
			usersDO.setEmail(email);
			usersService.save(usersDO);
			return Response.success(SUCCESS);
		} catch (BusinessException e) {
			LOG.error(e.getErrorDesc(), e);
			return Response.fail(e.getErrorDesc());
		} catch (DuplicateKeyException e) {
			LOG.error("手机号码已存在", e);
			return Response.fail("手机号码已存在");
		} catch (Exception e) {
			LOG.error("保存用户失败", e);
			return Response.fail("保存用户记录失败");
		}
	}

	/**
	 * 删除用户
	 */
	@POST
	@Path("/delete")
	public Response handleDelete(Map<String,String> paramMap) {
		try {
			String userId = paramMap.get(USER_ID);
			if (StringUtils.isBlank(userId)) {
				return Response.fail("要删除的用户ID不能为空");
			}
			usersService.delete(userId);
			return Response.success(SUCCESS);
		} catch (BusinessException e) {
			LOG.error(e.getErrorDesc(), e);
			return Response.fail(e.getErrorDesc());
		} catch (Exception e) {
			LOG.error("delete user fail", e);
			return Response.fail("用户信息删除失败");
		}
	}

	/**
	 * 可用角色
	 */
	@POST
	@Path("/load/available/roles")
	public Response handleLoadAvailableRoles(Map<String,String> paramMap) {
		try {
			String userId = paramMap.get(USER_ID);
			if (StringUtils.isBlank(userId)) {
				return null;
			}
			List<RolesDO> roleList = usersService.loadAvailableRoles(userId);
			return Response.success(SUCCESS, roleList);
		} catch (Exception e) {
			LOG.error(LOADROLES_FAIL, e);
			return Response.fail(LOADROLES_FAIL);
		}
	}

	/**
	 * 已经分配角色
	 */
	@POST
	@Path("/load/assigned/roles")
	public Response handleLoadAssignedRoles(Map<String,String> paramMap) {
		try {
			String userId = paramMap.get(USER_ID);
			if (StringUtils.isBlank(userId)) {
				return null;
			}
			List<RolesDO> roleList = usersService.loadAssignedRoles(userId);
			return Response.success(SUCCESS, roleList);
		} catch (Exception e) {
			LOG.error(LOADROLES_FAIL, e);
			return Response.fail(LOADROLES_FAIL);
		}
	}

	/**
	 * 保存用户角色设置
	 */
	@POST
	@Path("/save/assigned/roles")
	public Response handleSaveAssignedRoles(Map<String,String> paramMap) {
		try {
			String userId = paramMap.get(USER_ID);
			String roles = paramMap.get("roles");
			if (StringUtils.isBlank(userId)) {
				return Response.fail("用户ID不能为空");
			}
			usersService.saveAssignedRoles(userId, roles);
			return Response.success(SUCCESS);
		} catch (Exception e) {
			LOG.error("保存用户角色分配记录失败", e);
			return Response.fail("保存用户角色记录失败");
		}
	}

	/**
	 * 加载所有公寓信息
	 */
	@POST
	@Path("/apartmentList")
	public Response locadAllApartment(){
		try{
			Map<String,String> paramMap = Maps.newHashMap();
			paramMap.put("isDelete","00");
			List<Apartment> apartList = apartHouseService.listAllValidApartment(paramMap);
			return Response.success("查询公寓列表成功！",apartList);
		}catch (Exception e){
			LOG.error("查询公寓列表失败,-----Exception---->{}",e);
			return Response.fail("查询公寓列表失败!");
		}
	}

	/**
	 * 用户关联
	 */
	@POST
	@Path("/relevanceApart")
	public Response relevanceApart(Map<String,String> paramMap){
		try{
			UsersDO usersDO = new UsersDO();
			String userId = paramMap.get(USER_ID);
			String apartmentCode = paramMap.get(APARTMENT_CODE);
			usersDO.setId(Long.valueOf(userId));
			usersDO.setApartmentCode(apartmentCode);
			usersDO.setUpdatedBy(SpringSecurityUtils.getCurrentUser());

			usersService.relevanceApart(usersDO);

			return Response.success("用户关联公寓成功！","");
		}catch (Exception e){
			LOG.error("用户关联公寓失败,-----Exception---->{}",e);
			return Response.fail("用户关联公寓失败!");
		}
	}




}
