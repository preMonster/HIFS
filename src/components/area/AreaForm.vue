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
        <FormItem label="区域名"
                  prop="name">
            <aMapCascade ref="map" :isDisabled="modalData[0]===1" :aMapOnChange="aMapOnChange"></aMapCascade>
            <Input v-show="false" v-model="formData.name" placeholder="区域名" disabled/>
        </FormItem>
        <FormItem label="是否启用路面监控"
                  prop="isMonitor">
            <Select v-model="formData.isMonitor" placeholder="选择启用" :disabled="modalData[0]===1">
                <Option value="0">关闭</Option>
                <Option value="1">启用</Option>
            </Select>
        </FormItem>
        <FormItem label="描述">
            <Input v-model="formData.areaDescribe" placeholder="描述" :disabled="modalData[0]===1"/>
        </FormItem>
      </Form>
    </Modal>
  </div>
</template>

<script>
import aMapCascade from '@/components/aMap/aMapCascade'

export default {
  props: ['changeModal', 'modalData', 'save'],
  data () {
    return {
      modal: false,
      formData: {
        id: '',
        name: '',
        isMonitor: '0',
        areaDescribe: ''
      },
      ruleValidate: {
        name: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ],
        isMonitor: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  components: {
    aMapCascade
  },
  watch: {
    modalData (newModalData) {
      if (newModalData[2]) {
        const data = newModalData[2]
        this.formData.id = data.id
        this.formData.name = data.name
        this.formData.isMonitor = data.isMonitor
        this.formData.areaDescribe = data.areaDescribe
        this.$refs.map.setStreetAndStreets(data.name)
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
      this.$refs.map.reset()
      this.$refs.modalForm.resetFields()
    },
    aMapOnChange (areas) {
      this.formData.name = areas
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
