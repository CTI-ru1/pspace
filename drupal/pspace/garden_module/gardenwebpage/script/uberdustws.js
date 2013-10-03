var t = setTimeout("ping()", 30000);
var sockets = new Array();
var sock_count = 0;

function connect(hostname, node, capability, action) {
    console.log(sock_count);
    var mysock_id = sock_count;
    sock_count++;

    var host = "ws://" + hostname + "/readings.ws";

    var protocol = "SUB@" + node + "@" + capability;
    var encodedProtocol = protocol.replace(/@/g, ".").replace(/:/g, "--");


    try {

        if (!("WebSocket" in window)) {
            console.log("You have a browser that does not support Websockets!");
            if (!("MozWebSocket" in window)) {
                console.log("You have a browser that does not support MozWebsockets!");
                return -1;
            }
            else {
                sockets[mysock_id] = new MozWebSocket(host, encodedProtocol);
                console.log("You have a browser that supports MozWebSockets");
            }
        }
        else {
            sockets[mysock_id] = new WebSocket(host, encodedProtocol);
        }
        sockets[mysock_id].onopen = function () {
            console.log("socket.onopen");
        }

        sockets[mysock_id].onmessage = function (msg) {
            if (!(msg.data instanceof Blob)) {
                if (msg.data != "pong") {
                    action(msg.data);

                }
            }
        }
        sockets[mysock_id].onclose = function () {
            console.log("socket.onclose");
        }


    } catch (exception) {
        console.log('Error' + exception + "");

    }


}//End connect

function ping() {
    var i = 0;
    for (i = 0; i < sock_count; i++) {
        send("ping", i);
    }
    var t = setTimeout("ping()", 30000);
}

function send(text, sock_id) {
    if (text == "") {
        //console.log('Please enter a message'+"");
        return;
    }
    try {
        sockets[sock_id].send(text);
        //console.log('Sent: '+text+"")
    } catch (exception) {
        //console.log('Exception '+"");
    }
}

function disconnect(sock_id) {
    sockets[sock_id].close();
    console.log("Connection Closed!");
}

