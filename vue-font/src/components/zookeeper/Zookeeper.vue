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
        <el-button type="primary" icon="el-icon-refresh" circle></el-button>
      </el-col>
    </el-row>
    <!--支撑部数据展示-->
    <el-row>
      <el-col :span="4" :offset="1">
        <!--show-checkbox-->
        <!--default-expand-all-->
        <div class="tree">
          <el-tree
            :data="treeNodes"
            node-key="id"
            :expand-on-click-node="false"
            @node-click="nodeClickEvent"
            @node-expand="nodeExpand">
              <div class="custom-tree-node" slot-scope="{ node, data }">
                 <el-tooltip class="item" effect="dark" :content=data.nodeName placement="bottom">
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
                    v-if="data.id !== 'ZK-TREE-ROOT'"
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
          <el-tab-pane label="Node Data" name="first">
              <codemirror
              ref="mycode"
              :value="nodeDataContent"
              :options="cmOptions"
              class="code">
            </codemirror>
            <el-button-group style="margin-top: 10px">
              <el-button size="medium" type="primary" icon="el-icon-document-checked"></el-button>
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

let id = 1000
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
        readOnly: false
      },
      modes: modeInfo,
      treeNodes: JSON.parse(JSON.stringify(defaultTreeNode)),
      activeName: 'first',
      innerHtml: '@font-face {\n' +
        "  font-family: Chunkfive; src: url('Chunkfive.otf');\n" +
        '}\n' +
        '\n' +
        'body, .usertext {\n' +
        '  color: #F0F0F0; background: #600;\n' +
        '  font-family: Chunkfive, sans;\n' +
        '  --heading-1: 30px/32px Helvetica, sans-serif;\n' +
        '}\n' +
        '\n' +
        '@import url(print.css);\n' +
        '@media print {\n' +
        '  a[href^=http]::after {\n' +
        '    content: attr(href)\n' +
        '  }\n' +
        '}',
      tableData: [{
        k: '2016-05-02',
        v: '王小虎',
        alias: '上海市普陀区金沙江路 1518 弄'
      }, {
        k: '2016-05-04',
        v: '王小虎',
        alias: '上海市普陀区金沙江路 1517 弄'
      }, {
        k: '2016-05-01',
        v: '王小虎',
        alias: '上海市普陀区金沙江路 1519 弄'
      }, {
        k: '2016-05-03',
        v: '王小虎',
        alias: '上海市普陀区金沙江路 1516 弄'
      }],
      metadata: [],
      acls: [],
      nodeDataContent: '',
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

    append (data, nodeId) {
      // 代码流程， 需要先判断是不是root节点，如果要是root节点的话直接进行 路径创建就行了，如果不是的话需要进行路径拼接
      if (nodeId === 'ZK-TREE-ROOT') {
        console.log(data)
      }

      alert('弹出添加操作')
      // eslint-disable-next-line no-undef
      const newChild = {id: id++, label: 'testtest', children: []}
      if (!data.children) {
        this.$set(data, 'children', [])
      }
      data.children.push(newChild)
    },

    remove (node, data) {
      alert('删除操作')
      const parent = node.parent
      const children = parent.data.children || parent.data
      const index = children.findIndex(d => d.id === data.id)
      children.splice(index, 1)
    },

    openConnectDialog () {
      this.dialogConnectSetting = true
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
          console.log(JSON.stringify(this.treeNodes))
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
    nodeExpand () {
      console.log(this.$refs.mycode.content)
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
    },
    async createNewNodeSubmit () {
      let path = ''
      let node = this.createNodeForm.node
      while (node.data.id !== 'ZK-TREE-ROOT') {
        path = '/' + node.data.label + path
        node = node.parent
      }
      path = path + '/' + this.createNodeForm.name
      const resultData = await this.$http.post(ZookeeperApi.ZK_CREATE, {
        path: path,
        data: this.createNodeForm.data
      })
      if (resultData.data && resultData.data.code === 1) {
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
  overflow-y: hidden;
  overflow-x: auto;
}
.tree /deep/ .el-tree-node.is-expanded > .el-tree-node__children {
  display: inline;
}
</style>
