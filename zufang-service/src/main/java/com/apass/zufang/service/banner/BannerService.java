package com.apass.zufang.service.banner;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apass.gfb.framework.mybatis.page.Page;
import com.apass.gfb.framework.mybatis.page.Pagination;
import com.apass.zufang.domain.entity.Banner;
import com.apass.zufang.domain.vo.BannerVo;
import com.apass.zufang.mapper.zfang.BannerMapper;
import com.apass.zufang.utils.PaginationManage;

@Service
public class BannerService {

    @Autowired
    private BannerMapper bannerDao;

    public List<Banner> loadIndexBanners(String type) {
        return bannerDao.loadIndexBanners(type);
    }

    /**
     * 后台加载banner信息
     * @return
     */
//    public PaginationManage<Banner> loadBanners(Map<String, Object> map, Page page) {
//        PaginationManage<Banner> result = new PaginationManage<Banner>();
//        Pagination<Banner> response = bannerDao.loadBanners(map, page);
//        for(Banner bn : response.getDataList()){
//            if(BannerType.BANNER_INDEX.getIdentify().equals(bn.getBannerType())
//                || BannerType.BANNER_SIFT.getIdentify().equals(bn.getBannerType())){
//                bn.setBannerType(BannerType.getEnum(bn.getBannerType()).getMessage());
//            } else {
//                String[] arrs = bn.getBannerType().split("_");
//                bn.setBannerType(cateService.getCategoryById(Long.valueOf(arrs[1])).getCategoryName());
//            }
//        }
//        result.setDataList(response.getDataList());
//        result.setPageInfo(page.getPageNo(), page.getPageSize());
//        result.setTotalCount(response.getTotalCount());
//        return result;
//    }

    /**
     * 通过id查询banner信息
     * @param id
     * @return
     */
    public Banner selectById(Long id) {
        return bannerDao.selectByPrimaryKey(id);
    }

    /**
     * 后台新增banner信息
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer addBannerInfor(Banner entity) {
        return bannerDao.insertSelective(entity);
    }

    public Integer update(Banner entity){
        return bannerDao.updateByPrimaryKeySelective(entity);
    }

    /**
     * 删除
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteBannerInfor(Long id) {
        return bannerDao.deleteByPrimaryKey(id);
    }

    /**
     * 新增
     * @param entity
     */
    @Transactional(rollbackFor = Exception.class)
    public void insert(Banner entity) {
        bannerDao.insert(entity);
    }

    /**
     * 删除
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        bannerDao.deleteByPrimaryKey(id);
    }

    //查询所有banner图
//    public List<Banner> loadBannersList(Map<String, Object> map) {
//        return bannerDao.loadBannersList(map);
//    }
    
    /**
     * 通过activityId查询banner信息
     * @param id
     * @return
     */
    public List<Banner> getActivityUrlLikeActivityId(String activityId) {
        return bannerDao.getActivityUrlLikeActivityId(activityId);
    }
    
    public BannerVo getBannerVoLikeActivityId(String activityId){
    	List<Banner> bannerList = getActivityUrlLikeActivityId("%?activityId="+activityId);
    	Banner banner = null;
    	if(CollectionUtils.isNotEmpty(bannerList)){
    		banner = bannerList.get(0);
    	}
    	return getBannerPoToVo(banner);
    }
    
    public BannerVo getBannerPoToVo(Banner banner){
    	BannerVo vo = new BannerVo();
    	if(null != banner){
    		vo.setActivityUrl(banner.getActivityUrl().replace("ajqh://cn.apass.ajqh/web?url=", ""));
    		vo.setId(banner.getId());
    		vo.setName(banner.getBannerName());
    	}
    	return vo;
    }
}
