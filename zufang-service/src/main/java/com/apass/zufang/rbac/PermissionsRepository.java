package com.apass.zufang.rbac;
import com.apass.gfb.framework.annotation.MyBatisRepository;
import com.apass.gfb.framework.mybatis.support.BaseMybatisRepository;
import com.apass.zufang.domain.entity.rbac.PermissionsDO;
import java.util.List;
/**
 * 资源管理
 * @author Administrator
 *
 */
@MyBatisRepository
public class PermissionsRepository extends BaseMybatisRepository<PermissionsDO, Long> {
    /**
     * 删除角色权限表中的资源记录   旧方法弃用
     */
    public void deleteRolePermissionsByPermissionId(Long id) {
        String sql = this.getSQL("deleteRolePermissionsByPermissionId");
        this.getSqlSession().delete(sql, id);
    }
    /**
     * 删除角色权限表中的资源记录
     * @param id
     */
    public void updateRolePermissionsByPermissionId(Long id) {
        String sql = this.getSQL("updateRolePermissionsByPermissionId");
        this.getSqlSession().update(sql, id);
    }
    /**
     * 删除角色权限表中的资源记录
     */
    public List<PermissionsDO> filter(String permissionCode, Long neId) {
        PermissionsDO tempPermission = new PermissionsDO();
        tempPermission.setPermissionCode(permissionCode);
        tempPermission.setNeId(neId);
        tempPermission.setIsDelete("00");
        return this.filter(tempPermission);
    }
}