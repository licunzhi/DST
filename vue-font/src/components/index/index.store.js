export default {
  namespaced: true,

  state: {
    tabsInfo: [],
    tabValue: ''
  },

  getters: {
    tabsInfoMap (state) {
      const { tabsInfo } = state
      state.tabsInfo = tabsInfo.filter(function (value) {
        return (value.key && value.path)
      })
      return state.tabsInfo
    },
    getTabValue (state) {
      return state.tabValue
    }
  },

  mutations: {
    setTabsInfo (state, payload) {
      const { tabsInfo } = state
      const { key, keyPath } = payload
      let resultTabs = tabsInfo.filter(function (value) {
        return value.path === keyPath.join('/')
      })
      if (resultTabs.length <= 0) {
        state.tabsInfo.push({
          key: key,
          path: keyPath.join('/')
        })
      } else {
        return false
      }
    },
    removeTabsInfo (state, payload) {
      const { tabsInfo } = state
      state.tabsInfo = tabsInfo.filter(function (value) {
        return value.key !== payload
      })
    },
    setTabValue (state, payload) {
      state.tabValue = payload
    }
  }
}
