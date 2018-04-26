<template>
  <div class="test">
    <rcTable ref="rcTable"
             :tableAttr="tableAttr"
             :onPageChange="onPageChange"
             :onPageSizeChange="onPageSizeChange"
             :onCurrentChange="onCurrentChange"
             :onSelect="onSelect"
             :onSelectCancel="onSelectCancel"
             :onSelectAll="onSelectAll"
             :onSelectChange="onSelectChange"
             :onSortChange="onSortChange"
             :onFilterChange="onFilterChange"
             :onRowClick="onRowClick"
             :onRowDblclick="onRowDblclick"
             :onExpand="onExpand"></rcTable>
    <Button @click="onExport">导出</Button>
    <Button @click="onClear">清除高亮</Button>
    <Button @click="refreshTable()">刷新表格</Button>
  </div>
</template>

<script>
import rcTable from '@/components/rcTable.vue'
export default {
  name: 'test',
  data: function () {
    return {
      tableAttr: {
        url: 'http://localhost:3001/refresh',
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
        columns: [
          {
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
            // filters: [
            //     {
            //         label: '大于24',
            //         value: 1
            //     },
            //     {
            //         label: '等于24',
            //         value: 2
            //     },
            //     {
            //         label: '小于24',
            //         value: 3
            //     }
            // ],
            // filterMethod: (value, row) => {
            //     if (value === 1) {
            //         return row.age > 24
            //     } else if (value === 2) {
            //         return row.age === 24
            //     } else {
            //         return row.age < 24
            //     }
            // },
            // filterMultiple: true
          },
          {
            title: 'Address',
            key: 'address',
            disabledSearch: true
          }
        ]
      }
    }
  },
  components: {
    rcTable
  },
  methods: {
    rowClassName (row, index) {
      if (index % 2 === 0) {
        return 'bag-c-ccc'
      }
    },
    onPageChange (current) { // 返回页码
      console.log(current)
    },
    onPageSizeChange (pageSize) { // 返回每页条数
      console.log(pageSize)
    },
    onCurrentChange (currentRow, oldCurrentRow) { // 开启 highlight-row 后有效，当表格的当前行发生变化的时候会触发
      console.log(currentRow, oldCurrentRow)
    },
    onSelect (selection, row) { // 在多选模式下有效，选中某一项时触发 {selection：已选项数据,row：刚选择的项数据}
      console.log(selection, row)
    },
    onSelectCancel (selection, row) { // 在多选模式下有效，选中某一项时触发 {selection：已选项数据,row：取消选择的项数据}
      console.log(selection, row)
    },
    onSelectAll (selection) { // 在多选模式下有效，点击全选时触发
      console.log(selection)
    },
    onSelectChange (selection) { // 在多选模式下有效，只要选中项发生变化时就会触发
      console.log(selection)
    },
    onSortChange (column, key, order) { // 排序时有效，当点击排序时触发
      console.log(column, key, order)
    },
    onFilterChange (row) { // 筛选时有效，筛选条件发生变化时触发
      console.log(row)
    },
    onRowClick (row, index) { // 单击某一行时触发
      console.log(row, index)
    },
    onRowDblclick (row, index) { // 双击某一行时触发
      console.log(row, index)
    },
    onExpand (row, status) { // 展开或收起某一行时触发
      console.log(row, status)
    },
    onExport () {
      console.log(this.$refs.rcTable)
      this.$refs.rcTable.$refs.table.exportCsv({
        filename: 'The original data'
      })
    },
    onClear () {
      this.$refs.rcTable.$refs.table.clearCurrentRow()
    },
    refreshTable () {
      this.$refs.rcTable.refreshTable()
    }
  }
}
</script>

<style lang="less">
.test {
    width: 1200px;
    margin: 100px auto;
}
.ivu-table .bag-c-ccc {
    td {
        background-color: #ccc !important;
        color: white;
    }
}
</style>
