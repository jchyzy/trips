'use strict';

document.querySelector('#chatWindow').addEventListener('submit', sendMessage, true)
document.querySelector('#exitChat').addEventListener('click', leaveChat, true)

var stompClient = null;
var socket = new SockJS('/websocketApp');
stompClient = Stomp.over(socket);

stompClient.connect({}, connectionSuccess);


function sendMessage(event) {

    var messageContent = document.querySelector('#chatMessage').value.trim();
    if (messageContent ) {

        var chatMessage = {
            sender : user,
            message : document.querySelector('#chatMessage').value,
            type : 'CHAT'
        };

        stompClient.send("/app/chat.sendMessage."+trip.toString(), {}, JSON
            .stringify(chatMessage));
        document.querySelector('#chatMessage').value = '';
    }
    event.preventDefault();
}

function connectionSuccess() {
    console.log('Connected successfully');
    var topic = '/topic/chat.'+trip.toString();
    stompClient.subscribe(topic, onMessageReceived);
    stompClient.send("/app/chat.newUser."+trip.toString(), {}, JSON.stringify({
        sender : user,
        type : 'newUser'
    }))
}

function leaveChat(event) {

    stompClient.send("/app/chat.leave."+trip.toString(), {}, JSON.stringify({
        sender : user,
        type : 'Leave'
    }))
    // event.preventDefault();

}

function onMessageReceived(payload) {
    console.log("message");
    var message = JSON.parse(payload.body);
    console.log(message)
    var messageElement = document.createElement('li');

    if (message.type === 'newUser') {
        messageElement.classList.add('event-data');
        message.message = message.sender + ' has joined the chat';
    } else if (message.type === 'Leave') {
        messageElement.classList.add('event-data');
        message.message = message.sender + ' has left the chat';
    } else {
        messageElement.classList.add('message-data');

        var element = document.createElement('i');
        messageElement.appendChild(element);

        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(message.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
    }

    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.message);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    document.querySelector('#messageList').appendChild(messageElement);
    document.querySelector('#messageList').scrollTop = document
        .querySelector('#messageList').scrollHeight;

}