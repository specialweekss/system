<template>
  <div class="white-box">
    <div class="phone-header">
      <span class="back-button" @click="goHome">←</span>
      <span class="header-title">面试中</span>
    </div>
    <div class="phone-content">
      <div class="video-container">
          <video ref="localVideo" autoplay playsinline class="local-video"> </video>
          <video ref="remoteVideo" autoplay playsinline class="remote-video"></video>
      </div>
      <button class="end-interview-btn" v-if="!connection_on"  @click="startCall">开始面试</button>
      <button class="end-interview-btn" v-if="connection_on" @click="endCall">结束面试</button>



      <div class="expert-section">
        <div class="expert-title">专家名片</div>
        <div class="expert-card" v-if="expert!==null">
          <div class="expert-info">姓名：{{expert.name}}</div>
          <div class="expert-info">简介：{{expert.introduction}}</div>
        </div>
      </div>
      <div v-if="be_called">
        <div >你有来自{{this.to}}的来电，请准备开始面试</div>
        <el-button @click="goCall" size="large">准备好了！</el-button>
      </div>
    </div>
  </div>
</template>
<script>
import axios from "axios";
export default {
  props:['goHome','userId','to','appointmentId','type'],
  data() {
    return {
      expert:null,
      localStream: null,
      peerConnection: null,
      configuration: { iceServers: [{ urls: 'stun:stun.l.google.com:19302' }] },
      socket: null,
      be_called:false,
      to_adp:null,
      video_on:false,
      connection_on :false
    };
  },
  mounted() {
    this.initWebSocket();
    this.getExpert();
  },
  methods: {
    initPeerConnection() {
      this.peerConnection.ontrack = (event) => {
        if (event.streams && event.streams.length > 0) {
          this.remoteStream = event.streams[0];
          this.$refs.remoteVideo.srcObject = this.remoteStream;
          console.log(this.$refs.remoteVideo.srcObject );
          console.log(  this.$refs.localVideo.srcObject)
        }
      };
      this.peerConnection.onicecandidate = event => {
        if (event.candidate) {
          // 新的ICE候选者可用
          console.log('New ICE candidate:', event.candidate.toString());
          this.sendSignalingMessage({type:'candidate',candidate:JSON.stringify(event.candidate),from:this.userId,to:this.to});
        } else {
          // 所有的ICE候选者收集完毕
          console.log('All ICE candidates have been collected.');
        }
      };
    },
    async getExpert(){
      if(this.type === 1){
        await axios.get(window.Ip+'/ExpertById?expertId='+this.userId).then(response => {
          this.expert=response.data.data;
        })
      }
      else {
        await axios.get(window.Ip+'/ExpertById?expertId='+this.to).then(response => {
          this.expert=response.data.data;
        })
      }
      console.log(this.expert)
    },
    initWebSocket() {
      this.peerConnection = new RTCPeerConnection(this.configuration);
      this.socket = new WebSocket('wss://'+process.env.VUE_APP_LOCAL_IP+':8090/ws');

      this.socket.onopen = () => {
        console.log('WebSocket connection established');
        this.sendSignalingMessage({type: 'userId', from: this.userId});
      };

      this.socket.onmessage = (event) => {
        const message = JSON.parse(event.data);
        this.handleSignalingMessage(message);
      };

      this.socket.onclose = () => {
        console.log('WebSocket connection closed');
      };

      this.socket.onerror = (error) => {
        console.error('WebSocket error:', error);
      };
    },

    async startCall() {
      try {
        if(this.to==null)
        {
          alert('请输入通话对象')
          return
        }
        if(!this.video_on) {
          this.localStream = await navigator.mediaDevices.getUserMedia({audio: true, video: true});
          this.$refs.localVideo.srcObject = this.localStream;

          this.peerConnection.addStream(this.localStream);
          this.video_on = true
        }
        const offer = await this.peerConnection.createOffer();
        this.peerConnection.setLocalDescription(offer);
        await this.sendSignalingMessage({ type: 'offer', sdp: offer.sdp,from: this.userId,to :this.to});
      } catch (error) {
        console.error('Error accessing media devices.', error);
      }
    },
    async endCall(){
      alert('通话已结束')
      this.$refs.remoteVideo.srcObject = null
      this.$refs.localVideo.srcObject = null
      this.peerConnection.close()
      this.sendSignalingMessage({ type: 'cancel',from: this.userId,to :this.to});
      await axios.post(window.Ip+'/AppointmentEnd?appointmentId='+this.appointmentId)
      this.goHome()
    },
    async handleSignalingMessage(message) {
      switch (message.type) {
        case 'offer':
          console.log(message.from)
          console.log(this.to)
          if(message.from === this.userId) {
            alert('用户'+message.to+'未上线，请等待');
          }
          else if (message.from.toString() === this.to.toString())
          {
            this.sdp = message.sdp;
            this.be_called = true;
          }
          break;
        case 'answer':
          console.log('get');
          this.initPeerConnection()
          await this.peerConnection.setRemoteDescription(new RTCSessionDescription({type: 'answer', sdp: message.sdp}));
          this.connection_on = true
          break;
        case 'candidate':
          console.log('ice candidate:', message.candidate);
          this.peerConnection.addIceCandidate(new RTCIceCandidate(JSON.parse(  message.candidate)));
          break
        case 'cancel':
          alert('对方已结束通话')
          this.$refs.remoteVideo.srcObject = null
          this.$refs.localVideo.srcObject = null
          this.peerConnection.close()
          this.goHome()
          break;
        default:
          console.warn('Unknown message type:', message.type);
      }
    },
    async goCall(){
      this.localStream = await navigator.mediaDevices.getUserMedia({ audio: true, video: true });
      this.$refs.localVideo.srcObject = this.localStream;
      this.peerConnection.addStream(this.localStream);
      this.initPeerConnection()
      await this.peerConnection.setRemoteDescription(new RTCSessionDescription({type: 'offer', sdp: this.sdp}));
      await this.createAnswer();
      this.be_called=false;
      this.video_on =true
      this.connection_on =true
    },
    async createAnswer() {
      const answer = await this.peerConnection.createAnswer();
      await this.peerConnection.setLocalDescription(answer);
      this.sendSignalingMessage({ type: 'answer', sdp: answer.sdp ,from: this.userId,to :this.to});
    },

    sendSignalingMessage(message) {
      if (this.socket.readyState === WebSocket.OPEN) {
        this.socket.send(JSON.stringify(message));
      } else {
        console.error('WebSocket is not open');
      }
    }
  }
};
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
  padding: 20px;
}

/* 视频区域样式 */
.video-container {
  width: 100%;
  height: 300px;
  background-color: #f0f0f0;
  border-radius: 15px;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.remote-video {
  width: 100%;
  height: 100%;
  object-fit: cover; /* 使视频填充整个容器 */
}

.local-video {
  position: absolute;
  top: 80px;
  right: 20px;
  width: 150px; /* 设置本地视频的小窗口宽度 */
  height: auto; /* 维持宽高比 */
  border: 2px solid white; /* 可选：添加边框以区分 */
  z-index: 10; /* 确保本地视频在顶层 */
  object-fit: cover; /* 使本地视频适应小窗口 */
}
/* 结束面试按钮样式 */
.end-interview-btn {
  width: 100%;
  padding: 15px;
  background-color: #ff4444;
  color: white;
  border: none;
  border-radius: 12px;
  font-size: 16px;
  cursor: pointer;
  margin-bottom: 30px;
  transition: all 0.3s ease;
}

.end-interview-btn:hover {
  background-color: #ff0000;
}

/* 专家信息区域样式 */
.expert-section {
  padding: 0 10px;
}

.expert-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin-bottom: 15px;
}

.expert-card {
  background-color: #f8f8f8;
  border-radius: 12px;
  padding: 15px;
}

.expert-info {
  margin-bottom: 10px;
  color: #666;
  font-size: 14px;
  line-height: 1.6;
}
</style>