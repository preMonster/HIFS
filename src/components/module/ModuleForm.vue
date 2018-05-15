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
        <FormItem label="模块名"
                  prop="name">
            <Input v-model="formData.name" placeholder="模块名" :disabled="modalData[0]===1"/>
        </FormItem>
        <FormItem v-show="modalData[0]!==0" label="模块编码">
            <Input v-model="formData.code" placeholder="模块编码" disabled/>
        </FormItem>
        <FormItem label="父级编码"
                  prop="parentCode">
            <Tree class="tree"
                  :data='baseTreeData'
                  @on-select-change='getSelectedFormTree'
                  :disabled="modalData[0]===1"></Tree>
            <Input v-show="false" v-model="formData.parent_code" disabled/>
            <span v-if="treeInfo" class="ivu-form-item-error-tip">不能为空</span>
        </FormItem>
        <FormItem label="等级"
                  prop="level">
            <Input v-model="formData.level" placeholder="等级" disabled/>
        </FormItem>
        <FormItem label="地址">
            <Input v-model="formData.addres" placeholder="地址" :disabled="modalData[0]===1"/>
        </FormItem>
        <FormItem label="图标"
                  prop="iconName">
              <div @click='modalData[0]!==1?changeIconChooseState():""'
                   :class="{'disabledClick':modalData[0]===1}">
                <Icon :type="formData.icon_name"></Icon>
              </div>
              <Input v-show="false" v-model="formData.icon_name" disabled/>
              <span v-if="iconInfo" class="ivu-form-item-error-tip">不能为空</span>
              <div v-if="iconChooseShow" class="icon-choose-div">
                <IconChoose :getIcon="getIcon" class="icon-choose"/>
              </div>
        </FormItem>
      </Form>
    </Modal>
  </div>
</template>

<script>
import IconChoose from '@/components/iconsChoose/IconsChoose'

export default {
  props: ['changeModal', 'modalData', 'treeData', 'save'],
  data () {
    return {
      modal: false,
      treeInfo: false,
      iconInfo: false,
      iconChooseShow: false,
      formData: {
        id: '',
        name: '',
        code: '',
        parent_code: '',
        level: 1,
        icon_name: 'paper-airplane',
        addres: ''
      },
      ruleValidate: {
        name: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ],
        parentCode: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ],
        level: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ],
        iconName: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  components: {
    IconChoose
  },
  watch: {
    modalData (newModalData) {
      if (newModalData[2]) {
        const data = newModalData[2]
        this.formData.id = data.id
        this.formData.name = data.name
        this.formData.code = data.code
        this.formData.parent_code = data.parent_code
        this.formData.level = data.level
        this.formData.icon_name = data.icon_name
        this.formData.addres = data.addres
      }
    }
  },
  computed: {
    baseTreeData: function () {
      let data = []
      let parentCode = this.modalData[2] ? this.modalData[2].parent_code : ''
      for (let i in this.treeData) {
        let child = this.treeData[i].children
        let obj = {
          code: this.treeData[i].code,
          title: this.treeData[i].title,
          level: 0
        }
        if (parentCode) {
          obj.expand = true
        } else {
          obj.selected = true
        }
        if (this.modalData[0] === 1) {
          obj.disabled = true
        }
        data[i] = obj
        data[i].children = []
        for (let j in child) {
          let obj1 = {
            code: child[j].code,
            title: child[j].title,
            level: 1
          }
          if (parentCode === child[j].code) {
            obj1.selected = true
          }
          if (this.modalData[0] === 1) {
            obj1.disabled = true
          }
          data[i].children[j] = obj1
        }
      }
      return data
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
    getSelectedFormTree (data) {
      if (data[0]) {
        this.treeInfo = false
        console.log(data[0])
        this.formData.level = parseInt(data[0].level) + 1 + ''
      } else {
        this.treeInfo = true
        this.formData.level = ''
      }
      this.formData.parent_code = data[0] ? data[0].code : ''
    },
    getIcon (iconName) {
      this.formData.icon_name = iconName
      this.changeIconChooseState()
    },
    changeIconChooseState () {
      this.iconChooseShow = !this.iconChooseShow
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
