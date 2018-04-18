package com.apass.zufang.service.rbac;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.apass.gfb.framework.exception.BusinessException;
import com.apass.gfb.framework.mybatis.page.Page;
import com.apass.gfb.framework.mybatis.page.Pagination;
import com.apass.gfb.framework.security.toolkit.SpringSecurityUtils;
import com.apass.gfb.framework.utils.BaseConstants;
import com.apass.zufang.domain.Response;
import com.apass.zufang.domain.entity.rbac.PermissionsDO;
import com.apass.zufang.rbac.PermissionsRepository;
import com.apass.zufang.utils.PaginationManage;
import com.apass.zufang.utils.ResponsePageBody;
/**
 * 
 * @description Permission Service
 *
 * @author lixining
 * @version $Id: PermissionsService.java, v 0.1 2016年6月23日 下午3:43:47 lixining Exp $
 */
@Component
public class PermissionsService {
    @Autowired
    private PermissionsRepository permissionsRepository;
    /**
     * 资源分页
     * @param paramDO
     * @param page
     * @return
     */
    public PaginationManage<PermissionsDO> page(PermissionsDO paramDO, Page page) {
        PaginationManage<PermissionsDO> result = new PaginationManage<PermissionsDO>();
        Pagination<PermissionsDO> response = permissionsRepository.page(paramDO, page);
        result.setDataList(response.getDataList());
        result.setPageInfo(page.getPageNo(), page.getPageSize());
        result.setTotalCount(response.getTotalCount());
        return result;
    }
    /**
     * 删除资源ID
     */
    @Transactional(value="transactionManager",rollbackFor = { Exception.class,RuntimeException.class})
    public void delete(String permissionId) {
    	Long id = Long.parseLong(permissionId);
        permissionsRepository.deleteRolePermissionsByPermissionId(id);
        permissionsRepository.delete(id);
    }

    /**
     * 保存资源数据
     */
    public void save(PermissionsDO permission) throws BusinessException {
        Long id = permission.getId();
        String operator = SpringSecurityUtils.getCurrentUser();
        String permissionCode = permission.getPermissionCode();
        List<PermissionsDO> dataList = permissionsRepository.filter(permissionCode, id);
        if (!CollectionUtils.isEmpty(dataList)) {
            throw new BusinessException("资源编码已存在");
        }
        if (id ==null) {
            permission.setCreatedBy(operator);
            permission.setUpdatedBy(operator);
            permissionsRepository.insert(permission);
            return;
        }
        PermissionsDO permissionDB = permissionsRepository.select(id);
        if (permissionDB == null) {
            throw new BusinessException("记录不存在,无法更新, 请刷新列表后重试");
        }
        permissionDB.setUpdatedBy(operator);
        permissionDB.setPermissionCode(permission.getPermissionCode());
        permissionDB.setPermissionName(permission.getPermissionName());
        permissionDB.setDescription(permission.getDescription());
        permissionsRepository.updateAll(permissionDB);
    }
    /**
     * 主键加载
     */
    public PermissionsDO select(Long permissionId) {
        return permissionsRepository.select(permissionId);
    }
    /**
     * 资源分页
     * @param paramDO
     * @param page
     * @return
     */
    public ResponsePageBody<PermissionsDO> getPermissionsList(PermissionsDO paramDO, Page page) {
    	ResponsePageBody<PermissionsDO> result = new ResponsePageBody<PermissionsDO>();
        Pagination<PermissionsDO> response = permissionsRepository.page(paramDO, page);
        result.setRows(response.getDataList());
        result.setTotal(response.getTotalCount());
        result.setStatus(BaseConstants.CommonCode.SUCCESS_CODE);
        return result;
	}
    /**
     * 资源维护
     * @param permission
     * @return
     * @throws BusinessException
     */
    @Transactional(value="transactionManager",rollbackFor = { Exception.class,RuntimeException.class})
    public Response savePermissions(String permissionId,PermissionsDO entity,String user) throws BusinessException {
    	Long id = null;
    	if (StringUtils.isNotBlank(permissionId)) {
    		id = Long.parseLong(permissionId);
    	}
        String permissionCode = entity.getPermissionCode();
        List<PermissionsDO> dataList = permissionsRepository.filter(permissionCode, id);
        if (!CollectionUtils.isEmpty(dataList)) {
            throw new BusinessException("资源编码已存在!");
        }
        if (StringUtils.isBlank(permissionId)) {
        	entity.setCreatedBy(user);
        	entity.setUpdatedBy(user);
            permissionsRepository.insert(entity);
            return Response.success("资源新增成功！");
        }
        entity.setId(id);
        entity.setUpdatedBy(user);
        permissionsRepository.updateAll(entity);
        return Response.success("资源修改成功！");
    }
}