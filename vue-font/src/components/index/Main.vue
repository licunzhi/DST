<template>
    <div class="main-content">
        <div class="main-left">
          <div class="main-logo">
            D_S_T
          </div>
          <div class="main-menu">
            <el-menu class="el-menu-vertical-demo"
                     @open="handleOpen" @close="handleClose"
                     @select="handleSelect"
                     :router='true'>

              <el-submenu index="1">
                <template slot="title">
                  <i class="el-icon-document-remove"></i>
                  <span slot="title">Z-PREFIX</span>
                </template>
                <el-menu-item index="zookeeper">Zookeeper</el-menu-item>
              </el-submenu>
              </el-menu>
          </div>
        </div>
        <div class="main-right">
          <div class="content-header">
          </div>
          <div class="main-content-body">
            <router-view></router-view>
          </div>
        </div>
    </div>
</template>

<script>
export default {
  name: 'Main',
  data () {
    return {
      isCollapse: true
    }
  },
  methods: {
    handleSelect (key, keyPath) {
      console.log(key, keyPath)
    },
    handleOpen (key, keyPath) {
      console.log(key, keyPath)
    },
    handleClose (key, keyPath) {
      console.log(key, keyPath)
    }
  }
}
</script>

<style scoped>
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
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 100%;
  height: calc(100% - 100px);
}
.main-menu {
  height: calc(100% - 100px);
  overflow-y: auto;
  overflow-x: hidden;
  border-right: 1px solid #e6e6e6;
}
.main-right {
  width: calc(100% - 360px);
  overflow: hidden;
}
.content-header {
  position: fixed;
  width: calc(100% - 360px);
  z-index: 100;
  background-color: white;
  box-shadow: 0 1px 3px rgba(26,26,26,.1);
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
  width: calc(100% - 360px);
  overflow: auto;
}
.setting-location {
  font-size: 30px;
  margin-top: 15px;
  margin-right: 25px;
}
.el-divider {
  margin: 0px;
}
</style>
