import Vue from 'vue'
import Router from 'vue-router'
import Home from '@/pages/Home'
import parent from '@/components/componentsCommunication/parent'
import parentSlot from "@/components/componentsSlot/parentSlot"

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
    }
  ]
})
