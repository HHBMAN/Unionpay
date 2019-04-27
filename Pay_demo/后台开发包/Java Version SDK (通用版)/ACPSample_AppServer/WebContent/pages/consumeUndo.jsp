<%@ page language="java" contentType="text/html; charset=UTF-8"  import="java.text.*" import="java.util.*" 
    pageEncoding="UTF-8"%>

<form class="api-form" method="post" action="<%request.getContextPath();%>/ACPSample_AppServer/form05_6_3_ConsumeUndo" target="_blank">
<p>
<label>商户号：</label>
<input id="merId" type="text" name="merId" placeholder="" value="777290058110048" title="默认商户号仅作为联调测试使用，正式上线还请使用正式申请的商户号" required="required"/>
</p>
<p>
<label>订单发送时间：</label>
<input id="txnTime" type="text" name="txnTime" placeholder="订单发送时间" value="<%=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) %>" title="取北京时间，YYYYMMDDhhmmss格式" required="required"/>
</label>
<p>
<label>商户订单号：</label>
<input id="orderId" type="text" name="orderId" placeholder="商户订单号" value="<%=new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) %>" title="自行定义，8-32位数字字母" required="required"/>
</p>
<p>
<label>交易金额：</label>
<input id="txnAmt" type="text" name="txnAmt" placeholder="交易金额" value="" title="单位分，需与原消费一致" required="required"/>
</p>
<p>
<label>原交易流水号：</label>
<input id="origQryId" type="text" name="origQryId" placeholder="原交易流水号" value="" title="原交易流水号，从交易状态查询返回报文或代收的通知报文中获取 " required="required"/>
</p>
<p>
<label>&nbsp;</label>
<input type="submit" class="button" value="提交" />
<input type="button" class="showFaqBtn" value="遇到问题？" />
</p>
</form>

<div class="question">
<hr />
<h4>消费撤销您可能会遇到...</h4>
<p class="faq">
原交易状态不正确[2050001]:撤销类交易的交易金额和币种与原交易不匹配<br><br>
原交易状态不正确[2050002]:撤销原交易状态错,可能已经做过撤销或退货了<br><br>
原交易状态不正确[2040004]:撤销交易报文origQryId上送错误，没有找到被撤销的交易<br><br>
</p>
<hr />
 <jsp:include  page="/pages/more_faq.jsp"/>
</div>