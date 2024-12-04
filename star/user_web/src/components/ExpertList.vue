<template>
    <!-- 主容器 -->


      <div v-if="!appointmentOn">
        <div class="white-box">
          <!-- 顶部导航栏 -->
          <div class="phone-header">
            <span class="back-button" @click="goHome">←</span>
            <span class="header-title">专家列表</span>
          </div>
        <el-button size="large" @click="goAppointment" class="button">查看预约记录</el-button>
        <!-- 专家列表区域 -->
        <div class="expert-list" v-if="experts!==null">
            <div v-for="expert in experts" :key="expert.expertId" class="expert-item">
                <div class="expert-info">
                    <div class="expert-name">{{ expert.name }}</div>
                    <div class="expert-rating">{{ expert.introduction }}</div>
                </div>
                <div class="expert-buttons">
                    <button class="appointment-btn" @click="makeAppointment(expert.expertId)">+预约</button>
                </div>
            </div>
        </div>
        <div v-else>
          暂无可见专家
        </div>
      </div>
      </div>
       <user-appointment v-else :user-id="userId" :go-home="backHere"></user-appointment>

</template>

<script>
import axios from "axios";
import userAppointment from "@/components/UserAppointment.vue";
export default {
    props:['goHome','userId'],
    data() {
        return {
            experts: null,
            appointmentOn:false
        }
    },
  components:{
    userAppointment
  },
  mounted() {
      this.initExperts()
  },
  methods: {
       async initExperts(){
          await axios.get(window.Ip+'/ExpertList').then(response=>{
            if(response.data.total!==0)
              this.experts=response.data.data
          })
        },
        async makeAppointment(expertId) {
         console.log(expertId)
            await axios.post(window.Ip+'/AddAppointment',{
              expertId:expertId,
              userId : this.userId
            }).then(response=>{
              console.log(response)
              if(response.data.code ===200)
              {
                alert('预约成功！')
              }
              else
                alert('不可重复预约')
                }
            )
        },
        goAppointment(){
         this.appointmentOn=true
        },
        backHere(){
         this.appointmentOn= false
         this.initExperts()
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
    transform: translate(-50%, -50%); /* 居中定位 */
    background-color: white;
    border-radius: 50px; /* 圆角边框 */
    box-shadow: 0 0 15px rgba(0, 0, 0, 0.5); 
    padding: 0;
    display: flex;
    flex-direction: column;
    overflow: hidden; /* 防止内容溢出 */
}

/* 手机顶部栏样式 */
.phone-header {
    background-color: #4CAF50; 
    color: white;
    padding: 15px 20px;
    display: flex;
    align-items: center;
    position: sticky; /* 固定在顶部 */
    top: 0;
    justify-content: center;
    border-radius: 50px 50px 0 0; /* 顶部圆角 */
    z-index: 1;
}

/* 返回按钮样式 */
.back-button {
    font-size: 20px;
    margin-right: 15px;
    cursor: pointer;
    position: absolute;
    left: 20px;
}

/* 标题样式 */
.header-title {
    font-size: 18px;
    font-weight: bold;
}

/* 专家列表容器样式 */
.expert-list {
    padding: 15px;
    flex: 1;
    overflow-y: auto; /* 添加垂直滚动条 */
    -webkit-overflow-scrolling: touch; /* 支持iOS滚动 */
}

/* 自定义滚动条样式 */
.expert-list::-webkit-scrollbar {
    width: 6px;
}

.expert-list::-webkit-scrollbar-track {
    background: #f1f1f1;
    border-radius: 3px;
}

.expert-list::-webkit-scrollbar-thumb {
    background: #888;
    border-radius: 3px;
}

.expert-list::-webkit-scrollbar-thumb:hover {
    background: #555;
}

/* 单个专家卡片样式 */
.expert-item {
    display: flex;
    padding: 15px 0;
    border-bottom: 1px solid #eee; /* 底部分隔线 */
    align-items: center;
}

/* 专家头像样式 */
.expert-avatar {
    width: 60px;
    height: 60px;
    border-radius: 50%; 
    margin-right: 15px;
}

/* 专家信息区域样式 */
.expert-info {
    flex: 1; /* 占据剩余空间 */
}

/* 专家姓名样式 */
.expert-name {
    font-size: 16px;
    font-weight: bold;
    margin-bottom: 5px;
}

/* 专家评分样式 */
.expert-rating {
    color: #333333; /* 金色星星 */
}

/* 按钮容器样式 */
.expert-buttons {
    display: flex;
    flex-direction: column;
    gap: 5px;
}

/* 通用按钮样式 */
.follow-btn, .appointment-btn {
    padding: 5px 10px;
    border: none;
    border-radius: 15px;
    cursor: pointer;
}

/* 关注按钮样式 */
.follow-btn {
    background-color: #4CAF50; 
    color: white;
}

/* 预约按钮样式 */
.appointment-btn {
    background-color: #2196F3; 
    color: white;
}
.button{
  width: 100%;
}
</style>