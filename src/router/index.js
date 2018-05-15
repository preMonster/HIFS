import Vue from 'vue'
import Router from 'vue-router'
import Main from '@/page/main/Main'
import Login from '@/page/login/Login'
import Module from '@/page/module/Module'
import Role from '@/page/role/Role'
import RoleAssign from '@/page/roleAssign/RoleAssign'
import User from '@/page/user/User'

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
        }
      ]
    },
    {
      path: '/login',
      component: Login
    }
  ]
})
