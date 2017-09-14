package cn.dwxmp.service;

import cn.dwxmp.core.base.BaseService;
import cn.dwxmp.model.SysEmailConfig;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

/**
 * @author HuXinsheng
 *
 */
@Service
@CacheConfig(cacheNames = "sysEmailConfig")
public class SysEmailConfigService extends BaseService<SysEmailConfig> {

}
