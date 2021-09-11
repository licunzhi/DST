<template>
    <div class="main-content">
      <el-container>
        <el-aside width="auto" :class="{'left-is-collapse' : isCollapse}">
          <el-menu class="el-menu-vertical-demo"
                   :default-active="activeIndex"
                   @open="handleOpen" @close="handleClose"
                   :collapse="isCollapse"
                   @select="handleSelect"
                   background-color="#263445"
                   text-color="rgb(191, 203, 217)"
                   active-text-color="rgb(64, 158, 255)">
            <el-menu-item index="DataSourceTools">
              <i class="el-icon-menu"></i>
              <span slot="title" style="font-family:fantasy">DataSourceTools</span>
            </el-menu-item>
            <el-submenu index="Z-PREFIX">
              <template slot="title">
                <i class="el-icon-document-remove"></i>
                <span slot="title">Z-PREFIX</span>
              </template>
              <el-menu-item index="zookeeper">zookeeper</el-menu-item>
            </el-submenu>
          </el-menu>
        </el-aside>

        <el-container>
          <el-header>
            <div class="div-inline-block">
              <!--收缩按钮-->
              <el-button v-if="!isCollapse" class="switch-menu" icon="el-icon-s-fold" @click="isCollapse = true"></el-button>
              <el-button v-if="isCollapse"  class="switch-menu" icon="el-icon-s-unfold" @click="isCollapse = false"></el-button>
            </div>
            <!--面包屑-->
            <div class="div-inline-block">
              <el-breadcrumb separator="/">
                <el-breadcrumb-item :to="{ path: '/' }">DataSourceTools</el-breadcrumb-item>
              </el-breadcrumb>
            </div>
          </el-header>
          <el-main>
            <el-tabs v-model="tabValue" closable @tab-click="handleTabsClick" @edit="handleTabsEdit">
              <el-tab-pane :key="item.path" v-for="(item) in tabsInfoMap" :label="item.path" :name="item.key">
                <RouterTabView v-bind:routerPath="item"></RouterTabView>
              </el-tab-pane>
            </el-tabs>
          </el-main>
        </el-container>
      </el-container>
    </div>
</template>

<script>
import RouterTabView from '@/components/index/RouterTabView'
import { mapState, mapMutations, mapGetters } from 'vuex'
import { StoreNamespace } from '@/store/store-namespace.data'

export default {
  name: 'Main',
  components: {
    RouterTabView
  },
  data () {
    return {
      isCollapse: false,
      pathParent: '',
      pathChild: '',
      locationPath: '',
      activeIndex: 'DataSourceTools',
      tabValue: ''
    }
  },
  computed: {
    ...mapState(StoreNamespace.INDEX_STORE_MODULE, {
      tabsInfo: (state) => state.tabsInfo,
      tabValueStore: (state) => state.tabValue
    }),
    ...mapGetters(StoreNamespace.INDEX_STORE_MODULE, ['tabsInfoMap', 'getTabValue'])
  },
  mounted () {
    let tabsInfoMap = this.tabsInfo
    if (this.tabValue === '') {
      this.tabValue = tabsInfoMap.length > 0 ? tabsInfoMap[0].key : ''
    } else if (this.tabValueStore !== '') {
      this.tabValue = this.tabValueStore
    }
  },
  watch: {
    tabValue (value) {
      this.setTabValue(value)
      this.activeIndex = value
    }
  },
  methods: {
    ...mapMutations(StoreNamespace.INDEX_STORE_MODULE, ['setTabsInfo', 'removeTabsInfo', 'setTabValue']),
    handleSelect (key, keyPath) {
      this.setTabsInfo({key, keyPath})
      this.setTabValue(key)
      this.tabValue = key
    },
    handleOpen (key, keyPath) {
      // console.log(key, keyPath)
    },
    handleClose (key, keyPath) {
      // console.log(key, keyPath)
    },

    handleTabsClick (tab, event) {
      this.activeIndex = tab.name
    },
    handleTabsEdit (targetName, action) {
      if (action === 'add') {
      }
      if (action === 'remove') {
        this.removeTabsInfo(targetName)
        /* if remove check this, need to change location to others */
        if (this.tabsInfo.length > 0) {
          this.activeIndex = this.tabsInfo[0].key
          this.tabValue = this.tabsInfo[0].key
        } else {
          this.activeIndex = ''
          this.tabValue = ''
        }
      }
    }

  }
}
</script>

<style lang="scss" scoped>
.main-content {
  display: flex;
  flex-wrap: wrap;
  height: 100%;
  width: 100%;
  overflow: hidden;
}
.main-left {
  width: 250px;
  height: 100%;
}
.main-logo {
  height: 100px;
  font-size: 50px;
  line-height: 2;
  text-align: center;
  background-color: rgb(84, 92, 100);
  color: #fff;
  border-bottom: 1px solid rgb(238, 238, 238);
}
.el-menu-vertical-demo {
  height: 100%;
  min-height: 100%;
  .el-menu-item {
    border: none;
  }
}
.left-is-collapse {
  transition: all 1s;
  width: auto!important;
}
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 200px;
  height: 100%;
  min-height: 100%;
}
.el-header {
  height: 55px!important;
  z-index: 100;
  background-color: white;
  box-shadow: 0 1px 3px rgba(26,26,26,.1);
  padding: 0;
}
.user-info {
  width: 100%;
  height: 80px;
  background-color: #f6f6f6 ;
}
.main-content-bread {
  top: 80px;
  margin: 15px;
}
.main-content-body {
  position: absolute;
  top: 15px;
  width: calc(100% - 280px);
  margin-top: 80px;
  overflow: auto;
}
.switch-menu {
  height: 100%;
  width: 70px;
  font-size: 30px;
  border: none;
  background-color: transparent;
}
.div-inline-block {
  display: inline-block;
}
</style>
