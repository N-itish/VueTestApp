<template>
    <div id = "UserView">
            <label>find All users</label>
            <button v-on:click ="findUsers">Search</button>
            <ul v-for="(user,index) in users" :key="index">
                <li v-bind:class="{'selected': index == selectedIndex}" v-on:click ="getUser(user,index)">{{user}}</li>
                <button v-on:click ="callUser(user)">call</button><button v-on:click ="cancelCall(user)">stop</button>
            </ul><br>
    </div>
</template>
<script>
import {eventBus} from '../../Mediator';
import userAPI from '../../service/userAPI';
export default {
    name:'UserView',
    data(){
        return{
            selectedIndex : null,
            users:[],
            groupMembers:[],
            member:null,
        }
    },
    methods:{
        getUser(reciever,index){
            this.selectedIndex = index;
            //sending selected user from this component to the homepage
            this.$emit('childToParent', reciever)
        },
        findUsers(){
            userAPI.instance.get('/getUsername').then((response)=>{
                this.users = response.data;
            });
        },
        callUser(reciever){
            //console.log('call use component called')
            eventBus.$emit('callUser',reciever);
           
        },cancelCall(){
            eventBus.$emit('stopCall');
        }
    } 
}
</script>
<style scoped>
 .selected{
        background-color: black;
        color: white;
    }
</style>