export default {
  namespaced: true,

  state: {
    /* tabs页存储的信息 */
    tabsInfo: [],
    /* 当前激活页面位置 */
    tabValue: ''
  },

  getters: {
    tabsInfoMap (state) {
      const { tabsInfo } = state
      state.tabsInfo = tabsInfo.filter(function (value) {
        return (value.ip && value.port)
      })
      return state.tabsInfo
    },
    getTabValue (state) {
      return state.tabValue
    }
  },

  mutations: {
    setTabsInfo (state, payload) {
      const {ip, port} = payload
      const { tabsInfo } = state
      let resultTabs = tabsInfo.filter(function (value) {
        return (value.ip === ip && value.port === port)
      })
      if (resultTabs.length <= 0) {
        state.tabsInfo.push(payload)
      }
    },
    removeTabsInfo (state, payload) {
      const {ip, port} = payload
      const { tabsInfo } = state
      state.tabsInfo = tabsInfo.filter(function (value) {
        return (value.ip !== ip || value.port !== port)
      })
    },
    setTabValue (state, payload) {
      state.tabValue = payload
    }
  }
}
