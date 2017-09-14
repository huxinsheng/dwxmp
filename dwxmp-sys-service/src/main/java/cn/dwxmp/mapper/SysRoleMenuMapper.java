package cn.dwxmp.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import cn.dwxmp.core.base.BaseMapper;
import cn.dwxmp.model.SysRoleMenu;

public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {
	List<Long> queryMenuIdsByRoleId(@Param("roleId") Long roleId);

	List<Map<String, Object>> queryPermissions(@Param("roleId") Long roleId);

	List<String> queryPermission(@Param("roleId") Long id);
}
