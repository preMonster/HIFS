<template>
  <div class="Modal">
    <Modal
        v-model="modal"
        title="普通的Modal对话框标题"
        @on-ok="ok"
        @on-cancel="cancel">
        <p>对话框内容</p>
        <p>对话框内容</p>
        <p>对话框内容</p>
    </Modal>
  </div>
</template>

<script>
export default{
  props: ['modal', 'changeModal'],
  methods: {
    ok () {
      this.$Message.info('点击了确定')
    },
    cancel () {
      this.$Message.info('点击了取消')
    }
  }
}
</script>
