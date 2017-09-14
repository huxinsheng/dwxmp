package cn.dwxmp.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import cn.dwxmp.core.base.BaseMapper;
import cn.dwxmp.model.SysDic;

public interface SysDicMapper extends BaseMapper<SysDic> {
    List<Long> selectIdPage(@Param("cm") Map<String, Object> params);
}