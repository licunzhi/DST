// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'

// 引入UI组件
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import './common/css.scss'

// highlight.js代码高亮指令
import VueHighlightJS from './hightlight/hightlight'

import VueCodeMirror from 'vue-codemirror'
import 'codemirror/lib/codemirror.css'

import store from './store'
// Vuex
import Vuex from 'vuex'

// 封装axios
import ajax from './http/Ajax'

import echarts from 'echarts'

import qs from 'qs'

import lodash from 'lodash'

require('./mock.js')

// 全局echarts

// 产生生产提示
Vue.config.productionTip = false

// 配置全局调用属性
Vue.prototype.$http = ajax
Vue.prototype.ajax = ajax
Vue.prototype.echarts = echarts
Vue.prototype.qs = qs
Vue.prototype.lodash = lodash

// 使用element-ui 组件
Vue.use(ElementUI)

Vue.use(Vuex)

Vue.use(VueHighlightJS)

Vue.use(VueCodeMirror)

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>'
})
