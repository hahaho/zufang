package com.apass.zufang.rbac;

import com.apass.gfb.framework.annotation.MyBatisRepository;
import com.apass.gfb.framework.mybatis.support.BaseMybatisRepository;
import com.apass.zufang.domain.entity.rbac.MenusDO;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 
 * @description menu repository
 *
 * @author lixining
 * @version $Id: MenusRepository.java, v 0.1 2016年6月22日 上午11:12:33 lixining Exp $
 */
@MyBatisRepository
public class MenusRepository extends BaseMybatisRepository<MenusDO, Long> {

    /**
     * Select Available menus
     * 
     * @return List<MenusDO>
     */
    public List<MenusDO> selectAvailableMenus(String username, Long parentId) {
        String sqlScript = this.getSQL("selectAvailableMenus");
        Map<String, String> maps = Maps.newHashMap();
        maps.put("username", username);
        maps.put("parentId", String.valueOf(parentId));
        List<MenusDO> resultList = this.getSqlSession().selectList(sqlScript);
        if (CollectionUtils.isEmpty(resultList)) {
            return null;
        }
        for (MenusDO menusDO : resultList) {
            menusDO.setChildren(selectAvailableMenus(username, menusDO.getId()));
        }
        return resultList;
    }

    /**
     * Select All menus
     * 
     * @return List<MenusDO>
     */
    public List<MenusDO> selectAllMenus(Long parentId, String menuName) {
        MenusDO tempMenusDO = new MenusDO();
        tempMenusDO.setParentId(parentId);
        //TODO 根目录root--->-1
        if (parentId==-1l) {
            tempMenusDO.setText(menuName);
        }
        List<MenusDO> resultList = filter(tempMenusDO);
        if (CollectionUtils.isEmpty(resultList)) {
            return null;
        }
        for (MenusDO menusDO : resultList) {
            menusDO.setChildren(selectAllMenus(menusDO.getId(), menuName));
        }
        return resultList;
    }

    /**
     * 根据菜单名称 + neId过滤
     */
    public List<MenusDO> filter(String text, Long neId) {
        MenusDO tempMenusDO = new MenusDO();
        tempMenusDO.setText(text);
        tempMenusDO.setNeId(neId);
        return filter(tempMenusDO);
    }

    public void deleteRoleMenuByMenuId(Long menuId) {
        getSqlSession().delete(getSQL("deleteRoleMenuByMenuId"), menuId);
    }
}
