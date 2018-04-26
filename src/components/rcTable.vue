<template>
  <div class="table">
    <div class="switch-div">
      斑马线
      <i-switch v-model="tableAttr.stripe"
                style="margin-right: 5px"></i-switch>
      纵向边框
      <i-switch v-model="tableAttr.border"
                style="margin-right: 5px"></i-switch>
      展示表头
      <i-switch v-model="tableAttr.showHeader"
                style="margin-right: 5px"></i-switch>
      取消鼠标悬停的高亮
      <i-switch v-model="tableAttr.disabledHover"
                style="margin-right: 5px"></i-switch>
      支持高亮选中的行
      <i-switch v-model="tableAttr.highlightRow"
                style="margin-right: 5px"></i-switch>
      展示列选择框
      <i-switch v-model="tableAttr.columnsChooseShow"
                style="margin-right: 5px"></i-switch>
      展示默认导出栏
      <i-switch v-model="tableAttr.showExportBtns"
                style="margin-right: 5px"></i-switch>
      展示搜索组件
      <i-switch v-model="tableAttr.searchShow"
                style="margin-right: 5px"></i-switch>
      展示分页
      <i-switch v-model="tableAttr.pageData.show"
                style="margin-right: 5px"></i-switch>
      展示分页总数
      <i-switch v-model="tableAttr.pageData.showTotal"
                style="margin-right: 5px"></i-switch>
      展示分页快速输入分页
      <i-switch v-model="tableAttr.pageData.showSizer"
                style="margin-right: 5px"></i-switch>
      展示电梯井
      <i-switch v-model="tableAttr.pageData.showElevator"
                style="margin-right: 5px"></i-switch>
      <div style="margin-top:20px">
        表格尺寸
        <RadioGroup v-model="tableAttr.size"
                    type="button">
          <Radio label="large"></Radio>
          <Radio label="small"></Radio>
          <Radio label="default"></Radio>
        </RadioGroup>
      </div>
      <div style="margin-top:20px">
        高度
        <Slider v-model="tableAttr.height"
                :max="800"
                :min="200"
                :step="10"></Slider>
      </div>
    </div>
    <rcTable :tableAttr="tableAttr"></rcTable>
  </div>
</template>

<script>
import rcTable from '@/components/rcTable'
export default {
  name: 'table',
  data: function () {
    return {
      tableAttr: {
        url: 'http://localhost:3001/refresh',
        params: {},
        stripe: false, // 是否显示间隔斑马纹 默认false
        border: false, // 是否显示纵向边框 默认false
        showHeader: true, // 是否显示表头，默认true
        height: 200,
        disabledHover: true, // 取消鼠标悬停的高亮，默认false
        highlightRow: false, // 是否支持高亮选中的行，即单选，默认false
        size: 'large', // 表格尺寸，可选值为 large、small、default 或者不填
        columnsChooseShow: true, // 是否展示列选择框
        showExportBtns: true, // 是否展示默认导出栏
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
        columns: [{
          type: 'selection', // 列类型，可选值为 index、selection、expand、html
          width: '80'
        },
        {
          title: 'Name', // 列头显示文字
          key: 'name', // 对应列内容的字段名
          disabledChoose: true // 是否能够进行列选择
        },
        {
          title: 'Age',
          key: 'age',
          sortable: 'true' // 对应列是否可以排序，如果设置为 custom，则代表用户希望远程排序，需要监听 Table 的 on-sort-change 事件
        },
        {
          title: 'Address',
          key: 'address',
          disabledSearch: true
        },
        {
          title: 'Action',
          width: 150,
          align: 'center',
          render: (h, params) => {
            return h('div', [
              h('Button', {
                props: {
                  type: 'primary',
                  size: 'small'
                },
                style: {
                  marginRight: '5px'
                },
                on: {
                  click: () => {
                    this.show(params.row)
                  }
                }
              }, 'View'),
              h('Button', {
                props: {
                  type: 'error',
                  size: 'small'
                },
                on: {
                  click: () => {
                    this.remove(params)
                  }
                }
              }, 'Delete')
            ])
          }
        }
        ]
      }
    }
  },
  components: {
    rcTable
  },
  methods: {
    show (row) {
      this.$Modal.info({
        title: 'User Info',
        content: `Name：${row.name}<br>Age：${row.age}<br>Address：${row.address}`
      })
    },
    remove (params) {
      console.log(params)
    }
  }
}
</script>

<style lang="less">
.table {
    width: 1200px;
    margin: 200px auto;
    .switch-div {
        width: 100%;
        margin-bottom: 10px;
        .ivu-switch {
            margin: 10px 0px;
        }
    }
}
</style>
