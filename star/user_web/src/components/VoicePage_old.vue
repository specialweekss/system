<template>
  <div>
    <button @click="startRecording">开始录音</button>
    <button @click="stopRecording" :disabled="!isRecording">停止录音</button>
    <button v-if="audioFile" @click="uploadFile">上传</button>
    <a v-if="audioUrl" :href="audioUrl" :download=this.filename>下载录音</a>
    <audio controls v-if="audioUrl">
      <source :src="audioUrl" type="audio/webm">
    </audio>

  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      mediaRecorder: null,
      audioChunks: [],
      isRecording: false,
      audioUrl: null,
      audioFile :null,
      filename:null
    };
  },
  methods: {
    async startRecording() {
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
          let now;
          now = new Date();
          this.filename = now.toString().split(' ').join('').split('(')[0].split(':').join('')+"recording.webm";
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
    async uploadFile() {
      const formData = new FormData();
      formData.append('file', this.audioFile, this.filename);
      const  response = await axios.post(window.Ip+'/upload',formData)
      if (response.data.code === 200) {
        alert('上传成功！')
      }
    }
  }
};
</script>