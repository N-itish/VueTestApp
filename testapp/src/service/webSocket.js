import SockJS from "sockjs-client";
import Stomp from "webstomp-client";
import {eventBus}  from '../Mediator';
const url = "http://localhost:8090/gs-guide-websocket";
var headers;
export default class webSocket{
    static headers = [];
    constructor(){
        this.socket = new SockJS(url);
        this.stompClient = Stomp.over(this.socket);
        this.stompClient.debug = function(){};

    }
    setAuth(username,password){
       headers = {
           login: username,
           passcode: password
       }
    }
    connect(){
        this.stompClient.connect(
            headers,  
            () => {
              this.stompClient.subscribe("/user/topic/greeting", tick => {
                        eventBus.$emit('message',tick.body);         
              });
            },
            err => {
              this.disconnect(err)
            }); 
    }

    send(message){
        this.stompClient.send("/app/private",JSON.stringify(message),{});
    }

    disconnect(err){
        console.log('disconnected due to error :'+ err);
        this.stompClient = null;
    }
}