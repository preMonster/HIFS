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
              <i-table border stripe :content="self" :columns="columns" :data="tableData"></i-table>
            </div>
          </div>
          <ModuleForm :modal="modal" :changeModal='changeModal'></ModuleForm>
        </Card>
    </div>
</template>

<script>
import IconsChoose from '@/components/iconsChoose/IconsChoose'
import ModuleTitle from '@/components/titleMain/ModuleTitle'
import ModuleForm from '@/components/module/ModuleForm'
import ajax from '@/util/ajax'

export default{
  name: 'module',
  data () {
    return {
      self: this,
      iconName: '',
      modal: false,
      baseData: [],
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
      }],
      showFunc: this.show,
      tableData: []
    }
  },
  components: {
    IconsChoose,
    ModuleTitle,
    ModuleForm
  },
  mounted: function () {
    ajax.get({
      url: 'static/module.json',
      success: res => {
        this.baseData = res.data
        this.tableData = res.data
        for (let i in this.tableData) {
          this.tableData[i].show = this.show
          this.tableData[i].edit = this.edit
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
      this.changeModal(1)
    },
    edit (index) {
      this.changeModal(2)
    },
    changeModal () {
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
</style>
