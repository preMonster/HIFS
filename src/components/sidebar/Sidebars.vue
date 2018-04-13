<template>
  <div class="sidbar">
    <div class="iconImg">
        <img class="img" src="@/assets/img/logo.png"/>
    </div>
    <div class="sidebarContent">
        <Menu theme="dark">
                <Submenu key="1">
                    <template slot="title">
                    <Icon type="gear-b"></Icon>
                        系统管理
                    </template>
                    <router-link :to="'/module'"><Menu-item key="1-1">
                    <Icon type="paper-airplane"></Icon>
                    模块管理</Menu-item></router-link>
                    <Menu-item key="1-2">
                    <Icon type="paper-airplane"></Icon>
                    角色管理</Menu-item>
                    <Menu-item key="1-3">
                    <Icon type="paper-airplane"></Icon>
                    角色分配</Menu-item>
                    <Menu-item key="1-4">
                    <Icon type="paper-airplane"></Icon>
                    用户管理</Menu-item>
                </Submenu>
                <Submenu key="2">
                    <template slot="title">
                        <Icon type="home"></Icon>
                        基础管理
                    </template>
                    <Menu-item key="2-1">
                    <Icon type="paper-airplane"></Icon>
                    区域管理</Menu-item>
                    <Menu-item key="2-2">
                    <Icon type="paper-airplane"></Icon>
                    车辆详情</Menu-item>
                    <Menu-item key="2-2">
                    <Icon type="paper-airplane"></Icon>
                    违法基础信息</Menu-item>
                </Submenu>
                <Submenu key="3">
                    <template slot="title">
                        <Icon type="ios-people"></Icon>
                        违法取证
                    </template>
                    <Menu-item key="3-1">
                    <Icon type="paper-airplane"></Icon>
                    违法记录</Menu-item>
                    <Menu-item key="3-2">
                    <Icon type="paper-airplane"></Icon>
                    违法历史记录</Menu-item>
                </Submenu>
                <Submenu key="4">
                    <template slot="title">
                        <Icon type="stats-bars"></Icon>
                        统计分析
                    </template>
                    <Menu-group title="高发">
                        <Menu-item key="4-1">
                    <Icon type="paper-airplane"></Icon>
                    基础路段</Menu-item>
                        <Menu-item key="4-2">
                    <Icon type="paper-airplane"></Icon>
                    严重路段分析</Menu-item>
                    </Menu-group>
                    <Menu-group title="个人">
                        <Menu-item key="4-3">
                    <Icon type="paper-airplane"></Icon>
                    个人违法分析</Menu-item>
                    </Menu-group>
                </Submenu>
                <Submenu key="5">
                    <template slot="title">
                    <Icon type="videocamera"></Icon>
                        违法视频管理
                    </template>
                    <Menu-item key="5-1">
                    <Icon type="paper-airplane"></Icon>
                    视频回放</Menu-item>
                </Submenu>
            </Menu>
    </div>
  </div>
</template>

<script>
export default{
  name: 'Sidbar'
}
</script>

<style>
.sidbar{
    padding:30px 0px;
}

.iconImg{
    width: 100%;
    height: 100px;
    background-size: 100% 100%;
    text-align: center;
    margin:30px 0px;
}

.img{
    width: 110px;
    height: 110px;
    border-radius: 50%;
}
</style>
