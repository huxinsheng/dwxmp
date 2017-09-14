package cn.dwxmp.core;


import cn.dwxmp.core.base.BaseProviderImpl;
import cn.dwxmp.provider.SysProvider;
import com.alibaba.dubbo.config.annotation.Service;
import com.weibo.api.motan.config.springsupport.annotation.MotanService;

@Service(interfaceClass = SysProvider.class)
@MotanService(interfaceClass = SysProvider.class)
public class SysProviderImpl extends BaseProviderImpl implements SysProvider {

}