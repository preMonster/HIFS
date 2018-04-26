<template>
    <div class="speInputDiv">
        <select name="sex" :disabled='state===0?true:false'
            v-model="newData.sex" @change="getDate"
            :class="state===0?'readonlyClass':'inputClass'">
          <option value="0">男</option>
          <option value="1">女</option>
        </select>

        <input name="phone" :readonly='state===0?true:false'
            v-model="newData.phone" @change="getDate" :placeholder="'请输入你的电话'"
            :class="state===0?'readonlyClass':'inputClass'"/>
    </div>
</template>

<script>
export default{
  props: ['state', 'data', 'changeData'],
  data: function () {
    return {
      newData: {}
    }
  },
  mounted: function () {
    this.newData = this.data
  },
  methods: {
    getDate: function () {
      this.changeData(this.newData)
    }
  }
}
</script>

<style>
.speInputDiv > input,select{
    width:180px;
    height:30px;
    margin:15px auto;
    outline:none;
    text-align:center;
    font-size:16px;
    display: block;
}

.speInputDiv > select{
    padding: 0 0 0 82px;
    appearance:none;
    -moz-appearance:none;
    -webkit-appearance:none;
}

.speInputDiv > select > option{
    text-align: center;
}

.inputClass{
    border:none;
    border-bottom:1px #ccc solid;
}

.readonlyClass{
    border:none;
}
</style>
