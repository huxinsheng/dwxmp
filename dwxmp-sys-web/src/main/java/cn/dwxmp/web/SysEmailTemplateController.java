package cn.dwxmp.web;

import cn.dwxmp.core.base.AbstractController;
import cn.dwxmp.model.SysEmailTemplate;
import cn.dwxmp.provider.SysProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 邮件模版管理控制类
 * 
 * @author HuXinsheng
 * @version 2016年5月20日 下午3:13:31
 */
@RestController
@Api(value = "邮件模版管理", description = "邮件模版管理")
@RequestMapping(value = "emailTemplate")
public class SysEmailTemplateController extends AbstractController<SysProvider> {
	public String getService() {
		return "sysEmailTemplateService";
	}

	@ApiOperation(value = "查询邮件模版")
	@RequiresPermissions("sys.email.template.read")
	@RequestMapping(value = "/read/list", method = RequestMethod.PUT)
	public Object query(ModelMap modelMap, @RequestBody Map<String, Object> param) {
		return super.query(modelMap, param);
	}

	@ApiOperation(value = "邮件模版详情")
	@RequiresPermissions("sys.email.template.read")
	@RequestMapping(value = "/read/detail", method = RequestMethod.PUT)
	public Object get(ModelMap modelMap, @RequestBody SysEmailTemplate param) {
		return super.get(modelMap, param);
	}

	@ApiOperation(value = "修改邮件模版")
	@RequiresPermissions("sys.email.template.update")
	@RequestMapping(method = RequestMethod.POST)
	public Object update(ModelMap modelMap, @RequestBody SysEmailTemplate param) {
		return super.update(modelMap, param);
	}

	@ApiOperation(value = "删除邮件模版")
	@RequiresPermissions("sys.email.template.delete")
	@RequestMapping(method = RequestMethod.DELETE)
	public Object delete(ModelMap modelMap, @RequestBody SysEmailTemplate param) {
		return super.delete(modelMap, param);
	}
}