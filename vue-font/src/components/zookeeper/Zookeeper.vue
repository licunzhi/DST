<template>
  <div>
    <!--顶部面包屑-->
    <el-row>
      <el-col :span="23" :offset="1">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item>Z-PREFIX</el-breadcrumb-item>
          <el-breadcrumb-item>ZooKeeper</el-breadcrumb-item>
        </el-breadcrumb>
      </el-col>
    </el-row>

    <el-row>
      <el-col :span="22" :offset="1">
        <el-button :type="zkConnectType" icon="el-icon-caret-right" circle  @click="openConnectDialog"></el-button>
        <!--<el-button :type="zkStopType" icon="el-icon-error" circle @click="removeZkClient"></el-button>-->
        <el-button type="primary" icon="el-icon-refresh" circle @click="refreshNodeTree"></el-button>
      </el-col>
    </el-row>
    <!--支撑部数据展示-->
    <el-row>
      <el-col :span="4" :offset="1">
        <!--show-checkbox-->
        <!--default-expand-all-->
        <div class="tree">
          <el-tree
            ref="dataListRef"
            :data="treeNodes"
            node-key="path"
            :default-expanded-keys="idArr"
            :expand-on-click-node="false"
            :highlight-current = "true"
            @node-click="nodeClickEvent"
            @node-expand="nodeExpand"
            @node-collapse="nodeCollapse">
              <div class="custom-tree-node" slot-scope="{ node, data }">
                 <el-tooltip class="item" effect="dark" :content=data.nodeName placement="right-end">
                    <span class="custom-tree-node-label">
                      <el-tag size="mini" type="warning">
                        <i :class="{'el-icon-folder-opened' : data.fileType === 'folder', 'el-icon-document-remove' : data.fileType === 'file'}"></i>
                      </el-tag>
                      {{ data.nodeName }}
                    </span>
                </el-tooltip>
                <span>
                  <el-button
                    type="text"
                    size="mini"
                    icon="el-icon-plus"
                    @click="() => createNewNode(data, node)">
                  </el-button>
                  <el-button
                    v-if="data.path !== '/'"
                    type="text"
                    size="mini"
                    icon="el-icon-delete"
                    @click="() => remove(node, data)">
                  </el-button>
                </span>
              </div>
          </el-tree>
        </div>
      </el-col>
      <el-col :span="18" :offset="1">
        <el-tabs v-model="activeName" @tab-click="handleClickTab">
          <el-tab-pane label="Node Data" name="first" class="codeContent">
              <codemirror
              ref="nodeDataContent"
              :code.sync="nodeDataContent"
              :options="cmOptions"
              @change="changeCodeContent"
              class="code">
            </codemirror>
            <el-button-group style="margin-top: 10px">
              <el-button size="medium" type="primary" icon="el-icon-document-checked" @click="updateNodeData"></el-button>
            </el-button-group>
          </el-tab-pane>

          <el-tab-pane label="Node Metadata" name="second">
            <el-table :data="metadata" style="width: 100%">
              <el-table-column prop="k" label="key" width="180"></el-table-column>
              <el-table-column prop="v" label="value"></el-table-column>
            </el-table>
          </el-tab-pane>

          <el-tab-pane label="Node ACLs" name="third">
            <el-table :data="acls" style="width: 100%">
              <el-table-column prop="k" label="key" width="180"></el-table-column>
              <el-table-column prop="v" label="value"></el-table-column>
            </el-table>
          </el-tab-pane>
        </el-tabs>
      </el-col>
    </el-row>

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
          <el-input v-model="settingForm.baseSleepTimeMs"></el-input>
        </el-form-item>
        <el-form-item label="Max Retries">
          <el-input v-model="settingForm.maxRetries"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="onSubmit">connect</el-button>
          <el-button @click="dialogConnectSetting= false">cancel</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

    <!--添加节点操作-->
    <el-dialog title="Create New Node" :visible.sync="dialogCreateNewNode" width="30%" :before-close="handleCloseCreateNewNode">
      <el-form ref="createNodeForm" :model="createNodeForm" label-width="100px">
        <el-form-item label="Node Name" prop="name">
          <el-input v-model="createNodeForm.name"></el-input>
        </el-form-item>

        <el-form-item label="Node Data" prop="data">
          <el-input type="textarea" :rows="10" v-model="createNodeForm.data"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="createNewNodeSubmit">create</el-button>
          <el-button @click="handleCloseCreateNewNode">cancel</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import ZookeeperApi from './ZookeeperApi'
import modeInfo from './Modes'
import { codemirror } from 'vue-codemirror'
import 'codemirror/theme/ambiance.css'
require('codemirror/mode/javascript/javascript')

const defaultTreeNode = [{
  id: 'ZK-TREE-ROOT',
  label: 'No Data',
  fileType: 'folder',
  children: []
}]

export default {
  name: 'Zookeeper',
  components: {
    codemirror
  },
  data () {
    return {
      cmOptions: {
        value: '',
        mode: 'text/javascript',
        theme: 'ambiance',
        readOnly: false,
        tabSize: 4,
        lineNumbers: true
      },
      modes: modeInfo,
      treeNodes: JSON.parse(JSON.stringify(defaultTreeNode)),
      idArr: ['/'],
      activeName: 'first',
      innerHtml: '',
      metadata: [],
      acls: [],
      nodeDataContent: '',
      nodePath: '',
      dialogConnectSetting: false,
      settingForm: {
        ip: '127.0.0.1',
        port: '2181',
        baseSleepTimeMs: 5000,
        maxRetries: 1
      },
      zkConnectType: 'success',
      zkStopType: 'info',

      dialogCreateNewNode: false,
      createNodeForm: {
        name: '',
        data: '',
        nodeData: '',
        node: ''
      }
    }
  },

  methods: {
    remove (node, data) {
      this.$confirm('确认删除该节点或递归子节点数据？', 'Tips', {
        confirmButtonText: 'ok',
        cancelButtonText: 'cancel',
        type: 'warn',
        callback: async action => {
          if (action === 'confirm') {
            const treeResponseData = await this.$http.post(ZookeeperApi.ZK_DElETE, {
              path: data.path
            })
            if (treeResponseData.data && treeResponseData.data.code === 1) {
              const parent = node.parent
              const children = parent.data.children || parent.data
              const index = children.findIndex(d => d.id === data.id)
              children.splice(index, 1)
            }
          }
        }
      })
    },

    openConnectDialog () {
      this.dialogConnectSetting = true
    },
    async refreshNodeTree () {
      const treeResponseData = await this.$http.post(ZookeeperApi.ZK_RETRIEVE_All, {})
      if (treeResponseData.data && treeResponseData.data.code === 1) {
        this.treeNodes = []
        this.treeNodes.push(treeResponseData.data.data)
      } else {
        this.treeNodes = defaultTreeNode
      }
    },
    // 提交连接事件
    /*
    * init connect
    *       ok -> show information
    *       error -> init action
    * */
    async onSubmit () {
      const params = this.settingForm
      const resultData = await this.$http.post(ZookeeperApi.ZK_CONNECT, params)
      if (resultData.data /* && resultData.data.code === 1 */) {
        this.$message.success(resultData.data.messageList[0])
        this.dialogConnectSetting = false

        const treeResponseData = await this.$http.post(ZookeeperApi.ZK_RETRIEVE_All, {})
        if (treeResponseData.data && treeResponseData.data.code === 1) {
          this.treeNodes = []
          this.treeNodes.push(treeResponseData.data.data)
        } else {
          this.treeNodes = defaultTreeNode
        }
      } else {
        this.$message.error(resultData.data.messageList[0])
      }
    },
    removeZkClient () {
      this.zkConnectType = 'success'
      this.zkStopType = 'info'
    },
    handleCloseConnectSetting () {},
    handleClickTab () {},
    createNewNode (data, node) {
      this.createNodeForm.nodeData = data
      this.createNodeForm.node = node
      this.dialogCreateNewNode = true
    },
    handleCloseCreateNewNode () {
      // reset form data
      this.$refs['createNodeForm'].resetFields()
      this.createNodeForm.nodeData = ''
      this.createNodeForm.node = ''
      this.dialogCreateNewNode = false
    },
    changeCodeContent (content) {
      console.log(content)
    },
    async updateNodeData () {
      const response = await this.$http.post(ZookeeperApi.ZK_UPDATE_DATA, {
        path: this.nodePath,
        data: this.$refs.nodeDataContent.content
      })
      if (response.data && response.data.code === 1) {
        await this.refreshNodeTree()
        this.$message.success(response.data.messageList[0])
      } else {
        this.$message.success(response.data.messageList[0])
      }
    },
    nodeExpand (data) {
      this.idArr.push(data.path)
    },
    nodeCollapse (data) {
      let index = -1
      for (var i = 0; i < this.idArr.length; i++) {
        if (this.idArr[i] === data.path) {
          index = i
          break
        }
      }
      this.idArr.splice(index, 1)
    },
    nodeClickEvent (data) {
      let keys = Object.keys(data.nodeMetadata)
      let values = Object.values(data.nodeMetadata)
      let dataArr = []
      keys.forEach((k, index) => {
        dataArr.push({
          k: k,
          v: values[index]
        })
      })
      this.metadata = dataArr

      let dataArrAcl = []
      data.nodeAclsList.forEach((value, location) => {
        dataArrAcl.push({
          k: 'NO.' + (location + 1),
          v: ''
        })

        let keysAcl = Object.keys(value)
        let valuesAcl = Object.values(value)
        keysAcl.forEach((k, index) => {
          dataArrAcl.push({
            k: k,
            v: valuesAcl[index]
          })
        })
      })
      this.acls = dataArrAcl

      this.nodeDataContent = data.data
      /* fix data no change action */
      this.$refs.nodeDataContent.content = data.data
      this.nodePath = data.path
    },
    async createNewNodeSubmit () {
      let split = this.createNodeForm.nodeData.path === '/' ? '' : '/'
      let path = this.createNodeForm.nodeData.path + split + this.createNodeForm.name
      const resultData = await this.$http.post(ZookeeperApi.ZK_CREATE, {
        path: path,
        data: this.createNodeForm.data,
        nodeModel: 'PERSISTENT'
      })
      if (resultData.data && resultData.data.code === 1) {
        await this.refreshNodeTree()
        this.$message.success(resultData.data.messageList[0])
        this.dialogCreateNewNode = false
      } else {
        this.$message.error(resultData.data.messageList[0])
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.el-row {
  margin-bottom: 20px
}
.tree{
  overflow-y: auto;
  overflow-x: auto;
  max-height: 800px;
  min-height: 600px;
  border-right: 1px solid #e6e6e6;
}
.codeContent {
  & /deep/ .CodeMirror {
    height: 600px!important;
    max-height: 600px!important;
    min-height: 600px!important;
  }
}

.tree /deep/ .el-tree-node.is-expanded > .el-tree-node__children {
  display: inline;
}

.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}
.custom-tree-node-label {
  width: 70%;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
