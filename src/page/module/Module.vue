<template>
    <div>
        <Title :iconName="'paper-airplane'" :moduleName="'模块管理'"></Title>
        <Card class="module-card">
          <div class="module-card-flex-box">
            <div class="left-module-div">
              <Tree :data="baseData" @on-select-change="getSelectedTree"></Tree>
            </div>
            <div class="right-module-div">
              <i-table border stripe :content="self" :columns="columns" :data="tableData"></i-table>
            </div>
          </div>
        </Card>
    </div>
</template>

<script>
import IconsChoose from '@/components/iconsChoose/IconsChoose'
import Title from '@/components/titleMain/Title'
import ajax from '@/util/ajax'

export default{
  name: 'module',
  data: function () {
    return {
      self: this,
      iconName: '',
      baseData: [{
        title: '全部模块',
        children: [{
          title: '系统管理',
          children: [{
            title: '模块管理'
          }]
        }]
      }, {
        title: '全部模块',
        children: [{
          title: '系统管理',
          children: [{
            title: '模块管理'
          }]
        }]
      }],
      columns: [{
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
        render (h, column) {
          return h('p', {}, column.row.parentName ? column.row.parentName : '-')
        }
      }, {
        title: '等级',
        key: 'level'
      }, {
        title: '图标',
        key: 'iconName',
        align: 'center',
        render (h, column) {
          return h('p', [
            h('Icon', {
              props: {
                type: column.row.iconName
              },
              class: 'font-size18'
            })
          ])
        }
      }, {
        title: '地址',
        key: 'url',
        align: 'center',
        render (h, column) {
          return h('p', {}, column.row.url ? column.row.url : '-')
        }
      }, {
        title: '操作',
        key: 'action',
        width: 150,
        align: 'center',
        render (h, column) {
          console.log(this)
          return h('p', [
            h('i-button', {
              props: {
                type: 'primary',
                size: 'small'
              },
              class: 'margin-right5',
              on: {
                click: () => {
                  this.show(column.index)
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
                }
              }
            }, '修改')
          ])
        }
      }],
      tableData: []
    }
  },
  components: {
    IconsChoose,
    Title
  },
  mounted: function () {
    ajax.get({
      url: 'static/module.json',
      success: res => {
        this.tableData = res.data
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
      console.log(index)
    },
    edit (index) {
      console.log(index)
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
}

.font-size18{
  font-size: 18px;
}

.margin-right5{
  margin-right: 5px;
}
</style>
