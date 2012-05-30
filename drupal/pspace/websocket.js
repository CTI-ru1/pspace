//ff & chrome only

function message(value2add){
    onwsmessage(value2add);
}


var t=setTimeout("ping()",30000);
var sockets=new Array();
var sock_count=0;

//socket
function connect(hostname,node,capability){ //se full morfi ta nodes/capabilityies ( mazi me prefix)
	var mysock_id=sock_count;
	sock_count++;


    var host = "ws://"+hostname+"/readings.ws";

    var protocol = "SUB@"+node+"@"+capability;
    var encodedProtocol="";
    for (var i=0;i<protocol.length;i++){
        if (protocol[i]=="@"){
            encodedProtocol+=".";
        }
        else if(protocol[i]==":"){
            encodedProtocol+="-";
        }
        else{
            encodedProtocol+=protocol[i];
        }
    }
//    encodedProtocol=protocol;

    try{

   if(!("WebSocket" in window)){
       //  message("You have a browser that does not support Websockets!");
                 if(!("MozWebSocket" in window)){
                       //  message("You have a browser that does not support MozWebsockets!");
                         return -1;
                }


        else {
            sockets[mysock_id] = new MozWebSocket(host,encodedProtocol);

              //  message('You have a browser that supports MozWebSockets');
        }
    }
    else {
//        message("encodedProtocol="+encodedProtocol);
//        message("Protocol="+protocol);
        sockets[mysock_id] = new WebSocket(host,encodedProtocol);
//        message('You have a browser that supports WebSockets');
    }
        sockets[mysock_id].onopen = function(){
   //         message('socket.onopen ');
//            message('Socket Status: '+socket.readyState+' (open)'+"");
        }

        sockets[mysock_id].onmessage = function(msg){
            if (!(msg.data instanceof Blob)) {
                message(msg.data);
            }
        }
       sockets[mysock_id].onclose = function(){
  //          message("socket.onclose")
//            message('Socket Status: '+socket.readyState+' (Closed)'+"");
        }

	var t=setTimeout("ping()",30000);

    } catch(exception){
         //   message('Error'+exception+"");
    }

}

function ping()
{
	var i=0;
	for (i=0;i<sock_count;i++){
    	    sockets[i].sendsocket.send("ping");
	}
	var t=setTimeout("ping()",30000);

}

