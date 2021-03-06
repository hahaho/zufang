package com.apass.zufang.service.rbac;

import com.apass.gfb.framework.exception.BusinessException;
import com.apass.gfb.framework.mybatis.page.Page;
import com.apass.gfb.framework.mybatis.page.Pagination;
import com.apass.gfb.framework.security.toolkit.SpringSecurityUtils;
import com.apass.zufang.domain.entity.rbac.MenusDO;
import com.apass.zufang.rbac.MenusRepository;
import com.apass.zufang.utils.PaginationManage;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 
 * @description Menu Service
 *
 * @author lixining
 * @version $Id: MenusService.java, v 0.1 2016年6月23日 下午1:48:28 lixining Exp $
 */
@Component
public class MenusService {

    @Autowired
    private MenusRepository menusRepository;

    /**
     * Select user Permit Tree Menu Json
     */
    public List<MenusDO> selectPermitTreeJson() {
        String securityUserId = SpringSecurityUtils.getCurrentUser();
        return menusRepository.selectAvailableMenus(securityUserId, -1l);
    }

    /**
     * Menus Page
     */
    public PaginationManage<MenusDO> page(MenusDO paramDO, Page page) {
        PaginationManage<MenusDO> result = new PaginationManage<MenusDO>();
        Pagination<MenusDO> response = menusRepository.page(paramDO, page);
        result.setDataList(response.getDataList());
        result.setPageInfo(page.getPageNo(), page.getPageSize());
        result.setTotalCount(response.getTotalCount());
        return result;
    }

    /**
     * All Menus
     */
    public List<MenusDO> selectAllMenuTreeJson(String menuName) {
        if (StringUtils.isNotBlank(menuName)) {
            return menusRepository.selectAllMenus(-1l, menuName);
        }
        MenusDO rootMenu = new MenusDO();
        rootMenu.setChildren(menusRepository.selectAllMenus(-1l, null));
        rootMenu.setText("后台管理系统菜单树");
        rootMenu.setId(-1l);
        List<MenusDO> resultList = Lists.newArrayList();
        resultList.add(rootMenu);
        return resultList;
    }

    /**
     * 保存菜单数据
     */
    public void save(MenusDO menusDO) throws BusinessException {
        Long id = menusDO.getId();
        String operator = SpringSecurityUtils.getCurrentUser();

        String text = menusDO.getText();
        if (id == null) {
            menusDO.setCreatedBy(operator);
            menusDO.setUpdatedBy(operator);
            menusRepository.insert(menusDO);
            return;
        }
        MenusDO menusDB = menusRepository.select(id);
        if (menusDB == null) {
            throw new BusinessException("记录不存在,无法更新, 请刷新列表后重试");
        }
        menusDB.setUpdatedBy(operator);
        menusDB.setText(menusDO.getText());
        menusDB.setUrl(menusDO.getUrl());
        menusDB.setParentId(menusDO.getParentId());
        menusDB.setDisplay(menusDO.getDisplay());
        menusRepository.updateAll(menusDB);
    }

    /**
     * 加载菜单数据
     */
    public MenusDO select(Long menuId) {
        return menusRepository.select(menuId);
    }

    /**
     * 删除菜单数据
     */
    @Transactional(value="transactionManager",rollbackFor = { Exception.class,RuntimeException.class})
    public void delete(Long menuId) {
        menusRepository.deleteRoleMenuByMenuId(menuId);
        menusRepository.delete(menuId);
        MenusDO tempMenuDO = new MenusDO();
        tempMenuDO.setParentId(menuId);
        List<MenusDO> subMenuList = menusRepository.filter(tempMenuDO);
        if (CollectionUtils.isEmpty(subMenuList)) {
            return;
        }
        for (MenusDO menu : subMenuList) {
            delete(menu.getId());
        }
    }

}
