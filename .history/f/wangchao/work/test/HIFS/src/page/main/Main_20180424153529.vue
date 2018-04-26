<template>
  <div class="main">
    <div class="sidebarMain">
      <Sidebar></Sidebar>
    </div>
    <div class="contentMain">
      <div class="contentMainTitle">
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
  components: {
    Sidebar,
    TitleMain
  },
  mounted: function () {
    let user = localStorage.getItem('user')
    if (!user) {
      this.$router.push('login')
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
}

.contentMain {
    width: 100%;
}

.contentMainTitle {
    width: 100%;
    height: 70px;
    background-color: #394050;
}

.container {
    width: 100%;
}
</style>
