package com.apass.zufang.service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apass.gfb.framework.utils.BaseConstants;
import com.apass.zufang.domain.Response;
import com.apass.zufang.domain.entity.Apartment;
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
	@Transactional(rollbackFor = { Exception.class})
	public Integer createEntity(Apartment entity){
		return apartmentMapper.insertSelective(entity);
	}
	public Apartment readEntity(Apartment entity){
		return apartmentMapper.selectByPrimaryKey(entity.getId());
	}
	public Apartment readEntity(Long id){
		return apartmentMapper.selectByPrimaryKey(id);
	}
	@Transactional(rollbackFor = { Exception.class})
	public Integer updateEntity(Apartment entity){
		return apartmentMapper.updateByPrimaryKeySelective(entity);
	}
	@Transactional(rollbackFor = { Exception.class})
	public Integer deleteEntity(Apartment entity){
		return apartmentMapper.deleteByPrimaryKey(entity.getId());
	}
	@Transactional(rollbackFor = { Exception.class})
	public Integer deleteEntity(Long id){
		return apartmentMapper.deleteByPrimaryKey(id);
	}
	public ResponsePageBody<Apartment> getApartmentList(Apartment entity) {
		entity.setIsDelete("00");
		ResponsePageBody<Apartment> pageBody = new ResponsePageBody<Apartment>();
        Integer count = apartmentMapper.getApartmentListCount(entity);
        List<Apartment> response = apartmentMapper.getApartmentList(entity);
        pageBody.setTotal(count);
        pageBody.setRows(response);
        pageBody.setStatus(BaseConstants.CommonCode.SUCCESS_CODE);
        return pageBody;
	}
	@Transactional(rollbackFor = { Exception.class})
	public Response addApartment(Apartment entity,String username) {
		return Response.success("公寓信息新增成功！");
	}
}
