<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>CCOD</title>
</head>
<body>
<h2>状态：</h2>
<form action="/makecall" method="GET">
    主叫<br/>
    <input type="text" name="caller">
    <br/>
    被叫<br/>
    <input type="text" name="called">
    <br/>
    <input type="hidden" name="agentid" value="1001">
    <input type="submit" value="呼叫">
</form>
<form action="/dropcall" method="GET">
    <input type="hidden" name="agentid" value="1001">
    <input type="submit" value="挂断">
</form>

    <span id="advanced">
			<form action="/upload" method="GET">
				<input type="hidden" name="agentid" value="1001">
				<input type="submit" value="保存记录">
			</form>

			<form action="/lookupfile" method="GET">
				<input type="hidden" name="agentid" value="1001">
				<input type="submit" value="查找记录">
			</form>

			<form action="/lookup" method="GET">
				<input type="hidden" name="monitorid" value="1010">
				<input type="submit" value="查看正在通话">
			</form>

			<form action="/lookupactiveagent" method="GET">
				<input type="hidden" name="agentid" value="1001">
				<input type="submit" value="监听">
			</form>

			<form action="/lookupactiveagent" method="GET">
				<input type="hidden" name="agentid" value="1001">
				<input type="submit" value="强插">
			</form>

			<form action="/lookupactiveagent" method="GET">
				<input type="hidden" name="agentid" value="1001">
				<input type="submit" value="强拆">
			</form>

			<form action="/lookupactiveagent" method="GET">
				<input type="hidden" name="agentid" value="1001">
				<input type="submit" value="咨询">
			</form>

			<form action="/meet" method="GET">
				<input type="hidden" name="agentid" value="1001">
				<input type="hidden" name="agentid" value="1002">
				<input type="hidden" name="agentid" value="1003">
				<input type="submit" value="会议">
			</form>
		</span>
</body>
</html>
