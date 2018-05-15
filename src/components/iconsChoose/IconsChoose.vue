<template>
  <div class="icons-div">
    <div v-for="icon in iconNames"
         class="iconChoose"
         :key="icon"
         @click="chooseIcon(icon)">
      <Icon :type="icon"></Icon>
    </div>
  </div>
</template>

<script>
import ajax from '@/util/ajax'

export default {
  props: ['getIcon'],
  name: 'IconsChoose',
  data: function () {
    return {
      iconNames: []
    }
  },
  beforeCreate: function () {
    ajax.get({
      url: 'static/icons.json',
      success: res => {
        this.iconNames = res.data
      }
    })
  },
  methods: {
    chooseIcon (iconName) {
      this.getIcon(iconName)
    }
  }
}
</script>

<style>
.icons-div {
    width: 100%;
    height: 300px;
    border: 1px #ccc solid;
    overflow-y: auto;
    display: flex;
    flex-wrap: wrap;
}

.icons-div::-webkit-scrollbar {
    /*滚动条整体样式*/
    width: 0px; /*高宽分别对应横竖滚动条的尺寸*/
    height: 0px;
}

.iconChoose {
    width: 50px;
    height: 50px;
    text-align: center;
    line-height: 40px;
    font-size: 30px;
    color: #5c6b77;
}
</style>
