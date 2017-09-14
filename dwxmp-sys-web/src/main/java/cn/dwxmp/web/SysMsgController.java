package cn.dwxmp.web;

import cn.dwxmp.core.base.AbstractController;
import cn.dwxmp.model.SysMsg;
import cn.dwxmp.provider.SysProvider;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 短信  前端控制器
 * </p>
 *
 * @author HuXinsheng
 * @since 2017-03-12
 */
@Controller
@RequestMapping("/msg")
public class SysMsgController extends AbstractController<SysProvider> {
	public String getService() {
		return "sysMsgService";
	}

	@ApiOperation(value = "查询短信")
	@RequiresPermissions("msg.list.read")
	@PutMapping(value = "/read/list")
	public Object query(ModelMap modelMap, @RequestBody Map<String, Object> param) {
		return super.query(modelMap, param);
	}

	@ApiOperation(value = "短信详情")
	@RequiresPermissions("msg.list.read")
	@PutMapping(value = "/read/detail")
	public Object get(ModelMap modelMap, @RequestBody SysMsg param) {
		return super.get(modelMap, param);
	}

	@PostMapping
	@ApiOperation(value = "修改短信")
	@RequiresPermissions("msg.list.update")
	public Object update(ModelMap modelMap, @RequestBody SysMsg param) {
		return super.update(modelMap, param);
	}

	@DeleteMapping
	@ApiOperation(value = "删除短信")
	@RequiresPermissions("msg.list.delete")
	public Object delete(ModelMap modelMap, @RequestBody SysMsg param) {
		return super.delete(modelMap, param);
	}
}