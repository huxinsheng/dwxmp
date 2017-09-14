package cn.dwxmp.mapper;

import java.util.List;
import java.util.Map;

import cn.dwxmp.core.base.BaseMapper;
import cn.dwxmp.model.SysUser;
import org.apache.ibatis.annotations.Param;

public interface SysUserMapper extends BaseMapper<SysUser> {
    List<Long> selectIdPage(@Param("cm") Map<String, Object> params);
}
