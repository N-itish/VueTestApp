<template>
<div name = "cameraComp">
    <video id = "userVideo" ref = "video"></video>
   <!-- canvas store the object -->
    <canvas ref="canvas">
    </canvas>
</div>

</template>
<script>
import { eventBus } from '../../Mediator';
export default {
    name:"cameraComp",
    data(){
        return {
            image:null,
            stopInterval:0,
            canvas:null,
            websocketIns : null,
            recievedImage: null,
            reciever : 'global'
        }
    },
 
    mounted() {
        eventBus.$on('StartCall',(reciever)=>{
            this.reciever = reciever;
            this.sendVideo();
        })
        //getting the reciever from the Users component
        eventBus.$on('callUser',(reciever)=>{
            this.reciever = reciever;
            //send request to reciver for call
            this.callRequest();
        });
        //stopping the video
        eventBus.$on('stopCall',()=>{
            console.log('Video stopped');
            this.stopVideo();
        });

        //getting the websocket instance from HomePage
        eventBus.$on('WebSocketInstance',(instance)=>{
            this.websocketIns = instance;
        });
        this.setRefrences();
        
    },
    methods:{
        createMessage(message,messageType,reciever){
            var messageBody = {
                "reciever" : reciever,
                "message" : message,
                "messageType" : messageType 
            }
            return messageBody;
        },
        setRefrences(){
                this.video = this.$refs.video;
                this.image = this.$refs.image;
                this.canvas = this.$refs.canvas;
                this.video.height = 480;
                this.video.width = 640;
        },
        callRequest()
        {
            this.websocketIns.send(this.createMessage("callRequested","CALL",this.reciever))
        },
        startVideo(){
                var constrains = {
                //setting the constraints for the camera
                    video:{
                        width: { min: 640 }, height: { min: 480 }
                    }
                }
                //checking if the mediadevices is present or not
                if(navigator.mediaDevices && navigator.mediaDevices.getUserMedia) {
                    //getting the video from the camera
                    navigator.mediaDevices.getUserMedia(constrains).then(
                    stream => {
                        //setting the src of the video to the stream
                        this.video.srcObject = stream;
                        this.video.play();
                    });
                }else
                {
                    alert('please check if camera is present or not!!')
                }
                
            },
      
        stopVideo(){
            var mediaStream = this.video.srcObject;
            var tracks = mediaStream.getTracks();
            //getting all the records in the track and stopping them
            for(var i = 0 ; i< tracks.length;i++){
                //stopping every track 
                tracks[i].stop();
            }
            this.video = null;

        },
        //take pictures will only work if the startVideo function is called first and it works
        sendVideo(){
            this.startVideo();
            var context = this.canvas.getContext("2d");
            var video = this.video;
            var reciever = this.reciever;
            var canvas = this.canvas;
            var websocket  = this.websocketIns;
            //setting the canvas width and height
            context.canvas.width  = 480;
            context.canvas.height = 640;
            //sending image url to server
            setInterval(function(){
                context.drawImage(video, 0, 0, 640, 480);
                websocket.send(this.createMessage(canvas.toDataURL(),reciever,"CALL"));  
            },500);
              
        }
        
   },
}
</script>
<style scoped>
    #userVideo{
        display: none;
    }
</style>
