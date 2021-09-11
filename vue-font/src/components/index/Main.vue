<template>
    <div class="main-content">
      <el-container>
        <el-aside width="auto" :class="{'left-is-collapse' : isCollapse}">
          <el-menu class="el-menu-vertical-demo"
                   @open="handleOpen" @close="handleClose"
                   :collapse="isCollapse"
                   @select="handleSelect"
                   background-color="#263445"
                   text-color="rgb(191, 203, 217)"
                   active-text-color="rgb(64, 158, 255)"
                   :router='true'>
            <el-menu-item>
              <i class="el-icon-menu"></i>
              <span slot="title" style="font-family:fantasy">DataSourceTools</span>
            </el-menu-item>
            <el-submenu index="Z-PREFIX">
              <template slot="title">
                <i class="el-icon-document-remove"></i>
                <span slot="title">Z-PREFIX</span>
              </template>
              <el-menu-item index="zookeeper">Zookeeper</el-menu-item>
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
                <el-breadcrumb-item v-if="pathParent !== '' " :to="{ path: locationPath }">{{ pathParent }}</el-breadcrumb-item>
                <el-breadcrumb-item  v-if="pathChild !== '' "><a :to="{ path: '/zookeeper' }">{{ pathChild }}</a></el-breadcrumb-item>
              </el-breadcrumb>
            </div>
          </el-header>
          <el-main>
            <el-tabs v-model="editableTabsValue" closable>
              <el-tab-pane :key="item.name" v-for="(item) in editableTabs" :label="item.title" :name="item.name">
                <router-view></router-view>
              </el-tab-pane>
            </el-tabs>
          </el-main>
        </el-container>
      </el-container>
    </div>
</template>

<script>
export default {
  name: 'Main',
  data () {
    return {
      isCollapse: false,
      pathParent: '',
      pathChild: '',
      locationPath: '',

      editableTabsValue: '2',
      editableTabs: [{
        title: 'DataSourceTools',
        name: '1',
        content: 'Tab 1 content'
      }, {
        title: 'Z-PREFIX/Zookeeper',
        name: '2',
        content: 'Tab 2 content'
      }],
      tabIndex: 2
    }
  },
  methods: {
    handleSelect (key, keyPath) {
      console.log(keyPath)
      this.pathParent = keyPath[0] || ''
      this.pathChild = key || ''
      this.locationPath = key || ''
    },
    handleOpen (key, keyPath) {
      // console.log(key, keyPath)
    },
    handleClose (key, keyPath) {
      // console.log(key, keyPath)
    },

    handleTabsEdit (targetName, action) {
      if (action === 'add') {
        let newTabName = ++this.tabIndex + ''
        this.editableTabs.push({
          title: 'New Tab',
          name: newTabName,
          content: 'New Tab content'
        })
        this.editableTabsValue = newTabName
      }
      if (action === 'remove') {
        let tabs = this.editableTabs
        let activeName = this.editableTabsValue
        if (activeName === targetName) {
          tabs.forEach((tab, index) => {
            if (tab.name === targetName) {
              let nextTab = tabs[index + 1] || tabs[index - 1]
              if (nextTab) {
                activeName = nextTab.name
              }
            }
          })
        }

        this.editableTabsValue = activeName
        this.editableTabs = tabs.filter(tab => tab.name !== targetName)
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
