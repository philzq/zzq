import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)
const moudleA = {
  state: {
    count: 0
  },
  mutations: {
    increment (state) {
      // 这里的 `state` 对象是模块的局部状态
      state.count++
    }
  },
  actions: {
    increment({commit}){
      commit("increment")
    }
  },
  getters:{
    doubleCount(state){
      return state.count *2;
    }
  }
};

const moudleB = {
  state: {
    name: 'zzq'
  },
  mutations: {
    setName(state,name){
      state.name = state.name + name
    }
  },
  actions: {
    setName({commit,state},name){
      commit("setName",name);
    }
  },
  getters:{
    addNameA(state){
      return state.name + "A";
    }
  }
};

export default new Vuex.Store({
  modules: {
    moudleA,
    moudleB,
  },
  strict: false
})
