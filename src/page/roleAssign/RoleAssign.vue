<template>
    <div class="roleAssign">
      <ModuleTitle :iconName="'paper-airplane'" :moduleName="'角色分配'"></ModuleTitle>
      <Card class='module-card roleAssign-container'>
        <Card>
            <h3>角色选择</h3>
            <RadioGroup v-model="chooseRoleId" vertical @on-change="getModulesByRoleId">
                <Radio v-for="(item, index) in roleData" :label="item.id" :key="index">
                    <span>{{item.name}}</span>
                </Radio>
            </RadioGroup>
        </Card>
        <Card>
            <h3>模块选择</h3>
            <Tree :data="treeData" show-checkbox @on-check-change="checkChange"></Tree>
        </Card>
      </Card>
    </div>
</template>

<script>
import ModuleTitle from '@/components/titleMain/ModuleTitle'
import ajax from '@/util/ajax'
export default{
  name: 'roleAssign',
  data: function () {
    return {
      baseTreeData: [],
      treeData: [],
      roleData: [],
      chooseRoleId: 1
    }
  },
  created () {
    let that = this
    ajax.get({
      url: 'api/hifs/role/getRoleList',
      param: {},
      success: function (res) {
        that.roleData = res.data
      }
    })
    ajax.get({
      url: 'api/hifs/module/getModuleTrees',
      param: {},
      success: function (res) {
        that.treeData = [ res.data ]
        that.baseTreeData = JSON.parse(JSON.stringify(that.treeData))
        that.getModulesByRoleId(1)
      }
    })
  },
  components: {
    ModuleTitle
  },
  methods: {
    checkChange (arr) {
      let modules = []
      for (let i in arr) {
        let obj = {
          moduleId: arr[i].id
        }
        modules[i] = obj
      }
      let param = new URLSearchParams()
      param.append('roleId', this.chooseRoleId)
      param.append('moduleIds', JSON.stringify(modules))
      ajax.post({
        url: 'api/hifs/roleAssign/changeRoleAssign',
        param: param
      })
    },
    getModulesByRoleId () {
      let that = this
      ajax.get({
        url: 'api/hifs/module/getModulesByRoleId?roleId=' + that.chooseRoleId,
        param: {},
        success: function (res) {
          that.treeData = JSON.parse(JSON.stringify(that.baseTreeData))
          that.createNewTreeData(res.data, that.treeData)
        }
      })
    },
    createNewTreeData (chooseData, treeData) {
      let data = treeData

      if (data.length <= 0) {
        return
      }

      for (let i in data) {
        let code = data[i].code ? data[i].code : ''
        for (let j in chooseData) {
          if (chooseData[j].code === code) {
            data[i].checked = true
          }
        }
        let children = data[i].children ? data[i].children : []
        this.createNewTreeData(chooseData, children)
      }
    }
  }
}
</script>

<style>
    .roleAssign-container>.ivu-card-body{
        display: flex;
    }

    .roleAssign-container .ivu-card-body .ivu-card{
        margin: 50px 20px;
    }
    h3{
      margin-bottom: 25px;
    }
</style>
