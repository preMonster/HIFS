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
        <FormItem label="违法类型"
                  prop="illegalType">
          <Select v-model="formData.illegalType" placeholder="选择监控节点" :disabled="modalData[0]===1">
              <Option v-for="(item, index) in illegalList" :value="item.name" :key="index">{{item.name}}</Option>
          </Select>
        </FormItem>
        <FormItem label="违法人"
                  prop="peopleName">
            <Input v-model="formData.peopleName" placeholder="违法人" :disabled="modalData[0]===1"/>
        </FormItem>
        <FormItem label="身份证号"
                  prop="uno">
            <Input v-model="formData.uno" placeholder="身份证号" :disabled="modalData[0]===1"/>
        </FormItem>
        <FormItem label="电话"
                  prop="phone">
            <Input v-model="formData.phone" placeholder="电话" :disabled="modalData[0]===1"/>
        </FormItem>
        <FormItem label="车牌号">
            <Input v-model="formData.carno" placeholder="车牌号" :disabled="modalData[0]===1"/>
        </FormItem>
        <FormItem label="监控节点"
                  prop="monitorId">
            <Select v-model="formData.monitorId" placeholder="选择监控节点" :disabled="modalData[0]===1">
                <Option v-for="(item, index) in monitorList" :value="item.id+''" :key="index">{{item.areaName+"/"+item.name}}</Option>
            </Select>
        </FormItem>
        <FormItem label="违法时间段"
                  prop="date">
            <DatePicker v-model="date" type="datetimerange" format="yyyy-MM-dd HH:mm" placeholder="选择时间" style="width: 100%" :disabled="modalData[0]===1"></DatePicker>
        </FormItem>
        <FormItem label="违法截图">
            <Upload
                :before-upload="handleUpload"
                action="//jsonplaceholder.typicode.com/posts/">
                <Button type="ghost" icon="ios-cloud-upload-outline" :disabled="modalData[0]===1">上传图片</Button>
            </Upload>
            <img class="imgStyle" v-if="imgDataUrl" :src="imgDataUrl" >
        </FormItem>
      </Form>
    </Modal>
  </div>
</template>

<script>
import ajax from '@/util/ajax'

export default {
  props: ['changeModal', 'modalData', 'save'],
  data () {
    const validatePhone = (rule, value, callback) => {
      if (!(/^1[3|4|5|7|8][0-9]\d{4,8}$/.test(value))) {
        callback(new Error('电话号有误'))
      }
    }
    return {
      modal: false,
      imgDataUrl: '',
      monitorList: [],
      illegalList: [],
      file: null,
      date: [],
      formData: {
        id: '',
        illegalType: '',
        peopleName: '',
        uno: '',
        phone: '',
        carno: '',
        monitorId: '',
        evidence: ''
      },
      ruleValidate: {
        illegalType: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ],
        peopleName: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ],
        uno: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '不能为空', trigger: 'blur' },
          { validator: validatePhone, trigger: 'change' }
        ],
        monitorId: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ],
        date: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  watch: {
    modalData (newModalData) {
      if (newModalData[2]) {
        const data = newModalData[2]
        let bd = new Date(data.beginDate)
        let ed = new Date(data.endDate)
        this.formData.id = data.id
        this.formData.illegalType = data.illegalType
        this.formData.peopleName = data.peopleName
        this.formData.uno = data.uno
        this.formData.phone = data.phone
        this.formData.monitorId = data.monitorId
        this.formData.evidence = data.evidence
        this.formData.carno = data.carno
        this.date = [
          bd,
          ed
        ]
        this.imgDataUrl = data.evidence
      }
    }
  },
  created () {
    ajax.get({
      url: 'api/hifs/monitor/getRunningMonitor',
      param: {},
      success: res => {
        this.monitorList = res.data
      }
    })
    ajax.get({
      url: 'api/hifs/illegal/getIllegalList',
      param: {},
      success: res => {
        this.illegalList = res.data
        this.illegalList.push({'name': '其他'})
      }
    })
  },
  methods: {
    ok () {
      let params = new FormData()
      for (let i in this.formData) {
        params.append(i, this.formData[i])
      }
      let beginDateStr = this.date[0]
      let endDateStr = this.date[1]
      params.append('beginDate', this.getStrDate(beginDateStr))
      params.append('endDate', this.getStrDate(endDateStr))
      if (this.file) {
        params.append('file', this.file)
      }
      if (this.modalData[0] !== 1) {
        this.save(params, this.file)
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
      this.imgDataUrl = ''
      this.file = null
      this.date = []
      this.$refs.modalForm.resetFields()
    },
    handleUpload (file) {
      this.file = file
      let that = this
      this.getBase64(file, (url) => {
        that.imgDataUrl = 'data:image/png;base64,' + url // 显示的图片
      })
      return false
    },
    getBase64: function (file, callback) {
      var maxWidth = 640
      if (file) {
        var thisFile = file
        if (thisFile.size > 2019200) {
          // mualert.alertBox("图片不能超过800K")
          alert('图片不能超过2M')
          return
        }
        var reader = new FileReader()
        reader.onload = function (event) {
          var imgUrl = event.target.result
          var img = new Image()
          img.onload = function () {
            var canvasId = 'canvasBase64Imgid'
            var canvas = document.getElementById(canvasId)
            if (canvas != null) {
              document.body.removeChild(canvas)
            }
            canvas = document.createElement('canvas')
            canvas.innerHTML = 'New Canvas'
            canvas.setAttribute('id', canvasId)
            canvas.style.display = 'none'
            document.body.appendChild(canvas)
            canvas.width = this.width
            canvas.height = this.height
            var imageWidth = this.width
            var imageHeight = this.height
            if (this.width > maxWidth) {
              imageWidth = maxWidth
              imageHeight = this.height * maxWidth / this.width
              canvas.width = imageWidth
              canvas.height = imageHeight
            }
            var context = canvas.getContext('2d')
            context.clearRect(0, 0, imageWidth, imageHeight)
            context.drawImage(this, 0, 0, imageWidth, imageHeight)
            var base64 = canvas.toDataURL('image/png', 1)
            var imgbase = base64.substr(22)
            callback(imgbase)
          }
          img.src = imgUrl
        }
        reader.readAsDataURL(file)
      }
    },
    getStrDate (str) {
      let date = new Date(str)
      return date.getFullYear() + '-' +
                     (date.getMonth() + 1) + '-' +
                     date.getDate() + ' ' +
                     date.getHours() + ':' +
                     date.getMinutes()
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
.imgStyle{
  width:100px;
  height:100px;
}
</style>
