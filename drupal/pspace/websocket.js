//ff & chrome only

//write to a div

function message(value2add){
    onwsmessage(value2add);
}
/*
function clearMessages(){
    document.getElementById('area').value="";
}
*/



//socket
function connect(hostname,node,capability){ //se full morfi ta nodes/capabilityies ( mazi me prefix)

 
    var socket;
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
     //    message("You have a browser that does not support Websockets!");
                 if(!("MozWebSocket" in window)){
                    //     message("You have a browser that does not support MozWebsockets!");
                         return -1;
                }


        else {
            socket = new MozWebSocket(host,encodedProtocol);
               // message('You have a browser that supports MozWebSockets');
        }
    }
    else {
//        message("encodedProtocol="+encodedProtocol);
//        message("Protocol="+protocol);
        socket = new WebSocket(host,encodedProtocol);
     //   message('You have a browser that supports WebSockets');
    }
        socket.onopen = function(){
		ping(socket);
        //    message('socket.onopen ');
//            message('Socket Status: '+socket.readyState+' (open)'+"");
        }

		
        socket.onmessage = function(msg){
            if (!(msg.data instanceof Blob)) {  //mporw na to dw sto uberdust live
                message(msg.data);
            }
        }
       socket.onclose = function(){
           // message("socket.onclose")
//            message('Socket Status: '+socket.readyState+' (Closed)'+"");
        }

	var t=setTimeout("ping()",30000);

    } catch(exception){
         //   message('Error'+exception+"");
    }

}

function ping()
{
        socket.send("ping");
	var t=setTimeout("ping()",30000);
}

