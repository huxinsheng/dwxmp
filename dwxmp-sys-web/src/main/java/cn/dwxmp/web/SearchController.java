package cn.dwxmp.web;

import cn.dwxmp.core.base.AbstractController;
import cn.dwxmp.core.base.Parameter;
import cn.dwxmp.provider.SysProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@Api(value = "搜索", description = "搜索")
public class SearchController extends AbstractController<SysProvider> {

	public String getService() {
		return "searchService";
	}

	@PutMapping("query")
	@ApiOperation(value = "全库搜索")
	public Object query(ModelMap modelMap, @RequestBody Map<String, Object> param) {
		Parameter parameter = new Parameter(getService(), "query").setMap(param);
		List<?> list = provider.execute(parameter).getList();
		return setSuccessModelMap(modelMap, list);
	}
}
