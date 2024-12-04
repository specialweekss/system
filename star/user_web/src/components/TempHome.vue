<template>
  <div class="white-box" v-if="!logIn&&!showRegister">
    <div class="page" >
      <h1 id="welcome" >欢迎登录</h1>
      <div class="denglukuang">
        <input class="inputstring" type="text" placeholder="账号" v-model="userId" />
        <br/><br/>
        <input class="inputstring" type="password" placeholder="密码" v-model="password" />
        <br/>
        <a type="text" class="register-link" @click="registerOn">立即注册</a>
        <br/>
        <button class="button" type="button" @click="login">登录</button>
      </div>
    </div>
  </div>
  <div v-if="logIn">
  <model-select v-if="this.type === 0" :user-id="userId" :go-home="goHome"></model-select>
  <ExpertAppointment v-if="this.type === 1" :expert-id="userId" :go-home="goHome"></ExpertAppointment>
  </div>
  <register v-if="showRegister" :go-home="goHome"></register>
</template>

<script>
import modelSelect from "@/components/ModelSelect.vue";
import ExpertAppointment from "@/components/ExpertAppointment.vue";
import axios from "axios";
import register from "@/components/registerPage.vue";
export default {
  data() {
    return {
      password: '',
      logIn:false,
      userId:'',
      type:null,
      showRegister:false
    };
  },
  components:{
    register,
    modelSelect,
    ExpertAppointment
  },
  methods: {

    goHome(){
      this.logIn=false
      this.showRegister=false
    },
    registerOn(){
      this.showRegister=true
      console.log(this.showRegister)
    },
    async login() {
      console.log(this.showRegister)
      let idNum=Number(this.userId)
      if(this.password===''||this.userId==='')
      {
        alert('请输入账号密码！')
        return;
      }

      else if(idNum.toString()!==this.userId)
      {
        alert('账号格式错误，应为账号id！')
        return;
      }
      const response=await axios.get(window.Ip+'/LogIn?password='+this.password+'&userId='+this.userId)
      this.type=response.data.data.type
      console.log(response);
      if(response.data.code===400){ alert('账号或密码错误！');}
      else
        this.logIn=true;
    },
  },
};
</script>

<style scoped>
@import '@/css/inputstring.css';
body, html {
  margin: 0;
  padding: 0;
  height: 100%;
  background-color: rgba(225, 240, 231, 1);
  position: relative;
}

/* 主要内容区域的白色框(手机样式) */
.white-box {
  position: fixed;
  top: 50%;
  left: 50%;
  width: 25%;
  height: 80%;
  transform: translate(-50%, -50%);
  background-color: white;
  border-radius: 50px;
  box-shadow: 0 0 15px rgba(0, 0, 0, 0.5);
  overflow-y: auto;
  padding: 0;
}

#welcome {
  left: 100px;
  top: 82px;
  width: 309px;
  height: 117px;
  border: 1px solid rgba(209, 232, 220, 1);
  text-shadow: 0px 2px 4px rgba(0, 0, 0, 0.25);
  backdrop-filter: blur(4px);
  font-size: 72px;
  font-weight: 400;
  letter-spacing: 0;
  line-height: 99.79px;
  color: rgba(255, 235, 59, 1);
  text-align: left;
  vertical-align: top;
}

input{
  margin: 0;
  padding: 0;
}
img {
  position: absolute;
  left: 948px;
  top: 188px;
  width: 972px;
  height: 774px;
}

.denglukuang {
  margin: auto;
  top: 233px;
  width: 90%;
  height: 500px;
  border-radius: 90px;
  background: rgba(255, 255, 255, 1);
  border: 1px solid rgba(0, 0, 0, 1);
}

.button{
  left: 47px;
  top: 150px;
  width: 80%;
  height: 80px;
  border-radius: 20px;
  background: linear-gradient(180deg, rgba(161, 140, 209, 1) 0%, rgba(251, 194, 235, 1) 100%);
  box-shadow: 0px 2px 4px  rgba(0, 0, 0, 0.25);
  position: relative;
  font-size: 60px;
  font-weight: 400;
  letter-spacing: 0px;
  line-height: 86.88px;
  color: rgba(242, 220, 220, 1);
  text-align: center;
  vertical-align: top;
}
button:hover {
  background: linear-gradient(180deg, rgba(39, 159, 245, 1) 0%, rgba(55, 13, 191, 1) 10%);
}
input {
  margin: 0;
  padding: 0;
}
.page{
  position: fixed;
  height: 100%;
  width: 100%;
  background-color: rgba(152, 137, 185, 1);
}
.register-link {
  position: relative;
  left:80%;
  top:140px;
  transform: translateX(-50%);
  margin-top: 1000px;
  color: crimson;
  text-decoration: none;
  font-size: 18px;
  font-weight: 400;
  letter-spacing: 0;
  line-height: 25px;
}
</style>
