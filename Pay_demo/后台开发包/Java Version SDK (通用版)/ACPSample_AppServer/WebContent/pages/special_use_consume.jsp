<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<h4>----------请求报文-------------------</h5>

<h4>1. 支付商户直接上送卡号并在银联页面锁定该卡号支付：</h6>
<p class="faq">
1. 需联系业务部门申请控件锁卡功能，如业务人员不清楚，可明确告知为管理平台“接收商户共享信息”权限。<br>
2. 需上送accNo<br>
  contentData.put("accNo", "6216261000000000018");  //需锁定的卡号<br>
3.如果也需要回显证件号码，姓名 送如下<br>
	Map<String,String> customerInfoMap = new HashMap<String,String>();<br>
	customerInfoMap.put("certifTp", "01");						    //证件类型<br>
	customerInfoMap.put("certifId", "341126197709218366");		    //证件号码<br>
	customerInfoMap.put("customerNm", "全渠道");					    //姓名<br>
	String customerInfoStr = AcpService.getCustomerInfo(customerInfoMap,null,DemoBase.encoding_UTF8);<br>	
	contentData.put("customerInfo", customerInfoStr);
</p>

<h4>-------------通知报文---------------------</h5>
<h4>3. 返回报文中返回卡号，卡类型，支付方式:</h6>
<p class="faq">
需在申请入网或者已经入网后给银联业务运营中心申请开通这3种权限<br>

</p>
