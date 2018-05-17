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
        <FormItem label="违法信息"
                  prop="name">
            <Input v-model="formData.name" placeholder="违法信息" :disabled="modalData[0]===1"/>
        </FormItem>
        <FormItem label="处罚机制">
            <Input v-model="formData.punish" placeholder="处罚机制" :disabled="modalData[0]===1"/>
        </FormItem>
      </Form>
    </Modal>
  </div>
</template>

<script>
export default {
  props: ['modalData', 'save'],
  data () {
    return {
      modal: false,
      formData: {
        id: '',
        name: '',
        punish: ''
      },
      ruleValidate: {
        name: [
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
        this.formData.punish = data.punish
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
