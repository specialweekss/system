<template>
    <div class="white-box" v-if="!appointmentOn&&!testOn">
      <!-- 顶部导航栏 -->
      <div class="phone-header">
        <span class="back-button" @click="goHome">←</span>
        <span class="header-title">模块选择</span>
      </div>
        <div class="phone-content">
            <div class="interview-options">
                <button class="option-button" @click="goTest">基础模块</button>
                <button class="option-button" @click="goAppointment">线上模拟</button>
            </div>
        </div>
    </div>
  <expert-list v-else-if="appointmentOn" :user-id="userId" :go-home="backHere" ></expert-list>
  <TestPage v-else-if="testOn" :go-home="backHere" :user-id="userId"></TestPage>
</template>

<script>
import expertList from "@/components/ExpertList.vue";
import TestPage from "@/components/TestPage.vue";
export default {
    props:['goHome','userId'],
    data(){
      return{
        appointmentOn:false,
        testOn:false
      }
    },
  components:{
      expertList,
      TestPage
  },
  methods:{
    goAppointment(){
      this.appointmentOn=true
      this.testOn=false
    } ,
    goTest(){
      this.appointmentOn=false
      this.testOn=true
    },
    backHere(){
      this.appointmentOn=false
      this.testOn=false
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

.question {
    text-align: center;
    font-size: 20px;
    color: #333;
    margin-bottom: 40px;
    font-weight: bold;
}

.interview-options {
    display: flex;
    flex-direction: column;
    gap: 35px;
    padding: 0 10px;
}

.option-button {
    padding: 15px 20px;
    border: none;
    border-radius: 12px;
    background-color: #E8F5E9;
    color: #2E7D32;
    font-size: 16px;
    cursor: pointer;
    transition: all 0.3s ease;
    box-shadow: 0 2px 5px rgba(0,0,0,0.1);
}

.option-button:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 8px rgba(0,0,0,0.15);
    background-color: #C8E6C9;
}
</style>