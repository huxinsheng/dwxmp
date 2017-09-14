package cn.dwxmp.service;

import cn.dwxmp.core.base.BaseService;
import cn.dwxmp.model.SysEmail;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

/**
 * @author HuXinsheng
 *
 */
@Service
@CacheConfig(cacheNames = "sysEmail")
public class SysEmailService extends BaseService<SysEmail> {

}
