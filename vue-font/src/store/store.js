import Vue from 'vue'
import Vuex from 'vuex'
import createLogger from 'vuex/dist/logger'
import createPersistedState from 'vuex-persistedstate'
/*
* 组件中散开的模块在此处进行整合
* */

import { ZookeeperStoreModule } from '@/components/zookeeper'
import { IndexStoreModule } from '@/components/index'

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    ZookeeperStoreModule,
    IndexStoreModule
  },
  plugins: [
    createLogger(),
    createPersistedState({
      storage: window.sessionStorage,
      reducer (state) {
        return state
      }
    })
  ]
})

export default store
