<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>demo for MSMQ</title>
    <script src="http://libs.baidu.com/jquery/2.0.0/jquery.min.js"></script>
    <script src="https://cdn.bootcss.com/sockjs-client/1.1.4/sockjs.min.js"></script>
    <script src="https://cdn.bootcss.com/stomp.js/2.3.3/stomp.min.js"></script>
</head>
<body>
<form>
    <label>send message : </label>
    <input type="text" id="message"/>
    <button id="sendBtn" onclick="send()" type="button">send</button>
</form>
<label>receive message : </label>
<div id="messagesBox"></div>
<script>
    function send() {
        var message = $("#message").val();
        console.log(message);
        $.post("/send", {message: message}, function (resp) {
            console.log(JSON.stringify(resp));
        });
    }

    var socket = new SockJS('/endpoint1');
    var stompClient = Stomp.over(socket);
    stompClient.connect({}, function() {
        stompClient.subscribe('/topic', function(respnose){
            var messageBox = $('#messagesBox');
            messageBox.append("<p>"+respnose.body+"</p>")
        });
    });

</script>
</body>
</html>