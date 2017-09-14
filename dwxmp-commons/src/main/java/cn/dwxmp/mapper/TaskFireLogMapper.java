package cn.dwxmp.mapper;

import java.util.List;
import java.util.Map;

import cn.dwxmp.model.TaskFireLog;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.baomidou.mybatisplus.mapper.BaseMapper;

public interface TaskFireLogMapper extends BaseMapper<TaskFireLog> {
    List<Long> selectIdByMap(RowBounds rowBounds, @Param("cm") Map<String, Object> params);
}
