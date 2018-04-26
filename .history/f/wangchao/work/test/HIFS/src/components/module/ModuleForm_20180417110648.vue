<template>
  <div class="Modal">
    <Modal
        v-model="modal"
        :title="modalData[1]"
        @on-ok="ok"
        @on-cancel="cancel">
        {{modalData[0]}}
    </Modal>
  </div>
</template>

<script>
export default{
  props: ['modal', 'changeModal', 'modalData'],
  methods: {
    ok () {
      this.$Message.info('点击了确定')
      this.changeModal()
    },
    cancel () {
      this.$Message.info('点击了取消')
      this.changeModal()
    }
  }
}
</script>
