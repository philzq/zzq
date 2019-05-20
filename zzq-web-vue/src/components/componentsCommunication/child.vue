<template>
  <div id="child">
    child的名字叫:{{childName}}<br>
    <el-button type="primary" @click="brotherNotity">向child2打招呼</el-button>
  </div>
</template>

<script>
  export default {
    props:{
      childName :{
        type:String,
        default: ""
      }
    },methods:{
      childNotify(params){
        this.$emit("childNotify","child的名字叫"+this.childName);
      },
      brotherNotity(){
        this.$bus.$emit("child2","child2你好呀");
      }

    }
  }
</script>

<style scoped>
  #child{
    width: 100%;
    height: 100%;
    margin: 0px auto;
    font-size: 2rem;
    color: red;
  }
</style>
