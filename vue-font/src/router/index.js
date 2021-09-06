import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/components/index/HelloWorld'
import Main from '@/components/index/Main'
import Zookeeper from '@/components/zookeeper/Zookeeper.vue'

Vue.use(Router)

export default new Router({
  // mode: 'history',
  mode: 'hash', // 打包选项
  // base: '/sakura',
  routes: [
    {
      path: '/hello',
      name: 'HelloWorld',
      component: HelloWorld,
      meta: {
        requireAuth: true
      },
      children: []
    },
    {
      path: '/',
      name: 'Main',
      component: Main,
      meta: {
        requireAuth: true
      },
      children: [{
        path: '/zookeeper',
        name: 'zookeeper',
        component: Zookeeper,
        meta: {
          requireAuth: false
        }
      }]
    }
  ]
})
