<template>
  <div id="home">
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

