import Vue from 'vue'
import Router from 'vue-router'
import Main from '@/page/main/Main'
import Login from '@/page/login/Login'
import Module from '@/page/module/Module'
import Role from '@/page/role/Role'
import RoleAssign from '@/page/roleAssign/RoleAssign'
import User from '@/page/user/User'
import Area from '@/page/area/Area'
import Monitor from '@/page/monitor/Monitor'
import IllegalBaseInfo from '@/page/illegalBaseInfo/IllegalBaseInfo'
import Record from '@/page/record/Record'
import BaseAnaly from '@/page/baseAnaly/BaseAnaly'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      component: Main,
      children: [
        {
          path: '/module',
          component: Module
        }, {
          path: '/role',
          component: Role
        }, {
          path: '/roleAssign',
          component: RoleAssign
        }, {
          path: '/user',
          component: User
        }, {
          path: '/area',
          component: Area
        }, {
          path: '/monitor',
          component: Monitor
        }, {
          path: '/illegalBaseInfo',
          component: IllegalBaseInfo
        }, {
          path: '/record',
          component: Record
        }, {
          path: '/baseAnaly',
          component: BaseAnaly
        }
      ]
    },
    {
      path: '/login',
      component: Login
    }
  ]
})
