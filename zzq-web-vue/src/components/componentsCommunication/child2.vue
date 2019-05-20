<template>
  <div id="child2">
    child2的名字叫:{{child2Name}}
  </div>
</template>

<script>
  export default {
    props:{
      child2Name :{
        type:String,
        default: ""
      },methods:{
        child2Notify(params){
          this.$emit("child2Notify","child2我的名字叫"+this.child2Name);
        }
      }
    },
    created(){
      this.$bus.$on("child2",function(params){
        this.$message(params)
      })
    },
    beforeDestroy() {
      this.$bus.$off("child2",function(){
        this.$message("child2-bus销毁")
      })
    }
  }
</script>

<style scoped>
  #child2{
    width: 100%;
    height: 100%;
    margin: 0px auto;
    font-size: 2rem;
    color: red;
  }
</style>
