<template>
  <div>
    <el-input
      v-model="count"
      :disabled="true">
    </el-input>
    <el-input
      v-model="doubleCount"
      :disabled="true">
    </el-input>
    <el-button type="primary" @click="increment">增加count</el-button>

    <el-input
      v-model="name"
      :disabled="true">
    </el-input>
    <el-input
      v-model="addNameA"
      :disabled="true">
    </el-input>
    <el-button type="primary" @click="setName(123)">增加count</el-button>
  </div>
</template>
<script>
  import {mapActions,mapState,mapGetters,mapMutations} from 'vuex'
  export default {
    computed: {
      ...mapState({
        count: state => state.moudleA.count,
        name: state => state.moudleB.name
      }),
      ...mapGetters({
        doubleCount: "doubleCount",
        addNameA: "addNameA"
      })
    },
    methods: {
      ...mapActions([
        "increment",
        "setName"
      ])
    }
  }
</script>
