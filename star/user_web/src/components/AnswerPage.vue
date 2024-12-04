
<template>
question:<input v-model="this.question">
back_ground<input v-model="this.back_ground" type="text"/>
  <button @click =this.getAnswer > 分析 </button>
answer:{{this.answer}}
</template>

<script>
import axios from "axios";
export default {
  data(){
    return{
      question:null,
      back_ground:null,
      answer:'请开始'
    }
  },
  methods:{
    async getAnswer()
    {
      if(this.question==null||this.back_ground==null){
        alert('请给出问题和资料');
        this.answer = '请开始'
      }
      else{
        this.answer = '正在进行分析...'
        const response = await axios.get(window.Ip+'/getAnswer?question=' + this.question+'&back_ground=' + this.back_ground)
        this.answer=response.data.data;
     }
    }
  }
}
</script>

<style scoped>

</style>