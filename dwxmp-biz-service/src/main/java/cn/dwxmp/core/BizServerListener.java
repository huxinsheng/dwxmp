package cn.dwxmp.core;

import javax.servlet.ServletContextEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import cn.dwxmp.core.listener.ServerListener;

public class BizServerListener extends ServerListener{
    protected final Logger logger = LogManager.getLogger(this.getClass());

    public void contextInitialized(ServletContextEvent contextEvent) {
        super.contextInitialized(contextEvent);
    }
}
