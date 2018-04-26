import Vue from 'vue'
import Router from 'vue-router'
import Main from '@/page/main/Main'
import Module from '@/page/module/Module'
import Login from '@/page/login/Login'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      component: Main,
      children: [{
        path: '/module',
        component: Module
      }]
    }, {
      path: '/login',
      component: Login
    }
  ]
})
