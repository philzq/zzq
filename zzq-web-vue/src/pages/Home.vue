<template>
  <div id="home">
    <!--父子组件通信&ndash;&gt;props，$emit-->
    <!--向子组件传递username属性，以及监听子组件传递的childNotify事件由父组件childNotify执行-->
    <home-header :username="username" @childNotify="childNotify" >
    </home-header>
    <div>99999999</div>
    <home-footer>
    </home-footer>
  </div>
</template>

<script>
import header from "../components/header"
import footer from "../components/footer"

export default {
  components:{
    "home-header" : header,
    "home-footer" : footer
  },
  data (){
    return {
      username : "zzx"
    }
  },
  methods:{
    childNotify(params){
      this.$message(params);
    }
  }
}
</script>

