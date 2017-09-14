package cn.dwxmp.web;

import cn.dwxmp.core.base.AbstractController;
import cn.dwxmp.model.SysDic;
import cn.dwxmp.provider.SysProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 字典管理
 * 
 * @author HuXinsheng
 * @version 2016年5月20日 下午3:14:34
 */
@RestController
@Api(value = "字典管理", description = "字典管理")
@RequestMapping(value = "/dic")
public class SysDicController extends AbstractController<SysProvider> {

	public String getService() {
		return "sysDicService";
	}

	@ApiOperation(value = "查询字典项")
	@RequiresPermissions("sys.base.dic.read")
	@PutMapping(value = "/read/page")
	public Object query(ModelMap modelMap, @RequestBody Map<String, Object> param) {
		param.put("orderBy", "type_,sort_no");
		return super.query(modelMap, param);
	}

	@ApiOperation(value = "查询字典项")
	@RequiresPermissions("sys.base.dic.read")
	@PutMapping(value = "/read/list")
	public Object queryList(ModelMap modelMap, @RequestBody Map<String, Object> param) {
		param.put("orderBy", "type_,sort_no");
		return super.queryList(modelMap, param);
	}

	@ApiOperation(value = "字典项详情")
	@RequiresPermissions("sys.base.dic.read")
	@PutMapping(value = "/read/detail")
	public Object get(ModelMap modelMap, @RequestBody SysDic param) {
		return super.get(modelMap, param);
	}

	@PostMapping
	@ApiOperation(value = "修改字典项")
	@RequiresPermissions("sys.base.dic.update")
	public Object update(ModelMap modelMap, @RequestBody SysDic param) {
		return super.update(modelMap, param);
	}

	@DeleteMapping
	@ApiOperation(value = "删除字典项")
	@RequiresPermissions("sys.base.dic.delete")
	public Object delete(ModelMap modelMap, @RequestBody SysDic param) {
		return super.delete(modelMap, param);
	}
}
