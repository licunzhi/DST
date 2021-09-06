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
        <el-button :type="zkStopType" icon="el-icon-error" circle @click="removeZkClient"></el-button>
        <el-button type="primary" icon="el-icon-refresh" circle></el-button>
      </el-col>
    </el-row>
    <!--支撑部数据展示-->
    <el-row>
      <el-col :span="4" :offset="1">
        <el-tree
          :data="data"
          show-checkbox
          node-key="id"
          default-expand-all
          :expand-on-click-node="false">
      <span class="custom-tree-node" slot-scope="{ node, data }">
         <el-tooltip class="item" effect="dark" :content=node.label placement="right">
            <span class="custom-tree-node-label">{{ node.label }}</span>
        </el-tooltip>
        <span>
          <el-button
            type="text"
            size="mini"
            icon="el-icon-plus"
            @click="() => append(data)">
          </el-button>
          <el-button
            type="text"
            size="mini"
            icon="el-icon-delete"
            @click="() => remove(node, data)">
          </el-button>
        </span>
      </span>
        </el-tree>
      </el-col>
      <el-col :span="18" :offset="1">
        <el-tabs v-model="activeName" @tab-click="handleClick">
          <el-tab-pane label="Node Data" name="first"><pre v-highlightA><code>{{ innerHtml }}</code></pre></el-tab-pane>
          <el-tab-pane label="Node Metadata" name="second">
            <el-table :data="tableData" style="width: 100%">
              <el-table-column prop="k" label="k" width="180"></el-table-column>
              <el-table-column prop="v" label="v" width="180"></el-table-column>
              <el-table-column prop="alias" label="alias"></el-table-column>
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="Node ACLs" name="third">
            <el-table :data="tableData" style="width: 100%">
              <el-table-column prop="k" label="k" width="180"></el-table-column>
              <el-table-column prop="v" label="v" width="180"></el-table-column>
              <el-table-column prop="alias" label="alias"></el-table-column>
            </el-table>
          </el-tab-pane>
        </el-tabs>
      </el-col>
    </el-row>

    <!--创建初始化链接弹窗-->
    <el-dialog title="Connection Setting" :visible.sync="dialogVisible" width="30%" :before-close="handleClose">
      <el-form ref="form" :model="settingForm" label-width="100px">
        <el-form-item label="Connect Url">
          <el-input v-model="settingForm.zkUrl"></el-input>
        </el-form-item>
        <el-form-item label="Sleep time">
          <el-input v-model="settingForm.baseSleepTimeMs"></el-input>
        </el-form-item>
        <el-form-item label="Max Retries">
          <el-input v-model="settingForm.maxRetries"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="onSubmit">connect</el-button>
          <el-button @click="dialogVisible= false">cancel</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
let id = 1000

export default {
  name: 'Zookeeper',
  data () {
    const data = [{
      id: 1,
      label: '12345678901234567890123456789012345678901234567890',
      children: [{
        id: 4,
        label: '二级 1-1',
        children: [{
          id: 9,
          label: '三级 1-1-1'
        }, {
          id: 10,
          label: '三级 1-1-2'
        }]
      }]
    }, {
      id: 2,
      label: '一级 2',
      children: [{
        id: 5,
        label: '二级 2-1'
      }, {
        id: 6,
        label: '二级 2-2'
      }]
    }, {
      id: 3,
      label: '一级 3',
      children: [{
        id: 7,
        label: '二级 3-1'
      }, {
        id: 8,
        label: '二级 3-2'
      }]
    }]
    return {
      data: JSON.parse(JSON.stringify(data)),
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
      dialogVisible: false,
      settingForm: {
        name: '',
        zkUrl: 'localhost:2181',
        baseSleepTimeMs: 5000,
        maxRetries: 1
      },
      zkConnectType: 'success',
      zkStopType: 'info'
    }
  },

  methods: {
    append (data) {
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

    renderContent (h, {node, data, store}) {
      return (
        <span class="custom-tree-node">
          <span class="custom-tree-node-label">{node.label}</span>
          <span>
            <el-button size="mini" type="text" icon="el-icon-plus" on-click={() => this.append(data)}></el-button>
            <el-button size="mini" type="text" icon="el-icon-delete" on-click={() => this.remove(node, data)}></el-button>
          </span>
        </span>)
    },

    openConnectDialog () {
      if (this.zkConnectType === 'success') {
        this.dialogVisible = true
      }
    },
    onSubmit () {
      console.log('提交创建链接信息操作')
      this.zkConnectType = 'info'
      this.dialogVisible = false
      this.zkStopType = 'danger'
    },
    removeZkClient () {
      this.zkConnectType = 'success'
      this.zkStopType = 'info'
    }
  }
}
</script>

<style scoped>
.el-row {
  margin-bottom: 20px
}

.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
  overflow: hidden;
}

.custom-tree-node-label {
  width: 74%;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
