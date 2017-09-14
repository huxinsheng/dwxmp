package cn.dwxmp.core.support.pay;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.*;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AliPay {
	static Logger log = LogManager.getLogger(AliPay.class);

	/**
	 * App支付
	 * 
	 * @param model
	 * @param notifyUrl
	 * @return
	 * @throws AlipayApiException
	 */
	public static String startAppPayStr(AlipayTradeAppPayModel model, String notifyUrl) throws AlipayApiException {
		AlipayTradeAppPayResponse response = appPay(model, notifyUrl);
		return response.getBody();
	}

	/**
	 * App 支付
	 * https://doc.open.alipay.com/docs/doc.htm?treeId=54&articleId=106370&docType=1
	 * 
	 * @param model
	 * @param notifyUrl
	 * @return
	 * @throws AlipayApiException
	 */
	public static AlipayTradeAppPayResponse appPay(AlipayTradeAppPayModel model, String notifyUrl)
			throws AlipayApiException {
		// 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
		AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
		// SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		// 这里和普通的接口调用不同，使用的是sdkExecute
		AlipayTradeAppPayResponse response = AliPayConfig.build().getAlipayClient().sdkExecute(request);
		return response;
	}

	/**
	 * Wap支付
	 * https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.dfHHR3&
	 * treeId=203&articleId=105285&docType=1
	 * 
	 * @throws AlipayApiException
	 * @throws IOException
	 */
	public static void wapPay(HttpServletResponse response, AlipayTradeWapPayModel model, String returnUrl,
			String notifyUrl) throws AlipayApiException, IOException {
		String form = wapPayToString(response, model, returnUrl, notifyUrl);
		HttpServletResponse httpResponse = response;
		httpResponse.setContentType("text/html;charset=" + AliPayConfig.build().getCharset());
		httpResponse.getWriter().write(form);// 直接将完整的表单html输出到页面
		httpResponse.getWriter().flush();
	}

	public static String wapPayToString(HttpServletResponse response, AlipayTradeWapPayModel model, String returnUrl,
			String notifyUrl) throws AlipayApiException, IOException {
		AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();// 创建API对应的request
		alipayRequest.setReturnUrl(returnUrl);
		alipayRequest.setNotifyUrl(notifyUrl);// 在公共参数中设置回跳和通知地址
		alipayRequest.setBizModel(model);// 填充业务参数
		return AliPayConfig.build().getAlipayClient().pageExecute(alipayRequest).getBody(); // 调用SDK生成表单
	}

	/**
	 * 条形码支付、声波支付
	 * https://doc.open.alipay.com/docs/api.htm?spm=a219a.7629065.0.0.XVqALk&apiId=850&docType=4
	 * 
	 * @param notifyUrl
	 * @throws AlipayApiException
	 */
	public static String tradePay(AlipayTradePayModel model, String notifyUrl) throws AlipayApiException {
		AlipayTradePayResponse response = tradePayToResponse(model, notifyUrl);
		return response.getBody();
	}

	public static AlipayTradePayResponse tradePayToResponse(AlipayTradePayModel model, String notifyUrl)
			throws AlipayApiException {
		AlipayTradePayRequest request = new AlipayTradePayRequest();
		request.setBizModel(model);// 填充业务参数
		request.setNotifyUrl(notifyUrl);
		return AliPayConfig.build().getAlipayClient().execute(request); // 通过AliPayConfig.build().getAlipayClient()调用API，获得对应的response类
	}

	/**
	 * 扫码支付
	 * https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.i0UVZn&treeId=193&articleId=105170&docType=1#s4
	 * 
	 * @param notifyUrl
	 * @return
	 * @throws AlipayApiException
	 */
	public static String tradePrecreatePay(AlipayTradePrecreateModel model, String notifyUrl)
			throws AlipayApiException {
		AlipayTradePrecreateResponse response = tradePrecreatePayToResponse(model, notifyUrl);
		return response.getBody();
	}

	public static AlipayTradePrecreateResponse tradePrecreatePayToResponse(AlipayTradePrecreateModel model,
			String notifyUrl) throws AlipayApiException {
		AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		return AliPayConfig.build().getAlipayClient().execute(request);
	}

	/**
	 * 单笔转账到支付宝账户
	 * https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.54Ty29&treeId=193&articleId=106236&docType=1
	 * 
	 * @param model
	 * @return
	 * @throws AlipayApiException
	 */
	public static boolean transfer(AlipayFundTransToaccountTransferModel model) throws AlipayApiException {
		AlipayFundTransToaccountTransferResponse response = transferToResponse(model);
		String result = response.getBody();
		log.info("transfer result>" + result);
		if (response.isSuccess()) {
			return true;
		} else {
			// 调用查询接口查询数据
			JSONObject jsonObject = JSONObject.parseObject(result);
			String out_biz_no = jsonObject.getJSONObject("alipay_fund_trans_toaccount_transfer_response")
					.getString("out_biz_no");
			AlipayFundTransOrderQueryModel queryModel = new AlipayFundTransOrderQueryModel();
			model.setOutBizNo(out_biz_no);
			boolean isSuccess = transferQuery(queryModel);
			if (isSuccess) {
				return true;
			}
		}
		return false;
	}

	public static AlipayFundTransToaccountTransferResponse transferToResponse(
			AlipayFundTransToaccountTransferModel model) throws AlipayApiException {
		AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
		request.setBizModel(model);
		return AliPayConfig.build().getAlipayClient().execute(request);
	}

	/**
	 * 转账查询接口
	 * 
	 * @param model
	 * @return
	 * @throws AlipayApiException
	 */
	public static boolean transferQuery(AlipayFundTransOrderQueryModel model) throws AlipayApiException {
		AlipayFundTransOrderQueryResponse response = transferQueryToResponse(model);
		log.info("transferQuery result>" + response.getBody());
		if (response.isSuccess()) {
			return true;
		}
		return false;
	}

	public static AlipayFundTransOrderQueryResponse transferQueryToResponse(AlipayFundTransOrderQueryModel model)
			throws AlipayApiException {
		AlipayFundTransOrderQueryRequest request = new AlipayFundTransOrderQueryRequest();
		request.setBizModel(model);
		return AliPayConfig.build().getAlipayClient().execute(request);
	}

	/**
	 * 交易查询接口
	 * https://doc.open.alipay.com/docs/api.htm?spm=a219a.7395905.0.0.8H2JzG&docType=4&apiId=757
	 * 
	 * @param model
	 * @return
	 * @throws AlipayApiException
	 */
	public static boolean isTradeQuery(AlipayTradeQueryModel model) throws AlipayApiException {
		AlipayTradeQueryResponse response = tradeQuery(model);
		if (response.isSuccess()) {
			return true;
		}
		return false;
	}

	public static AlipayTradeQueryResponse tradeQuery(AlipayTradeQueryModel model) throws AlipayApiException {
		AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
		request.setBizModel(model);
		return AliPayConfig.build().getAlipayClient().execute(request);
	}

	/**
	 * 交易撤销接口
	 * https://doc.open.alipay.com/docs/api.htm?spm=a219a.7395905.0.0.XInh6e&docType=4&apiId=866
	 * 
	 * @param model
	 * @return
	 * @throws AlipayApiException
	 */
	public static boolean isTradeCancel(AlipayTradeCancelModel model) throws AlipayApiException {
		AlipayTradeCancelResponse response = tradeCancel(model);
		if (response.isSuccess()) {
			return true;
		}
		return false;
	}

	public static AlipayTradeCancelResponse tradeCancel(AlipayTradeCancelModel model) throws AlipayApiException {
		AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
		request.setBizModel(model);
		AlipayTradeCancelResponse response = AliPayConfig.build().getAlipayClient().execute(request);
		return response;
	}

	/**
	 * 关闭订单
	 * https://doc.open.alipay.com/docs/api.htm?spm=a219a.7629065.0.0.21yRUe&apiId=1058&docType=4
	 * 
	 * @param model
	 * @return
	 * @throws AlipayApiException
	 */
	public static boolean isTradeClose(AlipayTradeCloseModel model) throws AlipayApiException {
		AlipayTradeCloseResponse response = tradeClose(model);
		if (response.isSuccess()) {
			return true;
		}
		return false;
	}

	public static AlipayTradeCloseResponse tradeClose(AlipayTradeCloseModel model) throws AlipayApiException {
		AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
		request.setBizModel(model);
		return AliPayConfig.build().getAlipayClient().execute(request);

	}

	/**
	 * 统一收单交易创建接口
	 * https://doc.open.alipay.com/docs/api.htm?spm=a219a.7629065.0.0.21yRUe&apiId=1046&docType=4
	 * 
	 * @param model
	 * @param notifyUrl
	 * @return
	 * @throws AlipayApiException
	 */
	public static AlipayTradeCreateResponse tradeCreate(AlipayTradeCreateModel model, String notifyUrl)
			throws AlipayApiException {
		AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		return AliPayConfig.build().getAlipayClient().execute(request);
	}

	/**
	 * 退款
	 * https://doc.open.alipay.com/docs/api.htm?spm=a219a.7395905.0.0.SAyEeI&docType=4&apiId=759
	 * 
	 * @param model
	 * @return
	 * @throws AlipayApiException
	 */
	public static String tradeRefund(AlipayTradeRefundModel model) throws AlipayApiException {
		AlipayTradeRefundResponse response = tradeRefundToResponse(model);
		return response.getBody();
	}

	public static AlipayTradeRefundResponse tradeRefundToResponse(AlipayTradeRefundModel model)
			throws AlipayApiException {
		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
		request.setBizModel(model);
		return AliPayConfig.build().getAlipayClient().execute(request);
	}

	/**
	 * 退款查询
	 * https://doc.open.alipay.com/docs/api.htm?spm=a219a.7629065.0.0.KQeTSa&apiId=1049&docType=4
	 * 
	 * @param model
	 * @return
	 * @throws AlipayApiException
	 */
	public static String tradeRefundQuery(AlipayTradeFastpayRefundQueryModel model) throws AlipayApiException {
		AlipayTradeFastpayRefundQueryResponse response = tradeRefundQueryToResponse(model);
		return response.getBody();
	}

	public static AlipayTradeFastpayRefundQueryResponse tradeRefundQueryToResponse(
			AlipayTradeFastpayRefundQueryModel model) throws AlipayApiException {
		AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
		request.setBizModel(model);
		return AliPayConfig.build().getAlipayClient().execute(request);
	}

	/**
	 * 查询对账单下载地址
	 * 
	 * @param model
	 * @return
	 * @throws AlipayApiException
	 */
	public static String billDownloadurlQuery(AlipayDataDataserviceBillDownloadurlQueryModel model)
			throws AlipayApiException {
		AlipayDataDataserviceBillDownloadurlQueryResponse response = billDownloadurlQueryToResponse(model);
		return response.getBillDownloadUrl();
	}

	public static AlipayDataDataserviceBillDownloadurlQueryResponse billDownloadurlQueryToResponse(
			AlipayDataDataserviceBillDownloadurlQueryModel model) throws AlipayApiException {
		AlipayDataDataserviceBillDownloadurlQueryRequest request = new AlipayDataDataserviceBillDownloadurlQueryRequest();
		request.setBizModel(model);
		return AliPayConfig.build().getAlipayClient().execute(request);
	}

	/**
	 * 交易结算接口
	 * https://doc.open.alipay.com/docs/api.htm?spm=a219a.7395905.0.0.nl0RS3&docType=4&apiId=1147
	 * 
	 * @param model
	 * @return
	 * @throws AlipayApiException
	 */
	public static boolean isTradeOrderSettle(AlipayTradeOrderSettleModel model) throws AlipayApiException {
		AlipayTradeOrderSettleResponse response = tradeOrderSettle(model);
		if (response.isSuccess()) {
			return true;
		}
		return false;
	}

	public static AlipayTradeOrderSettleResponse tradeOrderSettle(AlipayTradeOrderSettleModel model)
			throws AlipayApiException {
		AlipayTradeOrderSettleRequest request = new AlipayTradeOrderSettleRequest();
		request.setBizModel(model);
		return AliPayConfig.build().getAlipayClient().execute(request);
	}

	/**
	 * 电脑网站支付(PC支付)
	 * 
	 * @param model
	 * @param notifyUrl
	 * @param returnUrl
	 * @return
	 * @throws AlipayApiException
	 * @throws IOException
	 */
	public static void tradePage(HttpServletResponse httpResponse, AlipayTradePayModel model, String notifyUrl,
			String returnUrl) throws AlipayApiException, IOException {
		AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		request.setReturnUrl(returnUrl);
		String form = AliPayConfig.build().getAlipayClient().pageExecute(request).getBody();// 调用SDK生成表单
		httpResponse.setContentType("text/html;charset=" + AliPayConfig.build().getCharset());
		httpResponse.getWriter().write(form);// 直接将完整的表单html输出到页面
		httpResponse.getWriter().flush();
		httpResponse.getWriter().close();
	}

	/**
	 * 将异步通知的参数转化为Map
	 * 
	 * @param request
	 * @return
	 */
	public static Map<String, String> toMap(HttpServletRequest request) {
		Map<String, String> params = new HashMap<>();
		Map<String, String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = iter.next();
			String[] values = requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			// 乱码解决，这段代码在出现乱码时使用。
			// valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}
		return params;
	}
}
