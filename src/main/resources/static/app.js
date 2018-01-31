

var stompClient = null;
var token = "";
function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#send").prop("disabled", false);
        $("#schema").prop("disabled", false);
        $("#inputSchema").show();
    }
    else {
        $("#conversation").hide();
        $("#send").prop("disabled", true);
        $("#schema").prop("disabled", true);
        $("#inputSchema").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var objUsername = $("#name");
    objUsername.prop('disabled', true);
    token = CryptoJS.MD5(objUsername.val()).toString()
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        var url='/topic/greetings/'+token;
        stompClient.subscribe(url, function (greeting) {
            showGreeting(JSON.parse(greeting.body).data);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    $("#conversation").show();
    var obj = $("#schema");
    stompClient.send("/app/hello", {}, JSON.stringify({'name': token, 'schema' : obj.val()}));
    $("#send").prop('disabled', true);
    obj.prop('disabled', true);
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
});