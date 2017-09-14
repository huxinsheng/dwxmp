package cn.dwxmp.service;

import cn.dwxmp.core.base.BaseService;
import cn.dwxmp.model.SysEmailTemplate;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

/**
 * @author HuXinsheng
 *
 */
@Service
@CacheConfig(cacheNames = "sysEmailTemplate")
public class SysEmailTemplateService extends BaseService<SysEmailTemplate> {

}
