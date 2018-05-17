<template>
    <div>
        <ModuleTitle :iconName="'paper-airplane'" :moduleName="'基础路段统计分析'"></ModuleTitle>
        <Card class='module-card'>
          <div id="myChart" :style="{width: '300px', height: '300px'}"></div>
        </Card>
    </div>
</template>

<script>
import ModuleTitle from '@/components/titleMain/ModuleTitle'

export default {
  name: 'BaseAnaly',
  components: {
    ModuleTitle
  },
  mounted () {
    // 基于准备好的dom，初始化echarts实例
    let myChart = this.$echarts.init(document.getElementById('myChart'))
    // 绘制图表
    myChart.setOption({
      title: { text: '' },
      tooltip: {},
      xAxis: {
        data: ['衬衫', '羊毛衫', '雪纺衫', '裤子', '高跟鞋', '袜子']
      },
      yAxis: {},
      series: [{
        name: '销量',
        type: 'bar',
        data: [5, 20, 36, 10, 10, 20]
      }]
    })
  }
}
</script>

<style lang="less">

</style>
