<template>
  <div class="sidbar">
    <div class="iconImg">
      <img class="img"
           src="@/assets/img/logo.png" />
    </div>
    <div class="sidebarContent">
      <Menu theme="dark">
        <Submenu v-for="item in data"
                 v-if="item.children.length>0"
                 :name="item.id"
                 :key="item.id">
          <template slot="title">
            <Icon :type="item.iconName"></Icon>
            {{item.name}}
          </template>
          <router-link v-if="item.name !== '统计分析'" v-for="child in item.children" :to="child.addres" :key="child.id">
            <Menu-item :name="child.id"
                       :key="child.id">
              <Icon :type="child.iconName"></Icon>
              {{child.name}}</Menu-item>
          </router-link>
          <Menu-group v-if="isTong(item, false)" title="高发">
            <router-link v-for="tong1 in tongData1" :to="tong1.addres" :key="tong1.id">
              <Menu-item :name="tong1.id"
                         :key="tong1.id">
                <Icon :type="tong1.iconName"></Icon>
                {{tong1.name}}</Menu-item>
            </router-link>
          </Menu-group>
          <Menu-group v-if="isTong(item, true)" title="个人">
            <router-link v-for="tong2 in tongData2" :to="tong2.addres" :key="tong2.id">
              <Menu-item :name="tong2.id"
                         :key="tong2.id">
                <Icon :type="tong2.iconName"></Icon>
                {{tong2.name}}</Menu-item>
            </router-link>
          </Menu-group>
        </Submenu>
      </Menu>
    </div>
  </div>
</template>

<script>
import ajax from '@/util/ajax.js'
export default {
  name: 'Sidbar',
  data: () => {
    return {
      data: [],
      tongData1: [],
      tongData2: []
    }
  },
  created () {
    let that = this
    let userId = localStorage.getItem('user')
    ajax.get({
      url: 'api/hifs/module/getModulesTreeByuserId?userId=' + userId,
      success: function (res) {
        that.data = res.data
        let treeData = res.data
        for (let i in treeData) {
          if (treeData[i].name === '统计分析') {
            that.getTong(treeData[i])
          }
        }
      }
    })
  },
  methods: {
    isTong (data, self) {
      let result = false
      let children = data.children
      if (data.name === '统计分析' && children.length > 0) {
        if (self && this.tongData2.length > 0) {
          result = true
        }

        if (!self && this.tongData1.length > 0) {
          result = true
        }
      }

      return result
    },
    getTong (data) {
      let children = data.children
      if (data.name === '统计分析' && children.length > 0) {
        for (let i in children) {
          if (children[i].name === '基础路段') {
            this.tongData1.push(children[i])
          }
          if (children[i].name === '严重路段分析') {
            this.tongData1.push(children[i])
          }
          if (children[i].name === '个人违法分析') {
            this.tongData2.push(children[i])
          }
        }
      }
    }
  }
}
</script>

<style>
.sidbar {
    padding: 30px 0px;
}

.iconImg {
    width: 100%;
    height: 100px;
    background-size: 100% 100%;
    text-align: center;
    margin: 30px 0px;
}

.img {
    width: 110px;
    height: 110px;
    border-radius: 50%;
}
</style>
