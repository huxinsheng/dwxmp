package cn.dwxmp.web;

import cn.dwxmp.core.base.AbstractController;
import cn.dwxmp.model.SysArticle;
import cn.dwxmp.provider.SysProvider;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 文章  前端控制器
 * </p>
 *
 * @author HuXinsheng
 * @since 2017-03-12
 */
@Controller
@RequestMapping("/article")
public class SysArticleController extends AbstractController<SysProvider> {
	public String getService() {
		return "sysArticleService";
	}

	@ApiOperation(value = "查询文章")
	@RequiresPermissions("cms.article.read")
	@PutMapping(value = "/read/list")
	public Object query(ModelMap modelMap, @RequestBody Map<String, Object> param) {
		return super.query(modelMap, param);
	}

	@ApiOperation(value = "文章详情")
	@RequiresPermissions("cms.article.read")
	@PutMapping(value = "/read/detail")
	public Object get(ModelMap modelMap, @RequestBody SysArticle param) {
		return super.get(modelMap, param);
	}

	@PostMapping
	@ApiOperation(value = "修改文章")
	@RequiresPermissions("cms.article.update")
	public Object update(ModelMap modelMap, @RequestBody SysArticle param) {
		if (param.getEnable() == null) {
			param.setEnable(0);
		}
		if (param.getIsTop() == null) {
			param.setIsTop(0);
		}
		return super.update(modelMap, param);
	}

	@DeleteMapping
	@ApiOperation(value = "删除文章")
	@RequiresPermissions("cms.article.delete")
	public Object delete(ModelMap modelMap, @RequestBody SysArticle param) {
		return super.delete(modelMap, param);
	}
}