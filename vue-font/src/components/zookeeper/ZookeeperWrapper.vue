<template>
  <div>
    <div style="margin-bottom: 20px;">
      <el-button icon="el-icon-setting" @click="openConnectDialog">Add New Connection</el-button>
      <el-button :icon="connectClassState ? 'el-icon-refresh' : 'el-icon-loading'" @click="reconnect">Refresh Tree Nodes</el-button>
    </div>

    <div>
      <el-tabs v-model="tabValue" type="card" editable @edit="handleTabsEdit">
        <el-tab-pane
          :key="item.ip + ':' + item.port"
          v-for="(item) in tabsInfo"
          :label="item.ip + ':' +item.port"
          :name="item.ip + ':' +item.port"
        >
          <zookeeper :ref="item.ip + ':' + item.port" v-bind:settingFormData="item"></zookeeper>
        </el-tab-pane>
      </el-tabs>
    </div>

    <!--创建初始化链接弹窗-->
    <el-dialog title="Connection Setting" :visible.sync="dialogConnectSetting" width="30%" :before-close="handleCloseConnectSetting">
      <el-form ref="form" :model="settingForm" label-width="100px">
        <el-form-item label="IP">
          <el-input v-model="settingForm.ip"></el-input>
        </el-form-item>
        <el-form-item label="PORT">
          <el-input v-model="settingForm.port"></el-input>
        </el-form-item>
        <el-form-item label="Sleep time">
          <el-input v-model="settingForm.timeout"></el-input>
        </el-form-item>
        <el-form-item label="Max Retries">
          <el-input v-model="settingForm.retriesTimes"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="onSubmit">connect</el-button>
          <el-button @click="dialogConnectSetting= false">cancel</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import Zookeeper from './Zookeeper'
import ZookeeperApi from './ZookeeperApi'
import { mapState, mapMutations, mapGetters } from 'vuex'
import { StoreNamespace } from '@/store/store-namespace.data'
import _ from 'lodash'

export default {
  name: 'ZookeeperWrapper',
  components: {
    Zookeeper
  },
  data () {
    return {
      connectClassState: true,
      refreshNodeClassState: false,
      dialogConnectSetting: false,
      settingForm: {
        ip: '127.0.0.1',
        port: '2181',
        timeout: 5000,
        retriesTimes: 1
      },
      tabValue: ''
    }
  },
  computed: {
    ...mapState(StoreNamespace.ZOOKEEPER_STORE_MODULE, {
      tabsInfo: (state) => state.tabsInfo,
      tabValueStore: (state) => state.tabValue
    })
  },
  mounted () {
    let tabsInfoMap = this.tabsInfo
    this.tabValue = tabsInfoMap.length > 0 ? `${tabsInfoMap[0].ip}:${tabsInfoMap[0].port}` : ''
  },
  watch: {
    tabValue (value) {
      this.setTabValue(value)
    }
  },
  methods: {
    ...mapMutations(StoreNamespace.ZOOKEEPER_STORE_MODULE, ['setTabsInfo', 'removeTabsInfo', 'setTabValue']),
    ...mapGetters(StoreNamespace.ZOOKEEPER_STORE_MODULE, ['tabsInfoMap', 'getTabValue']),
    openConnectDialog () {
      this.dialogConnectSetting = true
    },
    handleCloseConnectSetting () {},
    async onSubmit () {
      const params = this.settingForm
      const resultData = await this.$http.post(ZookeeperApi.ZK_CONNECT, params)
      if (resultData.data && resultData.data.code === 1) {
        this.$message.success(resultData.data.messageList[0])
        this.dialogConnectSetting = false

        /* fill tabs info to local */
        this.setTabsInfo(JSON.parse(JSON.stringify(this.settingForm)))
        /* locate active tabs in newer */
        this.setTabValue(`${this.settingForm.ip}:${this.settingForm.port}`)
        this.tabValue = `${this.settingForm.ip}:${this.settingForm.port}`
      } else {
        this.$message.error(resultData.data.messageList[0])
      }
    },
    handleTabsEdit (targetName, action) {
      if (action === 'add') {
        this.openConnectDialog()
      }
      let splits = targetName.split(':')

      if (action === 'remove') {
        this.removeTabsInfo({
          ip: splits[0],
          port: splits[1]
        })
        if (this.tabsInfo.length > 0) {
          this.tabValue = `${this.tabsInfo[0].ip}:${this.tabsInfo[0].port}`
        } else {
          this.activeIndex = ''
          this.tabValue = ''
        }
      }
    },
    /* reconnect  */
    reconnect: _.debounce(function () {
      this.connectClassState = false
      try {
        this.$refs[this.tabValue][0].refreshNodeTree()
      } catch (e) {
        console.error(e)
      }
      const _this = this
      setTimeout(function () {
        _this.connectClassState = true
      }, 2000)
    }, 1000, {
      leading: true,
      trailing: false
    })
  }
}
</script>

<style lang="scss" scoped>
</style>
