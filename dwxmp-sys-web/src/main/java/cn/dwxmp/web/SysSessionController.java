package cn.dwxmp.web;

import cn.dwxmp.core.base.AbstractController;
import cn.dwxmp.core.listener.SessionListener;
import cn.dwxmp.model.SysSession;
import cn.dwxmp.provider.SysProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用户会话管理
 * 
 * @author HuXinsheng
 * @version 2016年5月20日 下午3:13:06
 */
@RestController
@Api(value = "会话管理", description = "会话管理")
@RequestMapping(value = "/session")
public class SysSessionController extends AbstractController<SysProvider> {
	public String getService() {
		return "sysSessionService";
	}
	
	@Autowired
	SessionListener sessionListener;

	// 查询会话
	@ApiOperation(value = "查询会话")
	@PutMapping(value = "/read/list")
	@RequiresPermissions("sys.base.session.read")
	public Object get(ModelMap modelMap, @RequestBody Map<String, Object> param) {
		Integer number = sessionListener.getAllUserNumber();
		modelMap.put("userNumber", number); // 用户数大于会话数,有用户没有登录
		return super.query(modelMap, param);
	}

	@DeleteMapping
	@ApiOperation(value = "删除会话")
	@RequiresPermissions("sys.base.session.delete")
	public Object delete(ModelMap modelMap, @RequestBody SysSession param) {
		return super.delete(modelMap, param);
	}
}
