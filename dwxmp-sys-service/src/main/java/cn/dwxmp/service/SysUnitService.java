package cn.dwxmp.service;

import cn.dwxmp.core.base.BaseService;
import cn.dwxmp.model.SysUnit;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

/**
 * @author HuXinsheng
 *
 */
@Service
@CacheConfig(cacheNames = "sysUnit")
public class SysUnitService extends BaseService<SysUnit> {

}
