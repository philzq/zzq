import Vue from 'vue'
import Router from 'vue-router'
import Home from '@/pages/Home'
import parent from '@/components/componentsCommunication/parent'
import parentSlot from "@/components/componentsSlot/parentSlot"
import vuexText from "@/components/vuex/vuexTest"

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Home',
      component: Home
    },{
      path: '/parent',
      name: 'parent',
      component: parent
    },{
      path: '/parentSlot',
      name: 'parentSlot',
      component: parentSlot
    },{
      path: '/vuexText',
      name: 'vuexText',
      component: vuexText
    }
  ]
})
