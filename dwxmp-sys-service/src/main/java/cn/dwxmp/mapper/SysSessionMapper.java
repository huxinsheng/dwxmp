package cn.dwxmp.mapper;

import java.util.List;

import cn.dwxmp.core.base.BaseMapper;
import cn.dwxmp.model.SysSession;

public interface SysSessionMapper extends BaseMapper<SysSession> {

    void deleteBySessionId(String sessionId);

    Long queryBySessionId(String sessionId);

    List<String> querySessionIdByAccount(String account);
}