package cn.dwxmp.web;

import cn.dwxmp.core.base.AbstractController;
import cn.dwxmp.model.SysEmail;
import cn.dwxmp.provider.SysProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 邮件管理控制类
 * 
 * @author HuXinsheng
 * @version 2016年5月20日 下午3:13:31
 */
@RestController
@Api(value = "邮件管理", description = "邮件管理")
@RequestMapping(value = "email")
public class SysEmailController extends AbstractController<SysProvider> {

	public String getService() {
		return "sysEmailService";
	}

	@ApiOperation(value = "查询邮件")
	@RequiresPermissions("sys.email.list.read")
	@PutMapping(value = "/read/list")
	public Object query(ModelMap modelMap, @RequestBody Map<String, Object> param) {
		return super.query(modelMap, param);
	}

	@ApiOperation(value = "邮件详情")
	@RequiresPermissions("sys.email.list.read")
	@PutMapping(value = "/read/detail")
	public Object get(ModelMap modelMap, @RequestBody SysEmail param) {
		return super.get(modelMap, param);
	}

	@PostMapping
	@ApiOperation(value = "修改邮件")
	@RequiresPermissions("sys.email.list.update")
	public Object update(ModelMap modelMap, @RequestBody SysEmail param) {
		return super.update(modelMap, param);
	}

	@DeleteMapping
	@ApiOperation(value = "删除邮件")
	@RequiresPermissions("sys.email.list.delete")
	public Object delete(ModelMap modelMap, @RequestBody SysEmail param) {
		return super.delete(modelMap, param);
	}
}