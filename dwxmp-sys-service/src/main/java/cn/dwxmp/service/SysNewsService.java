package cn.dwxmp.service;

import cn.dwxmp.core.base.BaseService;
import cn.dwxmp.model.SysNews;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

/**
 * @author HuXinsheng
 *
 */
@Service
@CacheConfig(cacheNames = "sysNews")
public class SysNewsService extends BaseService<SysNews> {

}
