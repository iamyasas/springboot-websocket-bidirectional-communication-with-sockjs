var stompClient = null;

function setConnected(connected) {
  $('#connect').prop('disabled', connected);
  $('#disconnect').prop('disabled', !connected);
  if (connected) {
    $('#conversation').show();
  } else {
    $('#conversation').hide();
  }
  $('#greetings').html('');
}

function connect() {
  var socket = new SockJS('http://localhost:8080/websocket');
  stompClient = Stomp.over(socket);
  stompClient.connect(
    {},
    function(frame) {
      setConnected(true);
      console.log('Connected: ' + frame);
      stompClient.subscribe('/user/reply/name', function(message) {
        showGreeting('Message ' + message.body);
      });
    }
  );
}

function disconnect() {
  if (stompClient !== null) {
    stompClient.disconnect();
  }
  setConnected(false);
  console.log('Disconnected');
}

function sendName() {
  stompClient.send('/app/name', {}, JSON.stringify({ name: $('#name').val() }));
}

function login() {
  $.ajax({
    url: 'http://localhost:8080/login',
    type: 'POST',
    contentType: 'application/json',
    headers: {
      Authorization:
        'Basic ' + btoa($('#userID').val() + ':' + $('#pass').val())
    },
    xhrFields: {
      withCredentials: true
    }
  });
}

function showGreeting(message) {
  $('#greetings').append('<tr><td>' + message + '</td></tr>');
}

$(function() {
  $('form').on('submit', function(e) {
    e.preventDefault();
  });
  $('#connect').click(function() {
    connect();
  });
  $('#disconnect').click(function() {
    disconnect();
  });
  $('#send').click(function() {
    sendName();
  });
  $('#login').click(function() {
    login();
  });
});
