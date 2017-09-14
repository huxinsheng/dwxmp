package cn.dwxmp.web;

import cn.dwxmp.core.base.AbstractController;
import cn.dwxmp.model.SysMsgConfig;
import cn.dwxmp.provider.SysProvider;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 *   前端控制器
 * </p>
 *
 * @author HuXinsheng
 * @since 2017-03-12
 */
@Controller
@RequestMapping("/msgConfig")
public class SysMsgConfigController extends AbstractController<SysProvider> {
	public String getService() {
		return "sysMsgConfigService";
	}

	@ApiOperation(value = "查询")
	@RequiresPermissions("msg.config.read")
	@PutMapping(value = "/read/list")
	public Object query(ModelMap modelMap, @RequestBody Map<String, Object> param) {
		return super.query(modelMap, param);
	}

	@ApiOperation(value = "详情")
	@RequiresPermissions("msg.config.read")
	@PutMapping(value = "/read/detail")
	public Object get(ModelMap modelMap, @RequestBody SysMsgConfig param) {
		return super.get(modelMap, param);
	}

	@PostMapping
	@ApiOperation(value = "修改")
	@RequiresPermissions("msg.config.update")
	public Object update(ModelMap modelMap, @RequestBody SysMsgConfig param) {
		return super.update(modelMap, param);
	}

	@DeleteMapping
	@ApiOperation(value = "删除")
	@RequiresPermissions("msg.config.delete")
	public Object delete(ModelMap modelMap, @RequestBody SysMsgConfig param) {
		return super.delete(modelMap, param);
	}
}