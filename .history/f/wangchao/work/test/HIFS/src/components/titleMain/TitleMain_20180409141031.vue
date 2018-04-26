<template>
  <div class="title">
    <div class="sysName">
      <span>高速路违法取证系统</span>
    </div>
    <div class="flex-right">
      <div class="time">
        {{getNowDate}}
      </div>
      <div @click="changeUserInfoState" class="userInfo">
        <div class="infoContent">
          <div class="headImg">
            <img class="img" :src="'/static/image/'+userInfo.headImg"/>
          </div>
          <div class="userName">{{userInfo.realName}}</div>
        </div>
        <Icon :type="userInfoState?'arrow-up-b':'arrow-down-b'" class="icon"></Icon>
      </div>
    </div>
    <transition name="slide-fade">
      <div v-if="userInfoState" class="userInfoContentDiv">
        <form @submit.prevent>
          <div class="userInfoContentImgDiv">
            <input ref="imgFile" class="headimg-fileinput" type="file"/>
            <img @click="chooseFile" class="imgLarge" :src="'/static/image/'+userInfo.headImg"/>
          </div>
          <div class="user-info-name mar-top-bot10">{{userInfo.realName}} (<span>  {{userInfo.roleName}} </span>)</div>

          <SpecialForm :state="editState" :data="userInfo" :changeData="changeUserInfo"></SpecialForm>

          <div class="btns">
            <i-button type="primary" :loading="loading" @click="edit">
              <span v-if="!loading" >{{editState==0?'点击修改':'确认修改'}}</span>
              <span v-else>Loading...</span>
            </i-button>
            <i-button @click="outLogin" type="error">退出登录</i-button>
          </div>
        </form>
      </div>
    </transition>
  </div>
</template>

<script>
import SpecialForm from '@/components/specialForm/SpecialForm'
import ajax from '@/util/ajax'

export default{
  name: 'TitleMain',
  data: function () {
    return {
      time: new Date(),
      userInfo: {},
      userInfoState: false,
      editState: 0, // 0：点击修改，1：确认修改
      loading: false
    }
  },
  components: {
    SpecialForm
  },
  mounted: function () {
    let that = this
    setInterval(function () {
      that.time = new Date()
    }, 1000)

    ajax.get({
      url: 'static/test.json',
      param: {
        id: localStorage.getItem('user')
      },
      success: res => {
        that.userInfo = res.data
      }
    })
  },
  computed: {
    getNowDate () {
      return this.time.toLocaleString() + ' ' +
             this.getWeek()
    }
  },
  methods: {
    getWeek () {
      let week = [
        '星期天',
        '星期一',
        '星期二',
        '星期三',
        '星期四',
        '星期五',
        '星期六'
      ]
      return week[this.time.getDay()]
    },
    changeUserInfoState (isOpen) {
      this.userInfoState = !this.userInfoState
    },
    chooseFile () {
      this.$refs.imgFile.click()
    },
    changeUserInfo (data) {
      this.userInfo = data
    },
    edit () {
      this.editState = this.editState === 0 ? 1 : 0
      this.loading = true
      let that = this
      setTimeout(function () {
        that.loading = false
      }, 1000)
    },
    outLogin () {
      localStorage.removeItem('user')
      this.$router.push({ path: 'login' })
    }
  }
}
</script>

<style>
.title{
  height: 100%;
  display: flex;
  color: white;
  justify-content: space-between;
  padding: 0px 20px;
  align-items: center;
  position: relative;
}

.sysName{
  font-size: 26px;
  font-weight: 1000;
}

.flex-right{
  height: 100%;
  display: flex;
  align-items: center;
}

.time{
  font-size: 16px;
}

.userInfo{
  width: 100px;
  height: 100%;
  text-align: center;
}

.infoContent{
  height: 70%;
  padding-top:20px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.headImg{
  width:25px;
  height:25px;
  margin-right:5px;
}

.headImg > .img{
  width:25px;
  height:25px;
  border-radius:50%;
}

.userName{
  font-size: 14px;
}

.icon{
  height:20%;
}

.userInfoContentDiv{
  position: absolute;
  z-index: 999;
  width: 300px;
  padding: 20px 10px;
  right: 0px;
  top: 70px;
  border: 1px green solid;
  box-shadow: 0 1px 6px rgba(0,0,0,.2);
  text-align: center;
  color: #333333;
  background-color: white;
}

/* 可以设置不同的进入和离开动画 */
/* 设置持续时间和动画函数 */
.slide-fade-enter-active {
  transition: all .1s ease;
}
.slide-fade-leave-active {
  transition: all .1s cubic-bezier(1.0, 0.5, 0.8, 1.0);
}
.slide-fade-enter, .slide-fade-leave-to
/* .slide-fade-leave-active for below version 2.1.8 */ {
  transform: translateY(-10px);
  opacity: 0;
}

.userInfoContentImgDiv{
  width:60px;
  height:60px;
  margin:0px auto;
  position:relative;
}

.headimg-fileinput{
  position:absolute;
  z-index: -1;
  width:0px;
  height:0px;
}

.imgLarge{
  width:60px;
  height:60px;
  border-radius:50%;
}

.user-info-name{
  font-size: 20px;
  font-weight: 1000;
}

.user-info-name > span{
  font-size: 14px;
}

.mar-top-bot10{
  margin: 10px 0px;
}
</style>
