<template>
    <div class="white-box">
        <div class="phone-header">
            <span class="back-button" @click="goHome">←</span>
            <span class="header-title">结果分析</span>
        </div>
        <div class="phone-content">
            <div class="name-section">{{ name }}</div>
            
            <div class="stats-section">
                <div class="stat-item">
                    <div class="stat-label">得分</div>
                    <div class="stat-value" v-if="this.appointment!==null">{{ this.appointment.score }}</div>
                </div>
            </div>

            <div class="overall-score">
                <div class="score-label">综合评分</div>
                <div class="score-value">{{ grade }}</div>
            </div>

            <div class="feedback-section">
                <div class="feedback-title">专家总结及建议</div>
                <div class="feedback-content" v-if="appointment!==null">
                    {{ this.appointment.feedBack }}
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import axios from "axios";

export default {
    props:['goHome','userId','appointmentId'],
    data() {
        return {
            name: null,
            grade: null,
            appointment: null,
        }
    },
  mounted() {
      this.getUser()
    this.getFeedBack()
  },
  methods:{
    async getUser(){
      console.log(this.userId)
      await axios.get(window.Ip+'/getUserById?userId='+this.userId).then(response => {
        this.name=response.data.data.name
      })
    },
    async getFeedBack(){
      await axios.get(window.Ip+'/AppointmentById?appointmentId='+this.appointmentId).then(response => {

        this.appointment=response.data.data
        if(this.appointment.score<60){
          this.grade = 'C'
        }
        else if (this.appointment.score>60&&this.appointment.size<80){
          this.grade = 'B'
        }
        else {
          this.grade = 'A'
        }
      })
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
.phone-content {
    padding: 30px 20px;
}

.name-section {
    text-align: center;
    font-size: 24px;
    color: #333;
    margin-bottom: 30px;
    font-weight: bold;
}

.stats-section {
    display: flex;
    justify-content: space-between;
    margin-bottom: 30px;
    padding: 15px;
    background-color: #E8F5E9;
    border-radius: 12px;
}

.stat-item {
    text-align: center;
}

.stat-label {
    font-size: 14px;
    color: #666;
}

.stat-value {
    font-size: 24px;
    color: #2E7D32;
    font-weight: bold;
}

.overall-score {
    text-align: center;
    margin-bottom: 30px;
    padding: 20px;
    background-color: #E8F5E9;
    border-radius: 12px;
}

.score-label {
    font-size: 16px;
    color: #666;
    margin-bottom: 10px;
}

.score-value {
    font-size: 36px;
    color: #2E7D32;
    font-weight: bold;
}

.feedback-section {
    padding: 20px;
    background-color: #E8F5E9;
    border-radius: 12px;
}

.feedback-title {
    font-size: 16px;
    color: #333;
    margin-bottom: 10px;
    font-weight: bold;
}

.feedback-content {
    font-size: 14px;
    color: #666;
    line-height: 1.6;
}
</style>