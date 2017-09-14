package cn.dwxmp.web;

import cn.dwxmp.core.Constants;
import cn.dwxmp.core.base.AbstractController;
import cn.dwxmp.core.base.Parameter;
import cn.dwxmp.core.config.Resources;
import cn.dwxmp.core.exception.LoginException;
import cn.dwxmp.core.support.Assert;
import cn.dwxmp.core.support.HttpCode;
import cn.dwxmp.core.support.login.LoginHelper;
import cn.dwxmp.core.util.SecurityUtil;
import cn.dwxmp.model.Login;
import cn.dwxmp.model.SysUser;
import cn.dwxmp.provider.SysProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.SecurityUtils;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户登录
 * 
 * @author HuXinsheng
 * @version 2016年5月20日 下午3:11:21
 */
@RestController
@Api(value = "登录接口", description = "登录接口")
public class LoginController extends AbstractController<SysProvider> {

	public String getService() {
		return "sysUserService";
	}

	// 登录
	@ApiOperation(value = "用户登录")
	@PostMapping("/login")
	public Object login(@ApiParam(required = true, value = "登录帐号和密码") @RequestBody Login user, ModelMap modelMap,
			HttpServletRequest request) {
		Assert.notNull(user.getAccount(), "ACCOUNT");
		Assert.notNull(user.getPassword(), "PASSWORD");
		String clientIp = (String) request.getSession().getAttribute(Constants.USER_IP);
		if (LoginHelper.login(user.getAccount(), SecurityUtil.encryptPassword(user.getPassword()), clientIp)) {
			request.setAttribute("msg", "[" + user.getAccount() + "]登录成功.");
			return setSuccessModelMap(modelMap);
		}
		request.setAttribute("msg", "[" + user.getAccount() + "]登录失败.");
		throw new LoginException(Resources.getMessage("LOGIN_FAIL"));
	}

	// 登出
	@ApiOperation(value = "用户登出")
	@PostMapping("/logout")
	public Object logout(HttpServletRequest request, ModelMap modelMap) {
		SecurityUtils.getSubject().logout();
		return setSuccessModelMap(modelMap);
	}

	// 注册
	@ApiOperation(value = "用户注册")
	@PostMapping("/regin")
	public Object regin(HttpServletRequest request, ModelMap modelMap, @RequestBody SysUser sysUser) {
		Assert.notNull(sysUser.getAccount(), "ACCOUNT");
		Assert.notNull(sysUser.getPassword(), "PASSWORD");
		sysUser.setPassword(SecurityUtil.encryptPassword(sysUser.getPassword()));
		provider.execute(new Parameter("sysUserService", "update").setModel(sysUser));
		String clientIp = (String) request.getSession().getAttribute(Constants.USER_IP);
		if (LoginHelper.login(sysUser.getAccount(), sysUser.getPassword(), clientIp)) {
			return setSuccessModelMap(modelMap);
		}
		throw new IllegalArgumentException(Resources.getMessage("LOGIN_FAIL"));
	}

	// 没有登录
	@ApiIgnore
	@RequestMapping(value = "/unauthorized", method = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT })
	public Object unauthorized(ModelMap modelMap) throws Exception {
		return setModelMap(modelMap, HttpCode.UNAUTHORIZED);
	}

	// 没有权限
	@ApiIgnore
	@RequestMapping(value = "/forbidden", method = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT })
	public Object forbidden(ModelMap modelMap) {
		return setModelMap(modelMap, HttpCode.FORBIDDEN);
	}
}
