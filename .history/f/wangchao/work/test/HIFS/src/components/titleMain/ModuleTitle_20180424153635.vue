<template>
  <Card class="Title-title-card">
    <div class="flex-box">
      <div>
        <icon :type="iconName"></icon>
      </div>
      <div class="Title-titleName">
        <span>{{moduleName}}</span>
      </div>
    </div>
  </Card>
</template>

<script>
export default {
  name: 'ModuleTitle',
  props: ['iconName', 'moduleName']
}
</script>

<style>
.Title-title-card {
    width: 95%;
    margin: 20px auto;
    padding: 0px 20px;
    background: white;
}

.flex-box {
    display: flex;
    font-size: 26px;
}

.Title-titleName {
    margin-left: 10px;
}
</style>
