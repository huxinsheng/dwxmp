/**
 * 
 */
package cn.dwxmp.core.exception;

import cn.dwxmp.core.support.HttpCode;

/**
 * 
 * @author HuXinsheng
 * @version 2017年3月24日 下午9:30:10
 */
@SuppressWarnings("serial")
public class InstanceException extends BaseException {
    public InstanceException() {
        super();
    }

    public InstanceException(Throwable t) {
        super(t);
    }

    protected HttpCode getHttpCode() {
        return HttpCode.INTERNAL_SERVER_ERROR;
    }
}
