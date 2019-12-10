<template>
  <div>
    <parent-child :childName="childName" @childNotify="childReceive"></parent-child>
    <el-input
      placeholder="请输入childName"
      v-model="childName"
      :disabled="false">
    </el-input>
    <div style="width: 100%;height: 100px;background-color: gainsboro"></div>
    <parent-child2 :child2Name="child2Name" @child2Notify="child2Receive"></parent-child2>
    <el-input
      placeholder="请输入child2Name"
      v-model="child2Name"
      :disabled="false">
    </el-input>
  </div>
</template>

<script>
  import child from "./child"
  import child2 from "./child2"
  export default {
    components: {
      "parent-child" : child,
      "parent-child2" : child2
    },data(){
      return {
        childName : "我是child哦",
        child2Name : "我是child2哦"
      }
    },methods:{
      childReceive(params){
        this.$message(params)
      },
      child2Receive(params){
        this.$message(params)
      }
    }
  }
</script>

<style scoped>
  .el-input{
    width: 200px;
    margin: 0px auto;
  }
</style>
