<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<div style="font-size: 13px" >
测试卡号：与银联测试环境联调使用的卡号 <a href="https://open.unionpay.com/ajweb/help/faq/list?ha=%E6%B5%8B%E8%AF%95%E5%8D%A1%E5%8F%B7" target="_blank">测试卡号</a><br><br>

<a href="pages/special_use_consume.jsp" target="_blank">控件支付交易报文特殊用法</a><br>
<br>
<b>
交易流程：通过调用消费交易获取tn号提供给前端调起控件使用<br><br>
控件支付成功后，后续可以做退货，消费撤销，对账文件下载交易；为了确认消费，退货，消费撤销交易是否成功也可以开发交易状态查询交易<br><br>
</b>
交易状态查询说明：<br>
origrespcode=00成功，03、04、05重新查询，其他为失败。<br><br>
消费撤销交易与退货：<br>
消费撤销和退货有什么区别？<br>
消费撤销仅能对当天的消费做，必须为全额，一般当日或第二日到账，可能存在极少数银行不支持。<br>
退货能对11个月内的消费做（包括当天），支持部分退货或全额退货，到账时间较长，一般1-10天（多数发卡行5天内，但工行可能会10天），所有银行都支持。<br>
注：以上的天均指清算日，一般前一日23点至当天23点为一个清算日。测试环境为测试需要，13:30左右日切，所以13:30到13:30为一个清算日。<br><br>
对账文件下载：<br>
对账文件什么时候能下载？<br>
测试环境一般下午5点出，文件内包含的交易的时间范围是13:30-13:30。<br>
生产环境一般早上9点出，文件内包含的交易的时间范围是23:00-23:00。<br><br>
对账文件获取后会落地成一个zip文件，zip文件中的ZM，ZME文件各个字段的拆分解析可以参考DemoBase.java中的parseZMFile parseZMEFile 方法。<br>
</div>
<br>
<br>