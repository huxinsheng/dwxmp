package cn.dwxmp.core;

import cn.dwxmp.service.SysCacheService;
import cn.dwxmp.service.SysDicService;
import cn.dwxmp.service.SysUserService;
import com.weibo.api.motan.common.MotanConstants;
import com.weibo.api.motan.util.MotanSwitcherUtil;
import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.springframework.context.ApplicationContext;

/**
 * @项目名称： dwxmp
 * @类名称： Main
 * @类描述：
 * @创建人： huxinsheng
 * @创建时间： 2017-09-13 13:40
 * @联系方式： hxsysh@gmail.com
 * @修改备注：
 */
public class Main {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-config.xml");
        applicationContext.getBean(SysCacheService.class).flush();
        applicationContext.getBean(SysUserService.class).init();
        SysDicService sysDicService = applicationContext.getBean(SysDicService.class);
        sysDicService.getAllDic();
        MotanSwitcherUtil.setSwitcherValue(MotanConstants.REGISTRY_HEARTBEAT_SWITCHER, true);
        System.out.println("server start...");
    }

}
