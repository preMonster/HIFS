<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>

export default {
  name: 'App'
}
</script>

<style lang="less">
html, body{
  min-width:1000px;
  height:100%;
}

#app {
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑",Arial,sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  height: 100%;
}

.font-size18 {
  font-size: 18px;
}

.margin-right5 {
  margin-right: 5px;
}
.toolbars {
  font-size: 18px;
  text-align: right;
  padding: 10px 20px;
  .addIcon {
    color: #00cb66;
    margin-right: 5px;
    position: relative;
    top: -2px;
  }

  .deleteIcon {
    font-size: 22px;
    color: red;
  }
}
</style>
