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
        <FormItem label="节点名"
                  prop="name">
            <Input v-model="formData.name" placeholder="节点名" :disabled="modalData[0]===1"/>
        </FormItem>
        <FormItem label="所属区域"
                  prop="areaId">
            <Select v-model="formData.areaId" placeholder="选择所属区域" :disabled="modalData[0]===1">
                <Option v-for="(item, index) in areaList" :value="item.id + ''" :key="index">{{item.name}}</Option>
            </Select>
        </FormItem>
        <FormItem label="是否运行"
                  prop="isRun">
            <Select v-model="formData.isRun" placeholder="选择运行" :disabled="modalData[0]===1">
                <Option value="0">关闭</Option>
                <Option value="1">运行</Option>
            </Select>
        </FormItem>
        <FormItem label="描述">
            <Input v-model="formData.mtorDescribe" placeholder="描述" :disabled="modalData[0]===1"/>
        </FormItem>
      </Form>
    </Modal>
  </div>
</template>

<script>
export default {
  props: ['areaList', 'modalData', 'save'],
  data () {
    return {
      modal: false,
      formData: {
        id: '',
        name: '',
        areaId: '',
        isRun: '0',
        mtorDescribe: ''
      },
      ruleValidate: {
        name: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ],
        areaId: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ],
        isRun: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  watch: {
    modalData (newModalData) {
      if (newModalData[2]) {
        const data = newModalData[2]
        this.formData.id = data.id
        this.formData.name = data.name
        this.formData.areaId = data.areaId
        this.formData.isRun = data.isRun
        this.formData.mtorDescribe = data.mtorDescribe
        console.log(this.formData)
      }
    }
  },
  methods: {
    ok () {
      let params = new URLSearchParams()
      for (let i in this.formData) {
        params.append(i, this.formData[i])
      }
      if (this.modalData[0] !== 1) {
        this.save(params)
      }
      this.handleReset()
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
