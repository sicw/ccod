<%--
  Created by IntelliJ IDEA.
  User: sicwen
  Date: 2019/3/7
  Time: 16:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <title>响应</title>
    <script src="http://lib.sinaapp.com/js/jquery/1.9.1/jquery-1.9.1.min.js"></script>
</head>
<body>
    用户：<p id="username"></p>
    <br/>
    坐席状态：<p id="callerstatus">初始化</p>
    <br/>
    外线状态：<p id="calledstatus">初始化</p>
    <script type="text/javascript">
        var getting = {
            // 如何发送agentID和sessionID
            // agentID在登陆时会保存到session中
            // 在点击makeCall时会创建sessionID，保存到redis中。
            url:'http://localhost:8081/ccod-ucds/ajax?username=${requestScope.username}&sessionId=${requestScope.sessionId}',
            dataType:'json',
            success:function(res) {
                console.log(res);
                document.getElementById("callerstatus").innerHTML=res.callerstatus;
                document.getElementById("calledstatus").innerHTML=res.calledstatus;
                document.getElementById("username").innerHTML=res.username;
            }
        };
        //关键在这里，Ajax定时访问服务端，不断获取数据 ，这里是1秒请求一次。
        window.setInterval(function(){$.ajax(getting)},1000);
    </script>
</body>
</html>
