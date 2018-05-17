<template>
    <div>
        <ModuleTitle :iconName="'paper-airplane'" :moduleName="'监控节点管理'"></ModuleTitle>
        <Card class='module-card'>
          <div class="toolbars-container">
            <div class='toolbars'>
              <div class="areaChoose" style='display:inline-block'>
                <Select v-model="areaId" @on-change="refreshTable" placeholder="所属区域">
                    <Option v-for="(item, index) in areaList" :value="item.id" :key="index">{{item.name}}</Option>
                </Select>
              </div>
              <div style='display:inline-block' @click='changeModal(0)'>
                <Icon type='ios-plus addIcon' title='新增'></Icon>
              </div>
              <div style='display:inline-block' @click='changeDeleteModal()'>
                <Icon type='trash-a deleteIcon' title='删除'></Icon>
              </div>
            </div>
          </div>
          <rcTable ref="rcTable" :tableAttr="tableAttr" :onSelectChange="onSelectChange"></rcTable>
        </Card>
        <MonitorForm ref="modal"
                  :areaList='areaList'
                  :modalData='modalData'
                  :save="save"></MonitorForm>
    </div>
</template>

<script>
import ModuleTitle from '@/components/titleMain/ModuleTitle'
import ajax from '@/util/ajax'
import rcTable from '@/components/rcTable'
import MonitorForm from '@/components/monitor/MonitorForm'

export default {
  name: 'area',
  data: function () {
    return {
      areaId: '',
      areaList: [],
      modalData: [0, '新增', {}],
      chooseRows: [],
      tableAttr: {
        url: 'api/hifs/monitor/getMonitorsByPage',
        params: {},
        border: true, // 是否显示纵向边框 默认false
        width: '100%', // 表格宽度，单位 px 默认auto
        disabledHover: true, // 取消鼠标悬停的高亮，默认false
        size: 'large', // 表格尺寸，可选值为 large、small、default 或者不填
        columnsChooseShow: true, // 是否展示列选择框
        searchShow: true, // 是否展示搜索组件
        pageData: {
          show: true, // 是否展示分页
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
            title: '节点名',
            key: 'name'
          },
          {
            title: '所属区域',
            key: 'areaName',
            render (h, params) {
              return h(
                'p',
                {},
                params.row.areaName ? params.row.areaName : '-'
              )
            },
            disabledSearch: true
          },
          {
            title: '运行状态',
            key: 'isRun',
            render (h, params) {
              return h(
                'p',
                {},
                params.row.isRun === '0' ? '关闭' : '运行'
              )
            },
            disabledSearch: true
          },
          {
            title: '描述',
            key: 'mtorDescribe',
            render (h, params) {
              return h(
                'p',
                {},
                params.row.mtorDescribe ? params.row.mtorDescribe : '-'
              )
            },
            disabledSearch: true
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
      }
    }
  },
  mounted () {
    let that = this
    ajax.get({
      url: 'api/hifs/area/getRunningArea',
      success: function (res) {
        that.areaList = res.data
        that.areaId = that.areaList[0].id
        that.refreshTable()
      }
    })
  },
  components: {
    ModuleTitle,
    rcTable,
    MonitorForm
  },
  methods: {
    show (row) {
      this.changeModal(1, row)
    },
    edit (row) {
      this.changeModal(2, row)
    },
    changeModal (num, data) {
      let title = num === 0 ? '新增' : num === 1 ? '查看' : num === 2 ? '编辑' : ''
      this.modalData = [num, title, data]
      this.$refs.modal.openTheModal()
    },
    onSelectChange (selectedRows) {
      this.chooseRows = selectedRows
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
            param.append('monitors', JSON.stringify(rows))
            ajax.post({
              url: 'api/hifs/monitor/deleteMonitor',
              param: param,
              success: res => {
                that.refreshTable()
              }
            })
          }
        })
      }
    },
    save (params) {
      const that = this
      ajax.post({
        url: 'api/hifs/monitor/save',
        param: params,
        success: res => {
          that.refreshTable()
        }
      })
    },
    refreshTable () {
      let param = new URLSearchParams()
      param.append('areaId', this.areaId)
      this.$refs.rcTable.refreshTable(param)
    }
  }
}
</script>

<style lang="less">
.areaChoose{
  min-width: 300px;
  margin-right: 20px;
  .ivu-select-single .ivu-select-selection .ivu-select-placeholder, .ivu-select-single .ivu-select-selection .ivu-select-selected-value{
    text-align: left;
  }
  .ivu-select-dropdown{
    text-align: left;
  }
}
</style>
