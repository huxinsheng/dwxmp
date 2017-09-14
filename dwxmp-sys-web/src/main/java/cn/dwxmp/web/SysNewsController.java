package cn.dwxmp.web;

import cn.dwxmp.core.base.AbstractController;
import cn.dwxmp.model.SysNews;
import cn.dwxmp.provider.SysProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 新闻管理控制类
 * 
 * @author HuXinsheng
 * @version 2016年5月20日 下午3:13:31
 */
@RestController
@Api(value = "新闻管理", description = "新闻管理")
@RequestMapping(value = "news")
public class SysNewsController extends AbstractController<SysProvider> {
	public String getService() {
		return "sysNewsService";
	}

	@ApiOperation(value = "查询新闻")
	@RequiresPermissions("sys.cms.news.read")
	@PutMapping(value = "/read/list")
	public Object query(ModelMap modelMap, @RequestBody Map<String, Object> param) {
		return super.query(modelMap, param);
	}

	@ApiOperation(value = "新闻详情")
	@RequiresPermissions("sys.cms.news.read")
	@PutMapping(value = "/read/detail")
	public Object get(ModelMap modelMap, @RequestBody SysNews param) {
		return super.get(modelMap, param);
	}

	@PostMapping
	@ApiOperation(value = "修改新闻")
	@RequiresPermissions("sys.cms.news.update")
	public Object update(ModelMap modelMap, @RequestBody SysNews param) {
		if (param.getStatus() == null) {
			param.setStatus("0");
		}
		return super.update(modelMap, param);
	}

	@DeleteMapping
	@ApiOperation(value = "删除新闻")
	@RequiresPermissions("sys.cms.news.delete")
	public Object delete(ModelMap modelMap, @RequestBody SysNews param) {
		return super.delete(modelMap, param);
	}
}
