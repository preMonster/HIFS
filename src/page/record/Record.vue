<template>
    <div>
        <ModuleTitle :iconName="'paper-airplane'" :moduleName="'违法记录'"></ModuleTitle>
        <Card class='module-card'>
          <div class="toolbars-container">
            <div class='toolbars'>
              <div style='display:inline-block;' @click='changeModal(0)'>
                <Icon type='ios-plus addIcon' title='新增'></Icon>
              </div>
              <div style='display:inline-block' @click='changeDeleteModal()'>
                <Icon type='trash-a deleteIcon' title='删除'></Icon>
              </div>
              <div style='display:inline-block'>
                <Upload
                    :before-upload="handleUpload"
                    action="//jsonplaceholder.typicode.com/posts/">
                    <Button type="ghost" icon="ios-cloud-upload-outline">自动识别图片</Button>
                </Upload>
              </div>
            </div>
          </div>
          <rcTable ref="rcTable" :tableAttr="tableAttr" :onSelectChange="onSelectChange"></rcTable>
        </Card>
        <RecordForm ref="modal"
                  :modalData='modalData'
                  :save="save"></RecordForm>
    </div>
</template>

<script>
import ModuleTitle from '@/components/titleMain/ModuleTitle'
import rcTable from '@/components/rcTable'
import ajax from '@/util/ajax'
import RecordForm from '@/components/record/RecordForm'

export default {
  name: 'record',
  data () {
    return {
      modalData: [0, '新增', {}],
      chooseRows: [],
      tableAttr: {
        url: 'api/hifs/record/getRecordsByPage',
        params: {},
        border: true, // 是否显示纵向边框 默认false
        width: '100%', // 表格宽度，单位 px 默认auto
        disabledHover: true, // 取消鼠标悬停的高亮，默认false
        size: 'large', // 表格尺寸，可选值为 large、small、default 或者不填
        columnsChooseShow: true, // 是否展示列选择框
        searchShow: true, // 是否展示搜索组件
        showExportBtns: true, // 是否展示默认导出栏
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
            title: '违法类型',
            key: 'illegalType',
            disabledSearch: true
          },
          {
            title: '违法人',
            key: 'peopleName'
          },
          {
            title: '身份证号',
            key: 'uno'
          },
          {
            title: '电话',
            key: 'phone',
            disabledSearch: true
          },
          {
            title: '车牌号',
            key: 'carno',
            disabledSearch: true
          },
          {
            title: '监控节点',
            key: 'monitorName',
            disabledSearch: true
          },
          {
            title: '所属区域',
            key: 'areaName',
            disabledSearch: true
          },
          {
            title: '开始时间',
            key: 'beginDate',
            render (h, params) {
              let date = new Date(params.row.beginDate)
              return h(
                'p',
                date.getFullYear() + '-' +
                     (date.getMonth() + 1) + '-' +
                     date.getDate() + ' ' +
                     date.getHours() + ':' +
                     date.getMinutes()
              )
            },
            disabledSearch: true
          },
          {
            title: '结束时间',
            key: 'endDate',
            render (h, params) {
              let date = new Date(params.row.endDate)
              return h(
                'p',
                date.getFullYear() + '-' +
                     (date.getMonth() + 1) + '-' +
                     date.getDate() + ' ' +
                     date.getHours() + ':' +
                     date.getMinutes()
              )
            },
            disabledSearch: true
          },
          {
            title: '违法截图',
            key: 'evidence',
            render (h, params) {
              return h(
                'img',
                {
                  attrs: {
                    src: params.row.evidence
                  },
                  class: 'imgStyle'
                }
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
    RecordForm
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
            param.append('records', JSON.stringify(rows))
            ajax.post({
              url: 'api/hifs/record/deleteRecord',
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
    save (params, file) {
      const that = this
      let config = {
        headers: {'Content-Type': 'multipart/form-data'}
      }
      let url = file ? 'api/hifs/record/save' : 'api/hifs/record/saveNoFile'
      ajax.post({
        url: url,
        param: params,
        config: config,
        success: res => {
          let param = new URLSearchParams()
          that.$refs.rcTable.refreshTable(param)
        }
      })
    },
    handleUpload (file) {
      let config = {
        headers: {'Content-Type': 'multipart/form-data'}
      }
      let params = new FormData()
      params.append('file', file)
      ajax.post({
        url: 'api/hifs/record/saveByImg',
        param: params,
        config: config,
        success: res => {
          console.log(res)
        }
      })
      return false
    }
  }
}
</script>

<style lang="less">
.imgStyle{
  width: 40px;
  height: 40px;
}
</style>
