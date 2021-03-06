package cn.dwxmp.web;

import cn.dwxmp.core.base.AbstractController;
import cn.dwxmp.core.base.Parameter;
import cn.dwxmp.core.config.Resources;
import cn.dwxmp.core.exception.LoginException;
import cn.dwxmp.core.support.Assert;
import cn.dwxmp.core.support.HttpCode;
import cn.dwxmp.core.util.*;
import cn.dwxmp.model.Login;
import cn.dwxmp.model.SysUser;
import cn.dwxmp.provider.SysProvider;
import com.baomidou.mybatisplus.plugins.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户登录
 * 
 * @author ShenHuaJie
 * @version 2016年5月20日 下午3:11:21
 */
@RestController
@Api(value = "APP登录接口", description = "APP登录接口")
public class LoginController extends AbstractController<SysProvider> {

	public String getService() {
		return "sysUserService";
	}

	// 登录
	@ApiOperation(value = "用户登录")
	@PostMapping("app/login")
	public Object login(Login user, ModelMap modelMap, HttpServletRequest request) {
		String uuid = request.getHeader("UUID");
		org.springframework.util.Assert.notNull(uuid, "非法操作.");
		user = WebUtil.getParameter(request, Login.class);
		Assert.notNull(user.getAccount(), "ACCOUNT");
		Assert.notNull(user.getPassword(), "PASSWORD");

		boolean success = false;
		String password = (String) CacheUtil.getCache().get("LOGIN_" + user.getAccount());
		if (StringUtils.isNotBlank(password)) {
			if (user.getPassword().equals(password)) {
				WebUtil.saveCurrentUser(request, user.getAccount());
				success = true;
			}
		}
		if (!success) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("countSql", 0);
			params.put("enable", 1);
			params.put("loginKey", user.getAccount()); // 登录帐号/手机号/邮箱
			Parameter parameter = new Parameter(getService(), "query").setMap(params);
			Page<?> pageInfo = provider.execute(parameter).getPage();
			if (pageInfo.getTotal() == 1) {
				SysUser sysUser = (SysUser) pageInfo.getRecords().get(0);
				if (user.getPassword().equals(SecurityUtil.encryptPassword(user.getPassword()))) {
					WebUtil.saveCurrentUser(sysUser.getPhone());
					success = true;
				}
			}
		}

		if (success) {
			request.setAttribute("msg", "[" + user.getAccount() + "]登录成功.");
			String token = SecurityUtil.encryptPassword(user.getAccount() + DateUtil.getDateTime("yyyyMMddHHmmss"));
			TokenUtil.setTokenInfo(token, user.getAccount());
			modelMap.put("token", token);
			return setSuccessModelMap(modelMap);
		}
		request.setAttribute("msg", "[" + user.getAccount() + "]登录失败.");
		throw new LoginException(Resources.getMessage("LOGIN_FAIL"));
	}

	// 登出
	@ApiOperation(value = "用户登出")
	@PostMapping("app/logout")
	public Object logout(HttpServletRequest request, ModelMap modelMap) {
		String token = request.getHeader("token");
		if (StringUtils.isNotBlank(token)) {
			TokenUtil.delToken(token);
		}
		return setSuccessModelMap(modelMap);
	}

	// 注册
	@ApiOperation(value = "用户注册")
	@PostMapping("app/regin")
	public Object regin(ModelMap modelMap, @RequestBody SysUser sysUser) {
		Assert.notNull(sysUser.getAccount(), "ACCOUNT");
		Assert.notNull(sysUser.getPassword(), "PASSWORD");
		sysUser.setPassword(SecurityUtil.encryptPassword(sysUser.getPassword()));
		provider.execute(new Parameter("sysUserService", "update").setModel(sysUser));
		try {
			String token = SecurityUtil.encryptPassword(sysUser.getAccount() + DateUtil.getDateTime("yyyyMMddHHmmss"));
			TokenUtil.setTokenInfo(token, sysUser.getAccount());
			modelMap.put("token", token);
		} catch (Exception e) {
			logger.error("", e);
		}
		throw new IllegalArgumentException(Resources.getMessage("LOGIN_FAIL"));
	}

	// 没有登录
	@ApiOperation(value = "没有登录")
	@RequestMapping(value = "/unauthorized", method = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT })
	public Object unauthorized(ModelMap modelMap) throws Exception {
		return setModelMap(modelMap, HttpCode.UNAUTHORIZED);
	}

	// 没有权限
	@ApiOperation(value = "没有权限")
	@RequestMapping(value = "/forbidden", method = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT })
	public Object forbidden(ModelMap modelMap) {
		return setModelMap(modelMap, HttpCode.FORBIDDEN);
	}
}
