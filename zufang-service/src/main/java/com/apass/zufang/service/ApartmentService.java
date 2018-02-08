package com.apass.zufang.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
	public Integer createEntity(Apartment entity){
		return apartmentMapper.insertSelective(entity);
	}
	public Apartment readEntity(Apartment entity){
		return apartmentMapper.selectByPrimaryKey(entity.getId());
	}
	public Apartment readEntity(Long id){
		return apartmentMapper.selectByPrimaryKey(id);
	}
	public Integer updateEntity(Apartment entity){
		return apartmentMapper.updateByPrimaryKeySelective(entity);
	}
	public Integer deleteEntity(Apartment entity){
		return apartmentMapper.deleteByPrimaryKey(entity.getId());
	}
	public Integer deleteEntity(Long id){
		return apartmentMapper.deleteByPrimaryKey(id);
	}
	public ResponsePageBody<Apartment> getApartmentList(Apartment entity) {
		return null;
	}
	public Response addApartment(Apartment entity,String username) {
		return Response.success("公寓信息新增成功！");
	}
}
