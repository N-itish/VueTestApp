<template>
  <div id="adminPanel">
      <div id = "Header">
          <router-link :to="{name:'list'}"> Display Users </router-link>
          <router-link :to="{name:'changePassword'}">Change Password</router-link>
      </div>
      <router-view></router-view>
      
      <!-- <list-view v-if="showList" v-on:userDetails="getUserDetails"></list-view>
      <detailed-view  v-if="showDetails" :userData = "detailedUserData"></detailed-view>
      <change-password v-if="showPasswordChange"></change-password> -->
      <button id = "logout">Logout</button>
  </div>
</template>
<script>

export default {
  name:"adminPanel",
  data(){
    return{
      detailedUserData:[]
    };
  },methods:{
    getUserDetails(value){
      this.detailedUserData = value;
    }
  },
}
</script>
<style scoped>
#Header{
  top:0%;
  font-size: 20px;
  background-color: black;
  vertical-align: middle;
  padding: 1em;
}
label{
  margin-left: 20px;
  color: white;
  padding: 20px;
}
label:hover{
  color:black;
  background-color: white;
}
#logout{
  float: right;
  padding:10px;
  position:relative;
  margin-top:40%;
  margin-right : 20px;
}
</style>


