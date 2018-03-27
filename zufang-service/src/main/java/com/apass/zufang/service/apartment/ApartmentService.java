package com.apass.zufang.service.apartment;
import java.util.List;
import java.util.Map;

import com.apass.zufang.domain.entity.rbac.UsersDO;
import com.apass.zufang.rbac.UsersRepository;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.apass.gfb.framework.exception.BusinessException;
import com.apass.gfb.framework.utils.BaseConstants;
import com.apass.zufang.domain.Response;
import com.apass.zufang.domain.dto.ApprintmentQueryParams;
import com.apass.zufang.domain.entity.Apartment;
import com.apass.zufang.domain.vo.ApartmentVo;
import com.apass.zufang.mapper.zfang.ApartmentMapper;
import com.apass.zufang.utils.ResponsePageBody;
/**
 * 公寓管理
 * @author Administrator
 *
 */
@Service
public class ApartmentService {
	@Autowired
	private ApartmentMapper apartmentMapper;
	@Autowired
	public ImageService imageService;
	@Autowired
	private UsersRepository usersRepository;

	@Transactional(value="transactionManager",rollbackFor = { Exception.class,RuntimeException.class})
	public Integer createEntity(Apartment entity){
		return apartmentMapper.insertSelective(entity);
	}
	public Apartment readEntity(Long id){
		return apartmentMapper.selectByPrimaryKey(id);
	}
	@Transactional(value="transactionManager",rollbackFor = { Exception.class,RuntimeException.class})
	public Integer updateEntity(Apartment entity){
		return apartmentMapper.updateByPrimaryKeySelective(entity);
	}
	@Transactional(value="transactionManager",rollbackFor = { Exception.class,RuntimeException.class})
	public Integer deleteEntity(Long id){
		return apartmentMapper.deleteByPrimaryKey(id);
	}
	/**
	 * 公寓信息列表查询
	 * @param entity
	 * @return
	 * @throws BusinessException 
	 */
	public ResponsePageBody<ApartmentVo> getApartmentList(ApprintmentQueryParams entity) throws BusinessException {
		ResponsePageBody<ApartmentVo> pageBody = new ResponsePageBody<ApartmentVo>();
        Integer count = apartmentMapper.getApartmentListCount(entity);
        List<ApartmentVo> list = apartmentMapper.getApartmentList(entity);
        for(ApartmentVo en : list){
        	en.setFullCompanyLogo(imageService.getComplateUrl(en.getCompanyLogo()));
        }
        pageBody.setTotal(count);
        pageBody.setRows(list);
        pageBody.setStatus(BaseConstants.CommonCode.SUCCESS_CODE);
        return pageBody;
	}
	/**
	 * 公寓信息新增
	 * @param entity
	 * @param username
	 * @return
	 * @throws BusinessException 
	 */
	@Transactional(value="transactionManager",rollbackFor = { Exception.class,RuntimeException.class})
	public Response addApartment(Apartment entity,String username,String code) throws BusinessException {
		ApprintmentQueryParams entity2 = new ApprintmentQueryParams();
		entity2.setName(entity.getName());
		entity2.setCode(code);
		entity2.setIsDelete("00");
		List<ApartmentVo> count1 = apartmentMapper.getApartmentListNameCount(entity2);
		if(count1!=null&&count1.size()>0){
			return Response.fail("公寓信息新增失败,公寓名称不可重复！");
		}
		Integer count2 = apartmentMapper.getApartmentListCodeCount(entity2);
		count2++;
		String cou = null;
		if(count2<10){
			cou = "00"+count2;
		}else if(count2>9&&count2<100){
			cou = "0"+count2;
		}else{
			cou = count2.toString();
		}
		entity.fillAllField(username);
		entity.setCode(code+cou);
		if(createEntity(entity)==1){
			return Response.success("公寓信息新增成功！");
		}
		return Response.fail("公寓信息新增失败！");
	}
	/**
	 * 公寓信息修改
	 * @param entity
	 * @param username
	 * @return
	 */
	@Transactional(value="transactionManager",rollbackFor = { Exception.class,RuntimeException.class})
	public Response editApartment(Apartment entity,String username) {
		ApprintmentQueryParams entity2 = new ApprintmentQueryParams();
		entity2.setName(entity.getName());
		entity2.setIsDelete("00");
		List<ApartmentVo> count1 = apartmentMapper.getApartmentListNameCount(entity2);
		for(ApartmentVo vo : count1){
			if(vo.getId().equals(entity.getId())){
				continue;
			}
			if(StringUtils.equals(vo.getName(), entity.getName())){
				return Response.fail("公寓信息修改失败,公寓名称不可重复！");
			}
		}
		entity.fillField(username);
		if(updateEntity(entity)==1){
			return Response.success("公寓信息修改成功！");
		}
		return Response.fail("公寓信息修改失败！");
	}
	/**
	 * 新增/修改房源专用   用于新增公寓下属房源
	 * 根据登录人信息获取该个人归属公寓信息  
	 * @param username
	 * @return
	 * @throws BusinessException 
	 */
	public Long getApartmentByCurrentUser(String username) throws BusinessException {
		if(StringUtils.isBlank(username)){
			throw new BusinessException("登录人为空，或者登录人未查找到公寓信息！");
		}
        UsersDO usersDO = usersRepository.selectByUsername(username);//此处判断用户是否和公寓发生关联
        if(StringUtils.isBlank(usersDO.getApartmentCode())){
        	throw new BusinessException("该用户未关联公寓!");
        }
        Map<String,String> parmMap = Maps.newHashMap();
        parmMap.put("code",usersDO.getApartmentCode());
        List<Apartment> apartments = apartmentMapper.listAllValidApartment(parmMap);//此处根据用户表中的公寓code反查，公寓是否存在（可能被删或者其他的数据错误）
        if(CollectionUtils.isEmpty(apartments)){
            throw new BusinessException("该用户未关联公寓!");
        }
        if(apartments.size() > 1){
            throw new BusinessException("用户关联公寓数据有误!");
        }
        return apartments.get(0).getId();
	}
	
	/**
	 * 根据当前登录的用户名，查询对应的公寓Code
	 * @param username
	 * @return
	 * @throws BusinessException
	 */
	public String getApartmentCodeByCurrentUser(String username) throws BusinessException{
		if(StringUtils.isBlank(username)){
			throw new BusinessException("登录人为空，或者登录人未查找到公寓信息！");
		}
        UsersDO usersDO = usersRepository.selectByUsername(username);
        if(null == usersDO){
        	return null;
        }
		return usersDO.getApartmentCode();
	}
}