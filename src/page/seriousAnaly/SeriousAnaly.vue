<template>
    <div>
        <ModuleTitle :iconName="'paper-airplane'" :moduleName="'高发路段统计分析'"></ModuleTitle>
        <Card class='module-card'>
          <div class="toolbar">
            <Select @on-change="yearChange" class="toolbar-formItem" v-model="year">
                  <Option value="">--</Option>
                  <Option v-for="i in (new Date().getFullYear() - 2000)" :value="2000+i" :key="i">{{2000+i}}</Option>
            </Select>
            <Select @on-change="init" class="toolbar-formItem" v-model="value1">
                  <Option v-for="(item, index) in cascaderData" :value="item.monitorId" :key="index">{{item.area+'/'+item.monitor}}</Option>
            </Select>
          </div>
          <div class="line-card">
            <div class="card-contaner">
              <h3>表格</h3>
              <rcTable ref="rcTable" :tableAttr='tableAttr'></rcTable>
            </div>
            <div class="card-contaner">
              <div id="pieChart" class="card-content"></div>
            </div>
          </div>
          <div class="line-card">
              <div id="myChart" class="card-content"></div>
          </div>
          <div id="line" class="line-card">
            <div id="lineChart" class="card-content"></div>
          </div>
        </Card>
    </div>
</template>

<script>
import ModuleTitle from '@/components/titleMain/ModuleTitle'
import rcTable from '@/components/rcTable'
import ajax from '@/util/ajax'

export default {
  name: 'SeriousAnaly',
  components: {
    ModuleTitle,
    rcTable
  },
  data: function () {
    return {
      tableAttr: {
        url: 'api/hifs/record/getTRecords',
        params: {},
        border: true, // 是否显示纵向边框 默认false
        disabledHover: true, // 取消鼠标悬停的高亮，默认false
        size: 'large', // 表格尺寸，可选值为 large、small、default 或者不填
        columnsChooseShow: false, // 是否展示列选择框
        showExportBtns: true, // 是否展示默认导出栏
        searchShow: false, // 是否展示搜索组件
        pageData: {
          show: false // 是否展示分页
        },
        columns: [
          {
            title: '区域名',
            key: 'area'
          },
          {
            title: '监控节点',
            key: 'monitor'
          },
          {
            title: '违法事件统计',
            key: 'totalType'
          }
        ]
      },
      cascaderData: [],
      value1: '',
      year: '',
      pieLegend: [],
      pieData: [],
      tLegend: [],
      tData: [],
      lineLegend: [],
      lineData: []
    }
  },
  mounted () {
    let year = new Date().getFullYear()
    this.yearChange(year)
  },
  methods: {
    yearChange (year) {
      ajax.get({
        url: 'api/hifs/record/getHighMonitorInfo?year=' + year,
        param: {},
        success: res => {
          this.cascaderData = res.data
          this.year = year
          this.value1 = res.data.length > 0 ? res.data[0].monitorId : ''
          console.log(res.data, this.value1)
          this.init()
        }
      })
    },
    init () {
      this.pieInit()
      this.tableInit()
      this.tInit()
      this.lineInit()
    },
    tableInit () {
      let param = new URLSearchParams()
      param.append('year', this.year)
      param.append('monitorId', this.value1 || 'noMonitor')
      this.$refs.rcTable.refreshTable(param)
    },
    pieInit () {
      let monitorId = this.value1 || 'noMonitor'
      ajax.get({
        url: 'api/hifs/record/getPieRecords?monitorId=' + monitorId + '&year=' + this.year,
        param: {},
        success: res => {
          let lArr = []
          for (let i in res.data) {
            lArr.push(res.data[i].name)
          }
          this.pieLegend = lArr
          this.pageData = res.data
          // 基于准备好的dom，初始化echarts实例
          let pieChart = this.$echarts.init(document.getElementById('pieChart'))

          pieChart.setOption({
            title: {
              text: '违法案件发生比例',
              x: 'center'
            },
            tooltip: {
              trigger: 'item',
              formatter: '{a} <br/>{b} : {c} ({d}%)'
            },
            legend: {
              orient: 'vertical',
              left: 'left',
              data: lArr
            },
            toolbox: {
              feature: {
                saveAsImage: {}
              }
            },
            series: [
              {
                name: '访问来源',
                type: 'pie',
                radius: '55%',
                center: ['50%', '60%'],
                data: res.data,
                itemStyle: {
                  emphasis: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                  }
                }
              }
            ]
          })
        }
      })
    },
    tInit () {
      let param = new URLSearchParams()
      param.append('year', this.year)
      param.append('monitorId', this.value1 || 'noMonitor')
      ajax.post({
        url: 'api/hifs/record/getTRecords',
        param: param,
        success: res => {
          let lArr = []
          let dArr = []
          for (let i in res.data.data) {
            lArr.push(res.data.data[i].area + '/' + res.data.data[i].monitor)
            dArr.push(res.data.data[i].totalType)
          }
          this.tLegend = lArr
          this.tData = dArr
          // 基于准备好的dom，初始化echarts实例
          let myChart = this.$echarts.init(document.getElementById('myChart'))
          // 绘制图表
          myChart.setOption({
            title: {
              text: '监控节点违法记录统计',
              x: 'center'
            },
            tooltip: {},
            toolbox: {
              feature: {
                saveAsImage: {}
              }
            },
            xAxis: {
              data: lArr
            },
            yAxis: {},
            series: [{
              name: '违法记录',
              type: 'bar',
              data: dArr
            }]
          })
        }
      })
    },
    lineInit () {
      let monitorId = this.value1 || 'noMonitor'
      document.getElementById('line').innerHtml = '<div id="lineChart" class="card-content"></div>'
      ajax.get({
        url: 'api/hifs/record/getLineRecords?year=' + this.year + '&monitorId=' + monitorId,
        param: {},
        success: res => {
          let lArr = []
          let dArr = []
          let series = []
          for (let i in res.data) {
            lArr.push(res.data[i].monitor)
            let data = res.data[i].data
            let lineData = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
            for (let i in data) {
              lineData[parseInt(data[i].month) - 1] = data[i].num
            }
            dArr.push(lineData)
            series.push({
              name: res.data[i].monitor,
              type: 'line',
              stack: '总量',
              data: lineData
            })
          }
          this.lineLegend = lArr
          this.lineData = dArr
          // 基于准备好的dom，初始化echarts实例
          let lineChart = this.$echarts.init(document.getElementById('lineChart'))
          lineChart.setOption({
            title: {
              text: '监控节点事件统计'
            },
            tooltip: {
              trigger: 'axis'
            },
            legend: {
              data: lArr
            },
            toolbox: {
              feature: {
                saveAsImage: {}
              }
            },
            xAxis: {
              type: 'category',
              boundaryGap: false,
              data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
            },
            yAxis: {
              type: 'value'
            },
            series: series
          })
        }
      })
    }
  }
}
</script>

<style lang="less" scoped>

.line-card{
  width: 100%;
  height: 450px;
  margin: 20px 0px;
  display: flex;
  .card-contaner{
    width:50%;
    height: 450px;
    padding: 20px;
    h3{
      text-align: center;
      margin: 5px 0px;
    }
  }
}

.card-content{
  width: 100%;
  height: 100%;
  div{
    margin: 0px auto;
  }
}

.toolbar{
  margin: 15px 20px;
  display: flex;
  .toolbar-formItem{
    width: 300px;
    margin: 0px 10px;
  }
}
</style>
