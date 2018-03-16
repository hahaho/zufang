package com.apass.zufang.rbac;

import com.apass.gfb.framework.annotation.MyBatisRepository;
import com.apass.gfb.framework.mybatis.support.BaseMybatisRepository;
import com.apass.zufang.domain.entity.rbac.PermissionsDO;

import java.util.List;

/**
 * 
 * @description Permissions Repository
 *
 * @author lixining
 * @version $Id: PermissionsRepository.java, v 0.1 2016年6月22日 上午11:13:19 lixining Exp $
 */
@MyBatisRepository
public class PermissionsRepository extends BaseMybatisRepository<PermissionsDO, Long> {

    /**
     * 删除角色权限表中的资源记录
     */
    public void deleteRolePermissionsByPermissionId(Long permissionId) {
        String sql = this.getSQL("deleteRolePermissionsByPermissionId");
        this.getSqlSession().delete(sql, permissionId);
    }

    /**
     * 删除角色权限表中的资源记录
     */
    public List<PermissionsDO> filter(String permissionCode, Long neId) {
        PermissionsDO tempPermission = new PermissionsDO();
        tempPermission.setPermissionCode(permissionCode);
        tempPermission.setNeId(neId);
        return this.filter(tempPermission);
    }
}
