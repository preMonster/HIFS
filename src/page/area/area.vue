<template>
    <div>
        <ModuleTitle :iconName="'paper-airplane'" :moduleName="'区域管理'"></ModuleTitle>
        <Card class='module-card'>
            <div class="toolbars-container">
            <div class='toolbars'>
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
        <AreaForm ref="modal"
                    :modalData='modalData'
                    :save="save"></AreaForm>
    </div>
</template>

<script>
import ModuleTitle from '@/components/titleMain/ModuleTitle'
import rcTable from '@/components/rcTable'
import AreaForm from '@/components/area/AreaForm'
import ajax from '@/util/ajax'

export default {
  name: 'area',
  data: function () {
    return {
      modalData: [0, '新增', {}],
      chooseRows: [],
      tableAttr: {
        url: 'api/hifs/area/getAreasByPage',
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
            title: '区域名',
            key: 'name'
          },
          {
            title: '是否启用监控',
            key: 'isMonitor',
            render (h, params) {
              return h(
                'p',
                {},
                params.row.isMonitor === '0' ? '关闭' : '启用'
              )
            },
            disabledSearch: true
          },
          {
            title: '描述',
            key: 'areaDescribe',
            render (h, params) {
              return h(
                'p',
                {},
                params.row.areaDescribe ? params.row.areaDescribe : '-'
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
  components: {
    ModuleTitle,
    rcTable,
    AreaForm
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
            param.append('areas', JSON.stringify(rows))
            ajax.post({
              url: 'api/hifs/area/deleteArea',
              param: param,
              success: res => {
                let param = new URLSearchParams()
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
        url: 'api/hifs/area/save',
        param: params,
        success: res => {
          let param = new URLSearchParams()
          that.$refs.rcTable.refreshTable(param)
        }
      })
    }
  }
}
</script>

<style lang="less">

</style>
