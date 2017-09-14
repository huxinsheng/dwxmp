package cn.dwxmp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import cn.dwxmp.core.base.BaseMapper;
import cn.dwxmp.model.SysDept;

public interface SysDeptMapper extends BaseMapper<SysDept> {

	List<Long> selectIdPage(@Param("cm") SysDept sysDept);
}