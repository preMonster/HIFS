<template>
  <div class="Modal">
    <Modal v-model="modal"
           :title="modalData[1]"
           @on-ok="ok"
           @on-cancel="cancel">
      <Form ref="modalForm"
            :model="formData"
            :rules="ruleValidate"
            :label-width="80"
            label-position="left">
        <FormItem label="用户名"
                  prop="userName">
            <Input v-model="formData.userName" placeholder="用户名" :disabled="modalData[0]===1"/>
        </FormItem>
        <FormItem label="密码"
                  prop="password">
            <Input type="password" v-model="formData.password" placeholder="密码" :disabled="modalData[0]===1"/>
        </FormItem>
        <FormItem label="角色名"
                  prop="roleId">
            <Select v-model="formData.roleId" placeholder="选择角色" :disabled="modalData[0]===1">
                <Option v-for="(item, index) in roleList" :value="item.id+''" :key="index">{{item.name}}</Option>
            </Select>
        </FormItem>
        <FormItem label="身份证号"
                  prop="uno">
            <Input v-model="formData.uno" placeholder="身份证号" :disabled="modalData[0]===1"/>
        </FormItem>
        <FormItem label="性别"
                  prop="sex">
            <Select v-model="formData.sex" placeholder="选择性别" :disabled="modalData[0]===1">
                <Option value="0">男</Option>
                <Option value="1">女</Option>
            </Select>
        </FormItem>
        <FormItem label="真实名称">
            <Input v-model="formData.realName" placeholder="真实名称" :disabled="modalData[0]===1"/>
        </FormItem>
        <FormItem label="电话"
                  prop="phone">
            <Input v-model="formData.phone" placeholder="电话" :disabled="modalData[0]===1"/>
        </FormItem>
        <FormItem label="邮箱"
                  prop="email">
            <Input v-model="formData.email" placeholder="邮箱" :disabled="modalData[0]===1"/>
        </FormItem>
      </Form>
    </Modal>
  </div>
</template>

<script>
import ajax from '@/util/ajax'

export default {
  props: ['modalData', 'save'],
  data () {
    const validatePhone = (rule, value, callback) => {
      if (!(/^1[3|4|5|7|8][0-9]\d{4,8}$/.test(value))) {
        callback(new Error('电话号有误'))
      }
    }
    return {
      modal: false,
      roleList: [],
      formData: {
        id: '',
        userName: '',
        password: '',
        roleId: '',
        uno: '',
        sex: '',
        realName: '',
        phone: '',
        email: ''
      },
      ruleValidate: {
        userName: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '不能为空', trigger: 'blur' },
          { type: 'string', min: 6, max: 12, message: '密码位数大于等于6，小于等于12', trigger: 'blur' }
        ],
        roleId: [
          { required: true, message: '不能为空', trigger: 'change' }
        ],
        uno: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ],
        sex: [
          { required: true, message: '不能为空', trigger: 'change' }
        ],
        phone: [
          { validator: validatePhone, trigger: 'change' }
        ],
        email: [
          { type: 'email', message: '邮箱格式错误', trigger: 'change' }
        ]
      }
    }
  },
  created () {
    let that = this
    ajax.get({
      url: 'api/hifs/role/getRoleList',
      param: {},
      success: res => {
        that.roleList = res.data
      }
    })
  },
  watch: {
    modalData (newModalData) {
      if (newModalData[2]) {
        const data = newModalData[2]
        this.formData.id = data.id
        this.formData.userName = data.userName
        this.formData.password = data.password
        this.formData.roleId = data.roleId + ''
        this.formData.uno = data.uno
        this.formData.sex = data.sex + ''
        this.formData.realName = data.realName
        this.formData.phone = data.phone
        this.formData.email = data.email
      }
    }
  },
  methods: {
    ok () {
      let that = this
      let params = new URLSearchParams()
      for (let i in that.formData) {
        params.append(i, that.formData[i])
      }
      if (that.modalData[0] !== 1) {
        that.save(params)
      }
      that.handleReset()
    },
    cancel () {
      this.handleReset()
    },
    openTheModal () {
      this.modal = true
    },
    handleReset () {
      this.formData.id = ''
      this.$refs.modalForm.resetFields()
    }
  }
}
</script>

<style lang="less">
.tree ul li{
  margin: 1px 0px;
}
.icon-choose-div{
  width: 100%;
  position: relative;
  .icon-choose{
    position: absolute;
    background-color: white;
  }
}
.disabledClick{
  cursor:not-allowed;
}
</style>
