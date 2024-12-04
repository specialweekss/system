<template>
    <div class="white-box">
        <div class="phone-header">
            <span class="back-button" @click="goHome">←</span>
            <span class="header-title">人机训练测试</span>
        </div>
        <div class="phone-content">
            <div class="content-section">
                <div class="section-title">论述题</div>
                <div class="text-box" v-if="paper!==null">
                  <div>{{this.paper.backGround}}</div>
                   <div>问题 {{this.paper.question}}</div>
                </div>
              <div class="text-box" v-else></div>
            </div>
            <div class="content-section">
                <div class="section-title">模拟作答</div>
              <audio class="audio" controls v-if="audioUrl">
                <source :src="audioUrl" type="audio/webm">
              </audio>
                <div v-else>
                    <p class="instruction-text">请点击下方按钮开始作答</p>
                    <button class="start-button" @click="startAnswer" v-if="!isRecording">开始作答</button>
                    <button class="end-button" @click="stopRecording" v-else>停止作答</button>
                </div>
            </div>
            <div class="content-section">
              <button class='start-button' @click =this.getAnswer>AI作答</button>
                <div class="text-box">{{this.answer}}</div>
            </div>
        </div>
    </div>
</template>

<script>
import axios from "axios";

export default {
    props:['goHome','userId'],
   data(){
      return{
        paper:null,
        answer:'',
        score:'',
        isRecording:false,
        audioChunks:null,
        audioFile:null,
        audioUrl:null
      }
   },
  mounted() {
      this.getQuestion()
  },
  methods: {
      async getQuestion(){
          await axios.get(window.Ip+'/RandomPaper').then(response=>{
            console.log(response)
            this.paper=response.data.data
            if(this.paper.backGround === null)
            {
              this.paper.backGround = '材料：无'
            }
          })
      },
    async startAnswer() {
      this.isRecording = true;
      this.audioChunks = [];
      const stream = await navigator.mediaDevices.getUserMedia({ audio: true ,video:false});
      this.mediaRecorder = new MediaRecorder(stream);
      this.mediaRecorder.ondataavailable = event => {
        if (event.data.size > 0) {
          this.audioChunks.push(event.data);
        }
      };
      this.mediaRecorder.onstop = () => {
        this.audioFile = new Blob(this.audioChunks, { type: 'audio/webm' });
        this.audioUrl = URL.createObjectURL(this.audioFile);
        this.isRecording = false;
      };
      this.mediaRecorder.start();
    },
    stopRecording() {
      if (this.mediaRecorder && this.mediaRecorder.state !== 'inactive') {
        this.mediaRecorder.stop();
      }
    },
    async getAnswer()
    {

        this.answer = '正在进行分析...'
        const response = await axios.get(window.Ip+'/getAnswer?question=' + this.paper.question+'&back_ground=' + this.paper.backGround)
        this.answer=response.data.data;
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

.content-section {
    padding: 20px 0;
    border-bottom: 1px solid #E0E0E0;
}

.content-section:last-child {
    border-bottom: none;
}

.section-title {
    font-size: 18px;
    color: #333;
    font-weight: bold;
    margin-bottom: 15px;
    padding-left: 10px;
}

/* 新增样式 */
.text-box {
    width: 90%;
    height: 100px;
    margin: 10px auto;
    border: 1px solid #ccc;
    border-radius: 8px;
    padding: 10px;
    background-color: #f9f9f9;
    overflow-y: auto; /* 允许垂直滚动 */
}

.start-button {
    display: block;
    width: 80%;
    margin: 20px auto;
    padding: 12px;
    background-color: #4CAF50;
    color: white;
    border: none;
    border-radius: 25px;
    font-size: 16px;
    cursor: pointer;
}
.end-button {
  display: block;
  width: 80%;
  margin: 20px auto;
  padding: 12px;
  background-color: #ff0000;
  color: white;
  border: none;
  border-radius: 25px;
  font-size: 16px;
  cursor: pointer;
}

.start-button:hover {
    background-color: #45a049;
}

.instruction-text {
    text-align: center;
    color: #666;
    margin: 10px 0;
}
.audio{
  width: 100%;
}
.button{
  width: 100%;
}
</style>