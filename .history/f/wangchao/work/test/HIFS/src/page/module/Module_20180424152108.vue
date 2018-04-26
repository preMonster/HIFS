<template>
    <div>
        <ModuleTitle :iconName="'paper-airplane'" :moduleName="'模块管理'"></ModuleTitle>
        <Card class="module-card">
          <div class="module-card-flex-box">
            <div class="left-module-div">
              <Tree :data="baseData" @on-select-change="getSelectedTree"></Tree>
            </div>
            <div class="right-module-div">
              <div class="toolbars">
                <div style="display:inline-block" @click="changeModal(0)"><Icon type="ios-plus addIcon" title="新增"></Icon></div>
                <Icon @click="changeDeleteModal()" type="trash-a deleteIcon" title="删除"></Icon>
              </div>
              <rcTable :data="tableData" :tableAttr="tableAttr"></rcTable>
            </div>
          </div>
          <ModuleForm :modal="modal" :changeModal='changeModal' :modalData='modalData'></ModuleForm>
        </Card>
    </div>
</template>

<script>
import IconsChoose from '@/components/iconsChoose/IconsChoose'
import ModuleTitle from '@/components/titleMain/ModuleTitle'
import ModuleForm from '@/components/module/ModuleForm'
import rcTable from '@/components/rcTable'
import ajax from '@/util/ajax'

export default{
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
          showExportBtns: true, // 是否展示默认导出栏
          pageData: {
              show: true, // 是否展示分页
              pageWays: 'client', // client 客户端，serve 服务端， 默认服务端
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
          }, {
            title: '模块名',
            key: 'name'
          }, {
            title: '模块编码',
            key: 'code'
          }, {
            title: '父级模块',
            key: 'parentName',
            render (h, params) {
              return h('p', {}, params.row.parentName ? params.row.parentName : '-')
            }
          }, {
            title: '等级',
            key: 'level'
          }, {
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
            }
          }, {
            title: '地址',
            key: 'url',
            align: 'center',
            render (h, params) {
              return h('p', {}, params.row.url ? params.row.url : '-')
            }
          }, {
            title: '操作',
            key: 'action',
            width: 150,
            align: 'center',
            render (h, params) {
              return h('p', [
                h('i-button', {
                  props: {
                    type: 'primary',
                    size: 'small'
                  },
                  class: 'margin-right5',
                  on: {
                    click: () => {
                      params.row.show(params.index)
                    }
                  }
                }, '查看'),
                h('i-button', {
                  props: {
                    type: 'warning',
                    size: 'small'
                  },
                  on: {
                    click: () => {
                      params.row.edit(params.index)
                    }
                  }
                }, '修改')
              ])
            }
          }]
      },
      showFunc: this.show,
      tableData: {
        total: 0,
        data:[]
      }
    }
  },
  components: {
    IconsChoose,
    ModuleTitle,
    ModuleForm,
    rcTable
  },
  created: function () {
    ajax.get({
      url: 'static/module.json',
      success: res => {
        this.baseData = res.data.data
        this.tableData = res.data
        for (let i in this.tableData.data) {
          this.tableData.data[i].show = this.show
          this.tableData.data[i].edit = this.edit
        }
      }
    })
  },
  methods: {
    getIconName (iconName) {
      this.iconName = iconName
    },
    getSelectedTree (data) {
      console.log(data)
    },
    show (index) {
      this.changeModal(1, index)
    },
    edit (index) {
      this.changeModal(2, index)
    },
    changeModal (num, index) {
      let data = index ? this.tableData[index] : []
      this.modalData = num === 0 ? [0, '新增', data] : num === 1 ? [1, '查看', data] : [2, '编辑', data]
      this.modal = !this.modal
    }
  }
}
</script>

<style>
.module-card-flex-box{
  display: flex;
  min-height: 500px;
}

.left-module-div{
  width: 200px;
  border-right: 1px #ccc solid;
}

.right-module-div{
  width: 100%;
  padding: 0px 30px;
  position: relative;
}

.font-size18{
  font-size: 18px;
}

.margin-right5{
  margin-right: 5px;
}
.toolbars{
  font-size: 18px;
  text-align: right;
  padding: 10px 20px;
}

.addIcon{
  color: #00cb66;
  margin-right: 5px;
  position: relative;
  top: -2px;
}

.deleteIcon{
  font-size: 22px;
  color: red;
}
.page{
  margin-top: 10px;
  text-align: right;
}

.demo-spin-icon-load{
    animation: ani-demo-spin 1s linear infinite;
}
@keyframes ani-demo-spin {
    from { transform: rotate(0deg);}
    50%  { transform: rotate(180deg);}
    to   { transform: rotate(360deg);}
}
.demo-spin-col{
    width: 100%;
    height: 100%;
    position: absolute;
    top: 0;
    left: 0;
    border: 1px solid #eee;
}
</style>
