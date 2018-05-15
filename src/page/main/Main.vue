<template>
  <div class="main">
    <div v-show="showSidebar" class="sidebarMain">
      <Sidebar></Sidebar>
      <div class="icon-sidebar" @click="changeSidebarState()">
        <Icon type="reply"></Icon>
      </div>
    </div>
    <div class="contentMain">
      <div class="contentMainTitle">
        <div class="icon-sidebar1" @click="changeSidebarState()">
          <Icon v-show="!showSidebar" type="forward"></Icon>
        </div>
        <TitleMain></TitleMain>
      </div>
      <div class="container">
        <router-view/>
      </div>
    </div>
  </div>
</template>

<script>
import Sidebar from '@/components/sidebar/Sidebars'
import TitleMain from '@/components/titleMain/TitleMain'

export default {
  name: 'Main',
  data: function () {
    return {
      showSidebar: true
    }
  },
  components: {
    Sidebar,
    TitleMain
  },
  mounted: function () {
    let user = localStorage.getItem('user')
    if (!user) {
      this.$router.push('login')
    }
  },
  methods: {
    changeSidebarState () {
      this.showSidebar = !this.showSidebar
    }
  }
}
</script>

<style>
.main {
    min-height: 100%;
    color: #2c3e50;
    display: flex;
    background: #f5f6f7;
}

.module-card {
    width: 95%;
    margin: 20px auto;
    min-height: 500px;
}

.sidebarMain {
    width: 250px;
    min-height: 100%;
    background-color: #484d58;
    position: relative;
}

.icon-sidebar{
  font-size:18px;
  color: #ccc;
  position: absolute;
  right:5px;
  top:5px;
}

.icon-sidebar1{
  font-size:18px;
  color: #ccc;
  position: absolute;
  left:5px;
  top:5px;
  z-index: 999;
}

.contentMain {
    width: 100%;
}

.contentMainTitle {
    width: 100%;
    height: 70px;
    background-color: #394050;
    position: relative;
}

.container {
    width: 100%;
}
</style>
