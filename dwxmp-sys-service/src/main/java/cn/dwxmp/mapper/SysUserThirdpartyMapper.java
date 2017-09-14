package cn.dwxmp.mapper;

import org.apache.ibatis.annotations.Param;
import cn.dwxmp.core.base.BaseMapper;
import cn.dwxmp.model.SysUserThirdparty;

public interface SysUserThirdpartyMapper extends BaseMapper<SysUserThirdparty> {
	Long queryUserIdByThirdParty(@Param("provider") String provider, @Param("openId") String openId);
}