package cn.dwxmp.core;

import cn.dwxmp.core.base.BaseProviderImpl;
import cn.dwxmp.provider.BizProvider;

import com.alibaba.dubbo.config.annotation.Service;
import com.weibo.api.motan.config.springsupport.annotation.MotanService;

@Service(interfaceClass = BizProvider.class)
@MotanService(interfaceClass = BizProvider.class)
public class BizProviderImpl extends BaseProviderImpl implements BizProvider {

}