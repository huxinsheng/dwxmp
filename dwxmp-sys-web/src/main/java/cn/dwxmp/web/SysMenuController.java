package cn.dwxmp.web;

import cn.dwxmp.core.base.AbstractController;
import cn.dwxmp.core.base.Parameter;
import cn.dwxmp.model.SysMenu;
import cn.dwxmp.provider.SysProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 菜单管理
 * 
 * @author HuXinsheng
 * @version 2016年5月20日 下午3:14:54
 */
@RestController
@Api(value = "菜单管理", description = "菜单管理")
@RequestMapping(value = "menu")
public class SysMenuController extends AbstractController<SysProvider> {
	public String getService() {
		return "sysMenuService";
	}

	@ApiOperation(value = "查询菜单")
	@PutMapping(value = "/read/page")
	@RequiresPermissions("sys.base.menu.read")
	public Object query(ModelMap modelMap, @RequestBody Map<String, Object> param) {
		return super.query(modelMap, param);
	}

	@ApiOperation(value = "查询菜单")
	@PutMapping(value = "/read/list")
	@RequiresPermissions("sys.base.menu.read")
	public Object get(ModelMap modelMap, @RequestBody Map<String, Object> param) {
		return super.queryList(modelMap, param);
	}

	@ApiOperation(value = "查询菜单")
	@PutMapping(value = "/read/tree")
	@RequiresPermissions("sys.base.menu.read")
	public Object getTree(ModelMap modelMap, @RequestBody Map<String, Object> param) {
		Parameter parameter = new Parameter(getService(), "queryTreeList").setMap(param);
		List<?> list = provider.execute(parameter).getList();
		return setSuccessModelMap(modelMap, list);
	}

	@ApiOperation(value = "菜单详情")
	@PutMapping(value = "/read/detail")
	@RequiresPermissions("sys.base.menu.read")
	public Object get(ModelMap modelMap, @RequestBody SysMenu param) {
		return super.get(modelMap, param);
	}

	@PostMapping
	@ApiOperation(value = "修改菜单")
	@RequiresPermissions("sys.base.menu.update")
	public Object update(ModelMap modelMap, @RequestBody SysMenu param) {
		if (param.getIsShow() == null) {
			param.setIsShow(false);
		}
		return super.update(modelMap, param);
	}

	@DeleteMapping
	@ApiOperation(value = "删除菜单")
	@RequiresPermissions("sys.base.menu.delete")
	public Object delete(ModelMap modelMap, @RequestBody SysMenu param) {
		return super.delete(modelMap, param);
	}

	@ApiOperation(value = "获取所有权限")
	@RequiresPermissions("sys.base.menu.read")
	@RequestMapping(value = "/read/permission")
	public Object getPermissions(ModelMap modelMap) {
		Parameter parameter = new Parameter(getService(), "getPermissions").setModel(new SysMenu());
		List<?> list = provider.execute(parameter).getList();
		return setSuccessModelMap(modelMap, list);
	}
}
