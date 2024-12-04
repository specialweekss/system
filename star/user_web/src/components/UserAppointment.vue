
<template>
  <div class="white-box" v-if="!this.videoOn&&!resultOn">
    <div class="phone-header">
      <span class="back-button" @click="goHome">←</span>
      <span class="header-title">面试预约</span>
    </div>
    <div class="page-title">当前预约</div>
    <div >
      <h1 v-if="msg!==''">{{this.msg}}</h1>
      <div class="appointment-card" v-for="(value,index) in appointmentList" :key="index">
        <div class="card-info">面试单号：{{ value.appointmentId }}</div>
        <div class="card-info">预约专家ID：{{ value.expertId }}</div>
        <div class="card-buttons">
          <button class="cancel-btn" v-if="value.state ===-1" @click="cancel(value.appointmentId)">取消预约</button>
          <button class="enter-btn" v-if="value.state ===-1" @click="goVideo(value.expertId,value.appointmentId)">进入面试</button>
          <button class="enter-btn" v-else-if="value.state === 0" @click="getAppointmentList">等待专家评分</button>
          <button  class="enter-btn" v-else @click="goResult(value)">查看评价：{{value.score}}</button>
        </div>
      </div>
    </div>

  </div>

  <video-page v-if="videoOn" :user-id="userId" :to="expertId" :go-home="backHere" :appointment-id="this.appointmentId" :type=0></video-page>
  <appointment-result v-if="resultOn"  :go-home="backHere" :user-id="userId"  :appointment-id="appointmentId"></appointment-result>
</template>




<script>
import axios from "axios";
import videoPage from "@/components/VideoPage.vue";
import AppointmentResult from "@/components/AppointmentResult.vue";
export default {
  props: ['userId','goHome'],
  data(){
    return{
      appointmentList:[],
      expertId:null,
      msg:'加载中',
      videoOn:false,
      resultOn:false,
      appointmentId:null
    }
  },
  components:{
    videoPage,
    AppointmentResult
  },
  mounted() {
    this.getAppointmentList()
  },
  methods:{
    async getAppointmentList(){
      await axios.get('https://localhost:8090/AppointmentByUser?userId='+this.userId).then((response) => {
        if (response.data.total > 0){
          console.log(response.data)
          this.appointmentList = response.data.data;
          this.msg = ''
        }
        else {
          this.msg = '暂无预约信息';
        }
      })
    },
    async goVideo(expertId,appointmentId){
      this.videoOn = true
      this.resultOn=false
      this.expertId = expertId
      this.appointmentId = appointmentId
    },
    goResult(value){
      this.resultOn = true
      this.videoOn = false
      this.appointmentId = value.appointmentId
    },
    async cancel(appointmentId){
      await axios.post(window.Ip+'/CancelAppointment?appointmentId='+appointmentId).then(response=>{
        console.log(response)
        alert('面试已取消')
        this.getAppointmentList()
      })
    },
    backHere(){
      this.videoOn = false
      this.resultOn =false
      this.expertId = null
      this.getAppointmentList()
    }
  }
}
</script>



<style scoped>
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

/* 手机顶部栏样式 */
.phone-header {
  background-color: #4CAF50;
  color: white;
  padding: 15px 20px;
  display: flex;
  align-items: center;
  position: sticky;
  top: 0;
  justify-content: center;
}

.back-button {
  font-size: 20px;
  margin-right: 15px;
  cursor: pointer;
  position: absolute;
  left: 20px;
}

.header-title {
  font-size: 18px;
  font-weight: bold;
}

/* 主体内容样式 */
.page-title {
  font-size: 24px;
  font-weight: bold;
  color: #4CAF50;
  margin: 20px;
}

.appointment-card {
  margin: 15px;
  padding: 15px;
  border-radius: 10px;
  box-shadow: 0 2px 5px rgba(0,0,0,0.1);
  background-color: #f8f8f8;
}

.card-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin-bottom: 10px;
}

.card-info {
  margin: 5px 0;
  color: #666;
}

.card-buttons {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 15px;
}

.cancel-btn {
  padding: 8px 15px;
  border: none;
  border-radius: 5px;
  background-color: #ff5252;
  color: white;
  cursor: pointer;
}

.enter-btn {
  padding: 8px 15px;
  border: none;
  border-radius: 5px;
  background-color: #4CAF50;
  color: white;
  cursor: pointer;
}

.popup {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: rgba(95, 95, 95, 0.9);
  color: white;
  padding: 30px 30px;
  border-radius: 25px;
  z-index: 1000;
  box-shadow: 0 4px 8px rgba(0,0,0,0.2);
}
</style>