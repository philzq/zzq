<template>
    <div id="header">
      我是header
      <span>姓名:</span>
      <span>{{username}}</span>
      <el-button type="primary" @click="notifyHome">通知home收到姓名</el-button>
    </div>
</template>

<script>
export default {
  data(){
    return {
    }
  },
  props:{
    username:{
      type: String,
      default: ""
    }
  },
  methods:{
    notifyHome (){
      // 子组件以事件的形式通知父组件(需要使用$emit方法，第一个参数，事件名称；第二个事件附带的参数)
      this.$emit('childNotify', "感谢你给我的名字")
    }
  }
}
</script>

<style scoped>
  #header{
    width: 500px;
    height: 300px;
  }
</style>
