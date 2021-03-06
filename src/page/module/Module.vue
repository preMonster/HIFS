<template>
  <div>
    <ModuleTitle :iconName="'paper-airplane'" :moduleName="'模块管理'"></ModuleTitle>
    <Card class='module-card'>
      <div class='module-card-flex-box'>
        <div class='left-module-div'>
          <Tree :data='baseData' @on-select-change='getSelectedTree'></Tree>
        </div>
        <div class='right-module-div'>
          <div class='toolbars'>
            <div style='display:inline-block' @click='changeModal(0)'>
              <Icon type='ios-plus addIcon' title='新增'></Icon>
            </div>
            <div style='display:inline-block' @click='changeDeleteModal()'>
              <Icon type='trash-a deleteIcon' title='删除'></Icon>
            </div>
          </div>
          <div class='table-container'>
            <rcTable ref="rcTable" :tableAttr='tableAttr' :onSelectChange="onSelectChange"></rcTable>
          </div>
        </div>
      </div>
      <ModuleForm ref="modal"
                  :changeModal='changeModal'
                  :modalData='modalData'
                  :treeData="baseData"
                  :save="save"></ModuleForm>
    </Card>
  </div>
</template>

<script>
import IconsChoose from '@/components/iconsChoose/IconsChoose'
import ModuleTitle from '@/components/titleMain/ModuleTitle'
import ModuleForm from '@/components/module/ModuleForm'
import rcTable from '@/components/rcTable'
import ajax from '@/util/ajax'

export default {
  name: 'module',
  data () {
    return {
      self: this,
      iconName: '',
      modal: false,
      spinShow: true,
      modalData: [0, '新增', {}],
      baseData: [],
      tableAttr: {
        url: 'api/hifs/module/getModulesByPage',
        params: {},
        stripe: false, // 是否显示间隔斑马纹 默认false
        border: true, // 是否显示纵向边框 默认false
        showHeader: true, // 是否显示表头，默认true
        width: '100%', // 表格宽度，单位 px 默认auto
        height: 'auto', // 表格高度，单位 px，设置后，如果表格内容大于此值，会固定表头
        loading: false, // 表格是否加载中，默认false
        disabledHover: true, // 取消鼠标悬停的高亮，默认false
        highlightRow: false, // 是否支持高亮选中的行，即单选，默认false
        rowClassName: null, // 自定义行样式的方法，传入参数：row：当前行数据 index：当前行的索引 通过function返回行className
        size: 'large', // 表格尺寸，可选值为 large、small、default 或者不填
        noDataText: '', // 数据为空时显示的提示内容，默认暂无数据
        noFilteredDataText: '', // 列筛选时，数据为空时显示，默认暂无筛选结果
        columnsChooseShow: true, // 是否展示列选择框
        showExportBtns: false, // 是否展示默认导出栏
        searchShow: true, // 是否展示搜索组件
        pageData: {
          show: true, // 是否展示分页
          pageWays: 'serve', // client 客户端，serve 服务端， 默认服务端
          current: 1, // 当前页码
          pageSize: 5, // 每页条数
          pageSizeOpts: [5, 10, 20, 40], // 每页条数切换的配置
          showTotal: true, // 显示总数
          showSizer: true, // 显示分页，用来改变page-size
          showElevator: true // 显示电梯，可以快速切换到某一页
        },
        columns: [
          {
            type: 'selection',
            width: 60,
            align: 'center'
          },
          {
            title: '模块名',
            key: 'name'
          },
          {
            title: '模块编码',
            key: 'code'
          },
          {
            title: '父级模块',
            key: 'parentName',
            render (h, params) {
              return h(
                'p',
                {},
                params.row.parentName ? params.row.parentName : '-'
              )
            },
            disabledSearch: true
          },
          {
            title: '等级',
            key: 'level',
            disabledSearch: true
          },
          {
            title: '图标',
            key: 'iconName',
            align: 'center',
            render (h, params) {
              return h('p', [
                h('Icon', {
                  props: {
                    type: params.row.iconName
                  },
                  class: 'font-size18'
                })
              ])
            },
            disabledSearch: true
          },
          {
            title: '地址',
            key: 'addres',
            align: 'center',
            disabledSearch: true,
            render (h, params) {
              return h('p', {}, params.row.addres ? params.row.addres : '-')
            }
          },
          {
            title: '操作',
            key: 'action',
            width: 150,
            align: 'center',
            disabledSearch: true,
            render: (h, params) => {
              return h('p', [
                h(
                  'Button',
                  {
                    props: {
                      type: 'primary',
                      size: 'small'
                    },
                    class: 'margin-right5',
                    on: {
                      click: () => {
                        this.show(params.row)
                      }
                    }
                  },
                  '查看'
                ),
                h(
                  'Button',
                  {
                    props: {
                      type: 'warning',
                      size: 'small'
                    },
                    on: {
                      click: () => {
                        this.edit(params.row)
                      }
                    }
                  },
                  '修改'
                )
              ])
            }
          }
        ]
      },
      showFunc: this.show,
      tableData: {
        total: 0,
        data: []
      },
      chooseTreeData: {},
      chooseRows: []
    }
  },
  components: {
    IconsChoose,
    ModuleTitle,
    ModuleForm,
    rcTable
  },
  created: function () {
    this.getTreeData()
  },
  methods: {
    getIconName (iconName) {
      this.iconName = iconName
    },
    getSelectedTree (data) {
      let param = new URLSearchParams()
      param.append('parent_code', data[0].code)
      this.chooseTreeData = data[0]
      this.$refs.rcTable.refreshTable(param)
    },
    show (index) {
      this.changeModal(1, index)
    },
    edit (index) {
      this.changeModal(2, index)
    },
    changeModal (num, index) {
      let data = index
      let title = num === 0 ? '新增' : num === 1 ? '查看' : num === 2 ? '编辑' : ''
      this.modalData = [num, title, data]
      this.$refs.modal.openTheModal()
    },
    changeDeleteModal () {
      let that = this
      let rows = this.chooseRows
      if (rows.length === 0) {
        this.$Modal.error({
          title: '删除',
          content: '请选择删除项！'
        })
      } else {
        this.$Modal.error({
          title: '删除',
          content: '确认删除？',
          onOk: function () {
            let param = new URLSearchParams()
            param.append('modules', JSON.stringify(rows))
            ajax.post({
              url: 'api/hifs/module/deleteModule',
              param: param,
              success: res => {
                let param = new URLSearchParams()
                if (that.chooseTreeData.code) {
                  param.append('parent_code', that.chooseTreeData.code)
                }
                that.getTreeData()
                that.$refs.rcTable.refreshTable(param)
              }
            })
          }
        })
      }
    },
    save (params) {
      const that = this
      ajax.post({
        url: 'api/hifs/module/save',
        param: params,
        success: res => {
          let param = new URLSearchParams()
          if (that.chooseTreeData.code) {
            param.append('parent_code', that.chooseTreeData.code)
          }
          that.getTreeData()
          that.$refs.rcTable.refreshTable(param)
        }
      })
    },
    onSelectChange (selectedRows) {
      this.chooseRows = selectedRows
    },
    getTreeData () {
      ajax.get({
        url: 'api/hifs/module/getModuleTrees',
        success: res => {
          let data = [ res.data ]
          this.baseData = data
        }
      })
    }
  }
}
</script>

<style>
.module-card-flex-box {
  padding-left: 200px;
  min-height: 500px;
  position: relative;
}

.left-module-div {
  width: 200px;
  height: 100%;
  border-right: 1px #ccc solid;
  position: absolute;
  left: 0;
  top: 0;
}

.right-module-div {
  width: 100%;
  padding: 0px 30px;
  position: relative;
}

.font-size18 {
  font-size: 18px;
}

.margin-right5 {
  margin-right: 5px;
}

.toolbars {
  font-size: 18px;
  text-align: right;
  padding: 10px 20px;
}

.addIcon {
  color: #00cb66;
  margin-right: 5px;
  position: relative;
  top: -2px;
}

.deleteIcon {
  font-size: 22px;
  color: red;
}

.page {
  margin-top: 10px;
  text-align: right;
}

.demo-spin-icon-load {
  animation: ani-demo-spin 1s linear infinite;
}

@keyframes ani-demo-spin {
  from {
    transform: rotate(0deg);
  }
  50% {
    transform: rotate(180deg);
  }
  to {
    transform: rotate(360deg);
  }
}

.demo-spin-col {
  width: 100%;
  height: 100%;
  position: absolute;
  top: 0;
  left: 0;
  border: 1px solid #eee;
}
</style>
