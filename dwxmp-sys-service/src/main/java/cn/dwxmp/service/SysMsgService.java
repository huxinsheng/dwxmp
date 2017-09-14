package cn.dwxmp.service;

import cn.dwxmp.model.SysMsg;
import cn.dwxmp.core.base.BaseService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 短信  服务实现类
 * </p>
 *
 * @author HuXinsheng
 * @since 2017-03-12
 */
@Service
@CacheConfig(cacheNames = "sysMsg")
public class SysMsgService extends BaseService<SysMsg> {
	
}