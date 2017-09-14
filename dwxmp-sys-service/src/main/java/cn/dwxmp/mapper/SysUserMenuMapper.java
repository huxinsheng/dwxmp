package cn.dwxmp.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import cn.dwxmp.core.base.BaseMapper;
import cn.dwxmp.model.SysUserMenu;

public interface SysUserMenuMapper extends BaseMapper<SysUserMenu> {
	List<Long> queryMenuIdsByUserId(@Param("userId") Long userId);

	List<Map<String, Object>> queryPermissions(@Param("userId") Long userId);

	List<String> queryPermission(@Param("userId") Long id);
}