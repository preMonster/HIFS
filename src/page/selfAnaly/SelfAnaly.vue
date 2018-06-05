<template>
    <div>
        <ModuleTitle :iconName="'paper-airplane'" :moduleName="'个人违法记录统计分析'"></ModuleTitle>
        <Card class='module-card'>
          <div class="toolbar">
            <Input v-model="uNo" placeholder="身份证号" class="toolbar-formItem"/>
            <Cascader :data="cascaderData" v-model="value1" class="toolbar-formItem"></Cascader>
            <Select class="toolbar-formItem" v-model="year">
                  <Option value="">--</Option>
                  <Option v-for="i in (new Date().getFullYear() - 2000)" :value="2000+i" :key="i">{{2000+i}}</Option>
            </Select>
            <i-button type="primary"
                      @click="init()">搜索</i-button>
          </div>
          <div class="line-card">
            <div>
              <h3>表格</h3>
              <rcTable ref="rcTable" :tableAttr='tableAttr'></rcTable>
            </div>
          </div>
          <div class="line-card">
            <div class="card-contaner">
              <div id="pieChart" class="card-content"></div>
            </div>
          </div>
          <div class="line-card">
            <div class="card-contaner">
              <div id="myChart" class="card-content"></div>
            </div>
          </div>
          <div class="line-card">
            <div class="card-contaner">
             <div id="slefContainer" class="map-container"></div>
            </div>
          </div>
        </Card>
    </div>
</template>

<script>
import ModuleTitle from '@/components/titleMain/ModuleTitle'
import rcTable from '@/components/rcTable'
import ajax from '@/util/ajax'
import AMap from 'AMap' // 在页面中引入高德地图

export default {
  name: 'SelfAnaly',
  components: {
    ModuleTitle,
    rcTable
  },
  data: function () {
    return {
      tableAttr: {
        url: 'api/hifs/record/getSelfTRecords',
        params: {},
        border: true, // 是否显示纵向边框 默认false
        disabledHover: true, // 取消鼠标悬停的高亮，默认false
        size: 'large', // 表格尺寸，可选值为 large、small、default 或者不填
        columnsChooseShow: false, // 是否展示列选择框
        showExportBtns: true, // 是否展示默认导出栏
        searchShow: false, // 是否展示搜索组件
        noDataText: this.uNo ? '表现良好，继续加油' : '请输入身份证号', // 数据为空时显示的提示内容，默认暂无数据
        pageData: {
          show: false // 是否展示分页
        },
        columns: [
          {
            title: '违法类型',
            key: 'illegalType',
            disabledSearch: true
          },
          {
            title: '违法人',
            key: 'peopleName'
          },
          {
            title: '身份证号',
            key: 'uno'
          },
          {
            title: '电话',
            key: 'phone',
            disabledSearch: true
          },
          {
            title: '监控节点',
            key: 'monitorName',
            disabledSearch: true
          },
          {
            title: '所属区域',
            key: 'area',
            disabledSearch: true
          },
          {
            title: '开始时间',
            key: 'beginDate',
            render (h, params) {
              let date = new Date(params.row.beginDate)
              return h(
                'p',
                date.getFullYear() + '-' +
                     (date.getMonth() + 1) + '-' +
                     date.getDate() + ' ' +
                     date.getHours() + ':' +
                     date.getMinutes()
              )
            },
            disabledSearch: true
          },
          {
            title: '结束时间',
            key: 'endDate',
            render (h, params) {
              let date = new Date(params.row.endDate)
              return h(
                'p',
                date.getFullYear() + '-' +
                     (date.getMonth() + 1) + '-' +
                     date.getDate() + ' ' +
                     date.getHours() + ':' +
                     date.getMinutes()
              )
            },
            disabledSearch: true
          },
          {
            title: '违法截图',
            key: 'evidence',
            render (h, params) {
              return h(
                'img',
                {
                  attrs: {
                    src: params.row.evidence,
                    style: 'width: 100px;height: 100px;'
                  },
                  class: 'imgStyle'
                }
              )
            },
            disabledSearch: true
          }
        ]
      },
      cascaderData: [],
      value1: ['', ''],
      year: '',
      uNo: '',
      pieLegend: [],
      pieData: [],
      tLegend: [],
      tData: [],
      lineLegend: [],
      lineData: [],
      map: {}, // 高德地图
      polygon: {} // 高德地图区域渲染
    }
  },
  mounted () {
    ajax.get({
      url: 'api/hifs/monitor/getAreaMonitor',
      param: {},
      success: res => {
        this.cascaderData = res.data
        this.init()
      }
    })
  },
  methods: {
    init (value) {
      this.pieInit(value)
      this.tableInit(value)
      this.tInit(value)
      // this.lineInit()
    },
    tableInit (value) {
      let param = new URLSearchParams()
      param.append('uNo', this.uNo ? this.uNo : 'noBody')
      param.append('monitorId', this.value1[1] || '')
      param.append('year', this.year)
      this.$refs.rcTable.refreshTable(param)
    },
    pieInit (value) {
      let monitorId = this.value1[1] || ''
      let uNo = this.uNo ? this.uNo : 'noBody'
      ajax.get({
        url: 'api/hifs/record/getPieRecords?monitorId=' + monitorId + '&year=' + this.year + '&uNo=' + uNo,
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
    tInit (value) {
      let param = new URLSearchParams()
      param.append('uNo', this.uNo ? this.uNo : 'noBody')
      param.append('monitorId', this.value1[1] || '')
      param.append('year', this.year)
      let that = this
      ajax.post({
        url: 'api/hifs/record/getTRecords',
        param: param,
        success: res => {
          let lArr = []
          let dArr = []
          let areaList = []
          for (let i in res.data.data) {
            lArr.push(res.data.data[i].area + '/' + res.data.data[i].monitor)
            dArr.push(res.data.data[i].totalType)
            let areas = res.data.data[i].area.split('/')
            let areaName = areas[areas.length - 1]
            areaList.push(areaName)
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

          that.loadmap(areaList)
        }
      })
    },
    lineInit () {
      document.getElementById('line').innerHtml = '<div id="lineChart" class="card-content"></div>'
      ajax.get({
        url: 'api/hifs/record/getLineRecords?year=' + this.year,
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
    },
    loadmap (data) { // 初始化加载地图相关组件
      if (!data) {
        this.map = new AMap.Map('slefContainer', { // 地图初始化
          zoom: 12
        })
        this.polygon = new AMap.Polygon({ // 行政区边界渲染，使用多边形覆盖物实现，初始化this.polygon状态
          map: this.map,
          strokeWeight: 1,
          fillOpacity: 0.7,
          fillColor: '#CCF3FF',
          strokeColor: '#CC66CC'
        })
      } else {
        this.map = new AMap.Map('slefContainer', { // 地图初始化
          zoom: 12
        })
        this.polygon = new AMap.Polygon({ // 行政区边界渲染，使用多边形覆盖物实现，初始化this.polygon状态
          map: this.map,
          strokeWeight: 1,
          fillOpacity: 0.7,
          fillColor: '#CCF3FF',
          strokeColor: '#CC66CC'
        })
        this.polygon.setMap(null) //  清空地图渲染，方便重新绘制地图
        for (let i in data) {
          this.serchMap(data[i])
        }
      }
    },
    serchMap (name) {
      let districtSearch
      let that = this
      AMap.service('AMap.DistrictSearch', function () { // 回调函数
        // 实例化DistrictSearch
        districtSearch = new AMap.DistrictSearch({
          extensions: 'all', // 设为all,才能得到渲染地图所需要的bouldes
          level: 'country',
          subdistrict: 1
        })
        // TODO:  使用districtSearch对象调用行政区查询的功能
        districtSearch.setSubdistrict(1)
        // TODO:  使用districtSearch对象调用行政区查询的功能
        // 调用查询方法
        districtSearch.search(name, function (status, result) {
          // TODO :  按照自己需求处理查询结果
          that.polygonMap(result.districtList[0]) // 根据坐标数组渲染区域
        })
      })
    },
    polygonMap (result) { // 渲染地图
      this.polygon = new AMap.Polygon({ //  行政区边界渲染，使用多边形覆盖物实现
        map: this.map,
        strokeWeight: 1,
        fillOpacity: 0.7,
        fillColor: '#CCF3FF',
        strokeColor: '#CC66CC'
      })
      this.polygon.setPath(result.boundaries) //  渲染地图通过查询地区所得到的数组
      this.map.setFitView() //  地图自定义
      this.map.setCenter(result.center) //  设置地图中心区域
    }
  }
}
</script>

<style lang="less" scoped>

.imgStyle{
  width: 40px;
  height: 40px;
}

.line-card{
  width: 100%;
  min-height: 450px;
  margin: 20px 0px;
  display: flex;
  .card-contaner{
    width:100%;
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

.map-container{
  width: 100%;
  min-height: 100%;
}
</style>
