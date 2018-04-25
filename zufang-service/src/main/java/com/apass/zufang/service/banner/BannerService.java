package com.apass.zufang.service.banner;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apass.gfb.framework.exception.BusinessException;
import com.apass.gfb.framework.utils.BaseConstants;
import com.apass.zufang.domain.dto.BannerQueryParams;
import com.apass.zufang.domain.entity.Banner;
import com.apass.zufang.domain.enums.IsDeleteEnums;
import com.apass.zufang.domain.vo.BannerVo;
import com.apass.zufang.mapper.zfang.BannerMapper;
import com.apass.zufang.service.apartment.ImageService;
import com.apass.zufang.utils.ResponsePageBody;

@Service
public class BannerService {

    @Autowired
    private BannerMapper bannerDao;
    
    @Autowired
    private ImageService imgService;

    /*** banner信息管理列表 
     * @throws BusinessException */
	public ResponsePageBody<BannerVo> getBannerListsExceptDelete(BannerQueryParams dto) throws BusinessException{
		ResponsePageBody<BannerVo> body = new ResponsePageBody<>();
		dto.setIsDelete(IsDeleteEnums.IS_DELETE_00.getCode());
		List<BannerVo> houseList = bannerDao.getBannerLists(dto);
		for (BannerVo bannerVo : houseList) {
			bannerVo.setFullImgUrl(imgService.getComplateUrl(bannerVo.getImgUrl()));
		}
		body.setRows(houseList);
		body.setTotal(bannerDao.getBannerListsCount(dto));
		body.setStatus(BaseConstants.CommonCode.SUCCESS_CODE);
		return body;
	}

	@Transactional(value="transactionManager",rollbackFor = { Exception.class,RuntimeException.class})
	public void saveOrUpdateBanner(BannerVo banner) throws BusinessException{
		
		if(null == banner || StringUtils.isBlank(banner.getType())){
			throw new BusinessException("banner信息有误!");
		}
		
		/*** 1.首先根据Banner type 判断，此类型下的banner 数量是否达到5个，否则直接返回*/
		List<Banner> banners = getBannersByType(banner.getType());
		if(CollectionUtils.size(banners) >= 5){
			throw new BusinessException("该banner类型下banner数量已达上限！");
		}
		
		updateBannerSort(banners, banner);
		
		saveOrupdate(banner);//新增或者修改banner
	}
	
	/**
	 * 根据类型和排序和Id，查询出对应的banner,如果存在则说明sort有冲突，
	 * 把之前的sort和比sort大的sort字段都加1
	 * @param banners
	 * @param banner
	 */
	@Transactional(value="transactionManager",rollbackFor = { Exception.class,RuntimeException.class})
	public void updateBannerSort(List<Banner> banners,BannerVo banner){
		BannerQueryParams params  = new BannerQueryParams();
		params.setBannerType(banner.getType());
		params.setIsDelete(IsDeleteEnums.IS_DELETE_00.getCode());
		params.setId(banner.getId());
		params.setSort(banner.getSort());
		List<Banner> sameSortBanner = bannerDao.loadIndexBanners(params);
		
		if(CollectionUtils.isNotEmpty(sameSortBanner)){
			for (Banner ban : banners) {
				if(ban.getBannerSort() >= Long.parseLong(banner.getType())){
					ban.setBannerSort(ban.getBannerSort() + 1);
					ban.setUpdatedTime(new Date());
					ban.setUpdateUser(banner.getOperationName());
					bannerDao.updateByPrimaryKeySelective(ban);
				}
			}
		}
	}
	
	/**
	 * 根据type查询banner
	 * @param type
	 * @return
	 */
	public List<Banner> getBannersByType(String type){
		BannerQueryParams params  = new BannerQueryParams();
		params.setBannerType(type);
		params.setIsDelete(IsDeleteEnums.IS_DELETE_00.getCode());
		List<Banner> banners = bannerDao.loadIndexBanners(params);
		return banners;
	}
	
	/**
	 * 新增或者修改Banner
	 * @param banner
	 */
	@Transactional(value="transactionManager",rollbackFor = { Exception.class,RuntimeException.class})
	public void saveOrupdate(BannerVo banner){
		//新增
		Banner record = new Banner();
		Date date = new Date();
		record.setActivityUrl(banner.getActivityUrl());
		record.setBannerImgUrl(banner.getImgUrl());
		record.setBannerType(new Byte(banner.getType()));
		record.setBannerSort(Long.parseLong(banner.getSort()));
		record.setUpdatedTime(date);
		record.setUpdateUser(banner.getOperationName());
		String bannerName = banner.getImgUrl().substring(banner.getImgUrl().lastIndexOf("/")+1);
		record.setBannerName(bannerName);
		
		if(StringUtils.isBlank(banner.getId())){
			record.setCreatedTime(date);
			record.setCreateUser(banner.getOperationName());
			bannerDao.insertSelective(record);
			return;
		}
		record.setId(Long.parseLong(banner.getId()));
		bannerDao.updateByPrimaryKeySelective(record);
	}
    /**
     * 删除banner(逻辑删除)
     */
	@Transactional(value="transactionManager",rollbackFor = { Exception.class,RuntimeException.class})
	public void deleteBanner(String id,String updateUser) throws BusinessException{
		if(StringUtils.isBlank(id)){
			throw new BusinessException("bannerId不能为空!");
		}
		Banner banner = bannerDao.selectByPrimaryKey(Long.parseLong(id));
		if(null == banner){
			throw new BusinessException("id为{}的banner的不存在!",id);
		}
		banner.setUpdatedTime(new Date());
		banner.setUpdateUser(updateUser);
		banner.setIsDelete(IsDeleteEnums.IS_DELETE_01.getCode());
		bannerDao.updateByPrimaryKeySelective(banner);
		
	}

}
