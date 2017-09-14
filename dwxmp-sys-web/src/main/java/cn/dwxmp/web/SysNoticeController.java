package cn.dwxmp.web;

import cn.dwxmp.core.base.AbstractController;
import cn.dwxmp.model.SysNotice;
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
 * 通知管理控制类
 * 
 * @author HuXinsheng
 * @version 2016年5月20日 下午3:13:31
 */
@RestController
@Api(value = "通知管理", description = "通知管理")
@RequestMapping(value = "notice")
public class SysNoticeController extends AbstractController<SysProvider> {
	public String getService() {
		return "sysNoticeService";
	}

	@ApiOperation(value = "查询通知")
	@RequiresPermissions("cms.notice.read")
	@RequestMapping(value = "/read/list", method = RequestMethod.PUT)
	public Object query(ModelMap modelMap, @RequestBody Map<String, Object> param) {
		return super.query(modelMap, param);
	}

	@ApiOperation(value = "通知详情")
	@RequiresPermissions("cms.notice.read")
	@RequestMapping(value = "/read/detail", method = RequestMethod.PUT)
	public Object get(ModelMap modelMap, @RequestBody SysNotice param) {
		return super.get(modelMap, param);
	}

	@ApiOperation(value = "修改通知")
	@RequiresPermissions("cms.notice.update")
	@RequestMapping(method = RequestMethod.POST)
	public Object update(ModelMap modelMap, @RequestBody SysNotice param) {
		if (param.getStatus() == null) {
			param.setStatus("0");
		}
		return super.update(modelMap, param);
	}

	@ApiOperation(value = "删除通知")
	@RequiresPermissions("cms.notice.delete")
	@RequestMapping(method = RequestMethod.DELETE)
	public Object delete(ModelMap modelMap, @RequestBody SysNotice param) {
		return super.delete(modelMap, param);
	}
}
