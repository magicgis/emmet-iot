var stompClient = null;

function setConnected(connected) {
    document.getElementById('connect').disabled = connected;
    document.getElementById('disconnect').disabled = !connected;
    document.getElementById('conversationDiv').style.visibility = connected ? 'visible' : 'hidden';

}

function connect() {
    stompClient = Stomp.client('ws://localhost:8080/register');
    stompClient.debug = null;
    stompClient.connect({}, function(frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        showMessage("")
    });
}

function sendDeviceName() {
    var name = document.getElementById('name').value;
    stompClient.subscribe('/topic/' + name, function(mesage){
        showMessage(mesage.body);
    });
}


function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}


function showMessage(message) {
    var response = document.getElementById('response');
   // console.log(message);
    response.innerHTML = message;

}

