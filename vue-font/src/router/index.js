import Vue from 'vue'
import Router from 'vue-router'
import Main from '@/components/index/Main'
import ZookeeperWrapper from '@/components/zookeeper/ZookeeperWrapper'
import dst from '@/components/dst/dst'

Vue.use(Router)

export default new Router({
  // mode: 'history',
  mode: 'hash', // 打包选项
  // base: '/sakura',
  routes: [
    {
      path: '/',
      name: 'Main',
      component: Main,
      children: [{
        path: '/zookeeper',
        name: 'ZookeeperWrapper',
        component: ZookeeperWrapper
      }, {
        path: '/dst',
        name: 'dst',
        component: dst
      }]
    }
  ]
})
