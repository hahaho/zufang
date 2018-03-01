package com.apass.zufang.service.apartment;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.apass.gfb.framework.exception.BusinessException;
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
	public Apartment readEntity(Long id){
		return apartmentMapper.selectByPrimaryKey(id);
	}
	@Transactional(rollbackFor = { Exception.class})
	public Integer updateEntity(Apartment entity){
		return apartmentMapper.updateByPrimaryKeySelective(entity);
	}
	@Transactional(rollbackFor = { Exception.class})
	public Integer deleteEntity(Long id){
		return apartmentMapper.deleteByPrimaryKey(id);
	}
	/**
	 * 公寓信息列表查询
	 * @param entity
	 * @return
	 */
	public ResponsePageBody<Apartment> getApartmentList(Apartment entity) {
		ResponsePageBody<Apartment> pageBody = new ResponsePageBody<Apartment>();
        Integer count = apartmentMapper.getApartmentListCount(entity);
        List<Apartment> response = apartmentMapper.getApartmentList(entity);
        pageBody.setTotal(count);
        pageBody.setRows(response);
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
	@Transactional(rollbackFor = { Exception.class})
	public Response addApartment(Apartment entity,String username,String code) throws BusinessException {
		Apartment entity2 = new Apartment();
		entity2.setCode(code);
		Integer count = apartmentMapper.getApartmentListCodeCount(entity);
		String cou = ++count<10?"0"+count:count.toString();
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
	@Transactional(rollbackFor = { Exception.class})
	public Response editApartment(Apartment entity,String username) {
		entity.fillField(username);
		if(updateEntity(entity)==1){
			return Response.success("公寓信息修改成功！");
		}
		return Response.fail("公寓信息修改失败！");
	}
}
