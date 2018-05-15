<template>
  <div class='rc-table'>
    <div v-if='tableStyleClass.searchShow' class='search-table-div'>
      <div class='search-table-input-div'>
        <div class='search-input'>
          <Input size='large' v-model='searchValue' placeholder='搜索'/>
          <Dropdown trigger='click' placement='bottom-start' @on-visible-change='changeSearchColState'>
            <div @click='changeSearchColState()' class='col-search-select'>
              <div :title='colSearchChoose' class='col-type'>
                {{colSearchChoose}}
              </div>
              <Icon :type="colSearchState?'arrow-up-b':'arrow-down-b'"></Icon>
            </div>
            <DropdownMenu slot='list'>
              <DropdownItem v-for='(item,index) in colSearchList' :key='index'>
                <div class='drop-down-div' :title='item.title' @click='chooseColSearch(item.key,item.title)'>{{item.title}}</div>
              </DropdownItem>
            </DropdownMenu>
          </Dropdown>
        </div>
        <Button type='primary' @click='search()' class='sc-btn'>搜索</Button>
      </div>
      <div v-if='Object.keys(searchValueList).length>0' class='tags-div'>
        <Tag v-for='(value,key,index) in searchValueList' type='border' closable color='blue' :key='index' @on-close='handleCloseTags(key)'>{{key}} : {{value}}</Tag>
      </div>
    </div>
    <div class='relative'>
      <i-table
        id="table"
        ref="table"
        class="rc-table-class"
        :columns="tableColumns"
        :data="tableData"
        :stripe="tableStyleClass.stripe?tableStyleClass.stripe:false"
        :border="tableStyleClass.border?tableStyleClass.border:false"
        :show-header="tableStyleClass.showHeader===false?tableStyleClass.showHeader:true"
        :width="tableStyleClass.width?tableStyleClass.width:'auto'"
        :height="tableStyleClass.height?tableStyleClass.height:'auto'"
        :loading='loading'
        :disabled-hover='tableStyleClass.disabledHover?tableStyleClass.disabledHover:false'
        :highlight-row='tableStyleClass.highlightRow?tableStyleClass.highlightRow:false'
        :row-class-name='tableStyleClass.rowClassName?tableStyleClass.rowClassName:function(){}'
        :size="tableStyleClass.size?tableStyleClass.size:'default'"
        :no-data-text="tableStyleClass.noDataText?tableStyleClass.noDataText:'暂无数据'"
        :no-filtered-data-text="tableStyleClass.noFilteredDataText?tableStyleClass.noFilteredDataText:'暂无筛选结果'"
        @on-current-change='currentChange'
        @on-select='select'
        @on-select-cancel='selectCancel'
        @on-select-all='selectAll'
        @on-selection-change='selectChange'
        @on-sort-change='sortChange'
        @on-filter-change='filterChange'
        @on-row-click='rowClick'
        @on-row-dblclick='rowDblclick'
        @on-expand='expand'></i-table>
      <div v-if='tableStyleClass.columnsChooseShow' class='columns-choose-show-container'>
        <div @click='changeChooseColumnsState' class='icon-show'>
          <Icon type='navicon-round'></Icon>
        </div>
        <div v-if='isShowChooseColumns' class='columns-choose'>
          <Checkbox class='check' v-for='(item, index) in tableStyleClass.columns' v-if='item.key' :key='index' v-model='columnState[item.key]'
            :disabled='item.disabledChoose' @on-change='changeChoose'>{{item.title}}</Checkbox>
        </div>
      </div>
    </div>
    <div class='footer' style='margin: 10px;overflow: hidden'>
      <div v-if='pageData.show'>
        <Page
          :total='total'
          :current='current'
          :page-size='pageSize'
          :page-size-opts='pageSizeOpts'
          :show-total='showTotal'
          :show-elevator='showSizer'
          :show-sizer='showElevator'
          @on-change='pageChange'
          @on-page-size-change='pageSizeChange'></Page>
      </div>
      <div v-if='tableStyleClass.showExportBtns' class='export-btns'>
        <Button type='primary' size='large' @click='exportData(1)'>
          <Icon type='ios-download-outline'></Icon> 导出原数据</Button>
        <Button type='primary' size='large' @click='exportData(2)'>
          <Icon type='ios-download-outline'></Icon> 导出过滤排序后的数据 </Button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'rc-table',
  props: [
    'columns',
    'data',
    'params',
    'tableAttr',
    'rcTableState',
    'onPageSizeChange',
    'onPageChange',
    'onCurrentChange',
    'onSelect',
    'onSelectCancel',
    'onSelectAll',
    'onSelectChange',
    'onSortChange',
    'onFilterChange',
    'onRowClick',
    'onRowDblclick',
    'onExpand'
  ],
  data: function () {
    return {
      searchValue: '',
      searchValueList: {},
      colSearchState: false,
      colSearchList: [],
      colSearchChoose: '',
      colSearchChooseKey: '',
      currentNum: 1,
      pageSizeNum: 10,
      baseData: [],
      total: 0,
      tableData: [],
      isShowChooseColumns: false,
      tableColumns: [],
      columnState: {},
      sort: {},
      tableParams: new URLSearchParams(),
      loading: false
    }
  },
  computed: {
    tableStyleClass () {
      let tableClass = this.tableAttr ? this.tableAttr : []
      return tableClass
    },
    pageData () {
      let pageData = this.tableStyleClass.pageData
        ? this.tableStyleClass.pageData
        : []
      return pageData
    },
    current () {
      return this.pageData.current ? this.pageData.current : 1
    },
    pageSize () {
      return this.pageData.pageSize ? this.pageData.pageSize : 10
    },
    pageSizeOpts () {
      return this.pageData.pageSizeOpts
        ? this.pageData.pageSizeOpts
        : [10, 20, 30, 40]
    },
    showTotal () {
      return this.pageData.showTotal ? this.pageData.showTotal : false
    },
    showSizer () {
      return this.pageData.showSizer ? this.pageData.showSizer : false
    },
    showElevator () {
      return this.pageData.showElevator ? this.pageData.showElevator : false
    }
  },
  created () {
    this.currentNum = this.current
    this.pageSizeNum = this.pageSize
    this.tableColumns = this.tableStyleClass.columns
    this.tableParams = Object.keys(this.tableStyleClass.params).length > 0 ? this.tableStyleClass.params : new URLSearchParams()
    this.loading = this.tableStyleClass.loading
    let columns = this.tableStyleClass.columns
    for (let i in columns) {
      if (columns[i].key) {
        this.columnState[columns[i].key] = true
      }
    }
    for (let i in columns) {
      if (columns[i].key && !columns[i].disabledSearch) {
        this.colSearchList.push(columns[i])
      }
    }
    this.colSearchChoose = this.colSearchList[0].title
    this.colSearchChooseKey = this.colSearchList[0].key
    this.refreshTable(this.tableParams)
  },
  mounted () {
    if (this.$refs.table.height === 'auto' || !this.$refs.table.height) {
      this.$refs.table.scrollBarWidth = 0
    }
  },
  methods: {
    pageChange (current) {
      // 页码改变
      this.currentNum = current
      if (this.pageData.pageWays === 'client') {
        // 客户端分页
        this.changePage()
      } else {
        this.refreshTable(this.tableParams)
      }
      if (this.onPageChange) {
        this.onPageChange(current)
      }
    },
    pageSizeChange (pageSize) {
      // 页条数改变
      this.pageSizeNum = pageSize
      if (this.pageData.pageWays === 'client') {
        this.changePage()
      } else {
        this.refreshTable(this.tableParams)
      }
      if (this.onPageSizeChange) {
        this.onPageSizeChange(pageSize)
      }
    },
    changePage () {
      // 根据客户端分页改变表格
      let page = this.currentNum
      let pageSize = this.pageSizeNum
      let start = (page - 1) * pageSize
      let end = page * pageSize
      this.tableData = this.baseData.data.slice(start, end)
      for (let i in this.tableData) {
        this.tableData[i].rc_table_index = i
      }
    },
    changeChooseColumnsState () {
      // 关闭或者打开列选择框
      this.isShowChooseColumns = !this.isShowChooseColumns
    },
    changeChoose () {
      // 改变列
      let columns = this.tableStyleClass.columns
      let newColumns = []
      for (let i in columns) {
        if (!columns[i].key) {
          newColumns.push(columns[i])
        } else if (this.columnState[columns[i].key]) {
          newColumns.push(columns[i])
        }
      }

      this.tableColumns = newColumns
    },
    currentChange (currentRow, oldCurrentRow) {
      // 当前行发生变化（单选）事件
      delete currentRow['rc_table_index']
      delete oldCurrentRow['rc_table_index']
      if (this.onCurrentChange) {
        this.onCurrentChange(currentRow, oldCurrentRow)
      }
    },
    select (selection, row) {
      // 在多选模式下有效，选中某一项时触发 {selection：已选项数据,row：刚选择的项数据}
      let selectedRows = this.delteObjDeepCopy(selection)
      delete row['rc_table_index']
      if (this.onSelect) {
        this.onSelect(selectedRows, row)
      }
    },
    selectCancel (selection, row) {
      // 在多选模式下有效，选中某一项时触发 {selection：已选项数据,row：取消选择的项数据}
      let selectedRows = this.delteObjDeepCopy(selection)
      delete row['rc_table_index']
      if (this.onSelectCancel) {
        this.onSelectCancel(selectedRows, row)
      }
    },
    selectAll (selection) {
      // 在多选模式下有效，点击全选时触发
      let selectedRows = this.delteObjDeepCopy(selection)
      if (this.selectAll) {
        this.onSelectAll(selectedRows)
      }
    },
    selectChange (selection) {
      // 在多选模式下有效，只要选中项发生变化时就会触发
      let that = this
      this.$refs.table.rowClassName = function (row, index) {
        // 给选中的行改变样式
        for (let i in selection) {
          if (index === parseInt(selection[i].rc_table_index)) {
            return 'bag-c-selected'
          }
        }
        if (that.tableStyleClass.rowClassName) {
          // 如果设了默认样式改变，加上
          let tableClass = that.tableStyleClass.rowClassName(row, index)
          return tableClass
        }
      }
      let selectedRows = this.delteObjDeepCopy(selection)
      if (this.onSelectChange) {
        this.onSelectChange(selectedRows)
      }
    },
    sortChange (column, key, order) {
      // 在排序模式下有效，只要选中项发生变化时就会触发
      if (column.order === 'normal') {
        delete this.sort[column.key]
      } else {
        this.sort[column.key] = column.order
      }
      this.refreshTable(this.tableParams)
      if (this.onSortChange) {
        this.onSortChange(column, key, order)
      }
    },
    filterChange (row) {
      // 筛选时有效，筛选条件发生变化时触发
      if (this.onFilterChange) {
        this.onFilterChange(row)
      }
    },
    rowClick (row, index) {
      // 单击某一行时触发
      delete row['rc_table_index']
      if (this.onRowClick) {
        this.onRowClick(row, index)
      }
    },
    rowDblclick (row, index) {
      // 双击某一行时触发
      delete row['rc_table_index']
      if (this.onRowDblclick) {
        this.onRowDblclick(row, index)
      }
    },
    expand (row, status) {
      // 展开或收起某一行时触发
      delete row['rc_table_index']
      if (this.onExpand) {
        this.onExpand(row, status)
      }
    },
    exportData (type) {
      // 导出数据
      if (type === 1) {
        this.$refs.table.exportCsv({
          filename: 'data'
        })
      } else if (type === 2) {
        this.$refs.table.exportCsv({
          filename: 'Sorting-and-filtering-data',
          original: false
        })
      }
    },
    delteObjDeepCopy (source) {
      // 深克隆data
      let sourceCopy = []
      for (let i in source) {
        let obj = {}
        for (let item in source[i]) {
          obj[item] = source[i][item]
        }
        delete obj['rc_table_index']
        sourceCopy.push(obj)
      }
      return sourceCopy
    },
    getTableData (params) {
      const that = this
      // 获取数据源
      return this.$axios({
        method: 'post',
        url: that.tableStyleClass.url,
        data: params
      })
    },
    refreshTable (params) {
      // 刷新表格
      let that = this
      if (!params) {
        params = new URLSearchParams()
      }
      let obj = new URLSearchParams(params)
      this.tableParams = obj
      if (that.pageData.pageWays !== 'client') {
        // params.pageNumber = this.currentNum
        // params.pageSize = this.pageSizeNum
        params.append('pageNumber', this.currentNum)
        params.append('pageSize', this.pageSizeNum)
      }
      if (Object.keys(this.sort).length > 0) {
        // params.sort = this.sort
        params.append('sort', this.sort)
      }
      for (let i in this.searchValueList) {
        params.append(i, this.searchValueList[i])
      }
      this.loading = true
      this.getTableData(params)
        .then(function (response) {
          that.baseData = response.data
          that.total = that.baseData.total ? that.baseData.total : 0
          that.tableData = that.baseData.data ? that.baseData.data : []
          for (let i in that.tableData) {
            that.tableData[i].rc_table_index = i
          }
          if (that.pageData.pageWays === 'client') {
            that.changePage()
          }
          that.loading = false
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    changeSearchColState (visible) {
      // 改变列搜索选择框的状态
      this.colSearchState = visible
    },
    chooseColSearch (key, title) {
      // 选择列进行搜索
      this.colSearchChoose = title
      this.colSearchChooseKey = key
      this.searchValue = ''
    },
    search () {
      // 搜索
      if (!this.searchValue) {
        this.refreshTable(this.tableParams)
        return
      }
      this.searchValueList[this.colSearchChooseKey] = this.searchValue
      this.refreshTable(this.tableParams)
      this.searchValue = ''
    },
    handleCloseTags (key) {
      // 关闭标签
      delete this.searchValueList[key]
      this.refreshTable(this.tableParams)
    }
  }
}
</script>

<style lang='less'>
.rc-table {
  width: 100%;
  height: 100%;
  position: relative;
  .search-table-div {
    width: 100%;
    padding: 10px 0px;
    .search-table-input-div {
      .search-input {
        width: 350px;
        margin-right: 20px;
        display: inline-block;
        position: relative;
        .ivu-input-wrapper {
          padding-left: 80px;
          input {
            border-top-left-radius: 0px;
            border-bottom-left-radius: 0px;
          }
        }
        .ivu-dropdown {
          position: absolute;
          height: 36px;
          top: 0px;
          left: 0px;
          .ivu-dropdown-rel {
            height: 100%;
          }
          .ivu-dropdown-item {
            padding: 0px;
            .drop-down-div {
              width: 100%;
              height: 100%;
              padding: 7px 16px;
            }
          }
          .col-search-select {
            width: 80px;
            height: 100%;
            line-height: 30px;
            text-align: center;
            background-color: #ededee;
            border-right: 1px #dcddf0 solid;
            border-top-left-radius: 4px;
            border-bottom-left-radius: 4px;
            .col-type {
              width: 60px;
              display: inline-block;
              font-size: 12px;
              margin-top: 2px;
              overflow: hidden;
              white-space: nowrap;
              text-overflow: ellipsis;
            }
            i {
              float: right;
              margin-right: 5px;
              margin-top: 12px;
            }
          }
        }
      }
      .sc-btn {
        width: 70px;
        height: 35px;
        display: inline-block;
      }
    }
    .tags-div {
      margin-top: 10px;
    }
  }
  .relative {
    position: relative;
  }
  .columns-choose-show-container {
    position: absolute;
    top: 0px;
    right: 0px;
    .icon-show {
      width: 17px;
      height: 48px;
      text-align: center;
      font-size: 16px;
      line-height: 48px;
      cursor: pointer;
    }
    .columns-choose {
      position: absolute;
      top: 45px;
      right: 2px;
      z-index: 999;
      background-color: #fff;
      border: 1px solid rgba(0, 0, 0, 0.15);
      border-radius: 4px;
      box-shadow: 0 6px 12px rgba(0, 0, 0, 0.175);
      padding: 20px 15px;
      display: flex;
      flex-direction: column;
      text-align: left;
      .check {
        width: max-content;
        padding: 5px 0px;
        white-space: nowrap;
      }
    }
  }
  .ivu-table .bag-c-selected {
    td {
      background-color: gray !important;
      color: white;
    }
  }
  .footer {
    display: flex;
    justify-content: space-between;
  }
  table {
    width: 100% !important;
  }
}
</style>
