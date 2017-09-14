package cn.dwxmp.web;

import cn.dwxmp.core.base.AbstractController;
import cn.dwxmp.model.SysFeedback;
import cn.dwxmp.provider.SysProvider;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 反馈  前端控制器
 * </p>
 *
 * @author HuXinsheng
 * @since 2017-03-12
 */
@Controller
@RequestMapping("/feedback")
public class SysFeedbackController extends AbstractController<SysProvider> {
	public String getService() {
		return "sysFeedbackService";
	}

	@ApiOperation(value = "查询反馈")
	@RequiresPermissions("cms.feedback.read")
	@PutMapping(value = "/read/list")
	public Object query(ModelMap modelMap, @RequestBody Map<String, Object> param) {
		return super.query(modelMap, param);
	}

	@ApiOperation(value = "反馈详情")
	@RequiresPermissions("cms.feedback.read")
	@PutMapping(value = "/read/detail")
	public Object get(ModelMap modelMap, @RequestBody SysFeedback param) {
		return super.get(modelMap, param);
	}

	@PostMapping
	@ApiOperation(value = "修改反馈")
	@RequiresPermissions("cms.feedback.update")
	public Object update(ModelMap modelMap, @RequestBody SysFeedback param) {
		return super.update(modelMap, param);
	}

	@DeleteMapping
	@ApiOperation(value = "删除反馈")
	@RequiresPermissions("cms.feedback.delete")
	public Object delete(ModelMap modelMap, @RequestBody SysFeedback param) {
		return super.delete(modelMap, param);
	}
}