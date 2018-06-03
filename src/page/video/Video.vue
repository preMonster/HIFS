<template>
    <div>
        <ModuleTitle :iconName="'paper-airplane'" :moduleName="'视频回放'"></ModuleTitle>
        <Card class='module-card video-card'>
          <div class="toolbar">
            <Select @change="chooseVideo('0')" class="toolbar-formItem" v-model="monitorId">
                  <Option value="0">监控节点1</Option>
                  <Option value="1">监控节点2</Option>
                  <Option value="2">监控节点3</Option>
            </Select>
            <DatePicker @on-change="chooseVideo('0')" type="datetimerange"  v-model="date" format="yyyy-MM-dd HH" placeholder="选择时间" class="toolbar-formItem"></DatePicker>
          </div>
          <div class="cardContainer">
            <video id="myvideo"
                  :src="videos[monitorId][videoId].videoSrc"
                  :poster="videos[monitorId][videoId].videoImg"
                  :muted="muteStatus"
                  controls="controls"
                  width="70%"
                  height="500">
                your browser does not support the video tag
            </video>
            <div class="videoCard">
              <div v-for="item in videos[monitorId]" class="videoContainer" @click="chooseVideo(item.id)" :key="item.id">
                <img class="videoImg" :title="getTitle" :src="item.videoImg"/>
              </div>
            </div>
          </div>
        </Card>
    </div>
</template>

<script>
import ModuleTitle from '@/components/titleMain/ModuleTitle'

export default {
  name: 'video',
  data: function () {
    return {
      videoId: '0',
      playStatus: '',
      muteStatus: '',
      isMute: true,
      isPlay: false,
      monitorId: '0',
      date: [],
      videos: [
        [{
          id: '0',
          videoSrc: '/static/video/v.mp4',
          videoImg: 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1527172775355&di=88ddebf0c28c04e9692417282d3972f4&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dshijue1%252C0%252C0%252C294%252C40%2Fsign%3Dda264db124dda3cc1fe9b06369805374%2F7dd98d1001e93901b02f591071ec54e736d196eb.jpg'
        }, {
          id: '1',
          videoSrc: '/static/video/v.mp4',
          videoImg: 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1527172912845&di=05aaa030f57f36a990c251399daaa706&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dshijue1%252C0%252C0%252C294%252C40%2Fsign%3D3780bbc38bcb39dbd5cd6f15b87f6351%2Fb7fd5266d0160924415c7d32de0735fae6cd349a.jpg'
        }, {
          id: '2',
          videoSrc: '/static/video/v.mp4',
          videoImg: 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1527172876270&di=456f8412102c9dde193e8eadb8c92d35&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dshijue1%252C0%252C0%252C294%252C40%2Fsign%3D377eba3f9ddda144ce0464f1dadebad7%2Fac345982b2b7d0a2375d1ab1c1ef76094b369a01.jpg'
        }], [{
          id: '0',
          videoSrc: '/static/video/v.mp4',
          videoImg: 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1527172912845&di=05aaa030f57f36a990c251399daaa706&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dshijue1%252C0%252C0%252C294%252C40%2Fsign%3D3780bbc38bcb39dbd5cd6f15b87f6351%2Fb7fd5266d0160924415c7d32de0735fae6cd349a.jpg'
        }, {
          id: '1',
          videoSrc: '/static/video/v.mp4',
          videoImg: 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1527172876270&di=456f8412102c9dde193e8eadb8c92d35&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dshijue1%252C0%252C0%252C294%252C40%2Fsign%3D377eba3f9ddda144ce0464f1dadebad7%2Fac345982b2b7d0a2375d1ab1c1ef76094b369a01.jpg'
        }, {
          id: '2',
          videoSrc: '/static/video/v.mp4',
          videoImg: 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1527172775355&di=88ddebf0c28c04e9692417282d3972f4&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dshijue1%252C0%252C0%252C294%252C40%2Fsign%3Dda264db124dda3cc1fe9b06369805374%2F7dd98d1001e93901b02f591071ec54e736d196eb.jpg'
        }], [{
          id: '0',
          videoSrc: '/static/video/v.mp4',
          videoImg: 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1527172876270&di=456f8412102c9dde193e8eadb8c92d35&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dshijue1%252C0%252C0%252C294%252C40%2Fsign%3D377eba3f9ddda144ce0464f1dadebad7%2Fac345982b2b7d0a2375d1ab1c1ef76094b369a01.jpg'
        }, {
          id: '1',
          videoSrc: '/static/video/v.mp4',
          videoImg: 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1527172912845&di=05aaa030f57f36a990c251399daaa706&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dshijue1%252C0%252C0%252C294%252C40%2Fsign%3D3780bbc38bcb39dbd5cd6f15b87f6351%2Fb7fd5266d0160924415c7d32de0735fae6cd349a.jpg'
        }, {
          id: '2',
          videoSrc: '/static/video/v.mp4',
          videoImg: 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1527172912845&di=05aaa030f57f36a990c251399daaa706&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dshijue1%252C0%252C0%252C294%252C40%2Fsign%3D3780bbc38bcb39dbd5cd6f15b87f6351%2Fb7fd5266d0160924415c7d32de0735fae6cd349a.jpg'
        }]
      ]
    }
  },
  computed: {
    getTitle: function () {
      let date = this.date[0] ? this.date[0] + '——' + this.date[1] : ''
      return '监控节点' + this.monitorId + date
    }
  },
  components: {
    ModuleTitle
  },
  methods: {
    playClick () {
      this.isPlay = !this.isPlay
      this.playStatus = 'autoplay'
    },
    closeSoundClick () {
      this.isMute = !this.isMute
      if (this.isMute) {
        this.muteStatus = ''
      } else {
        this.muteStatus = 'muted'
      }
    },
    chooseVideo (id) {
      this.videoId = id
    }
  }
}
</script>

<style lang="less">
  .video-card .ivu-card-body{
    padding: 20px 60px;
    background-color: #4b4d58;
    .toolbar{
      margin: 15px 20px;
      .toolbar-formItem{
        width: 300px;
      }
    }
    .cardContainer{
      display: flex;
      justify-content: space-between;
      #myvideo{
        margin-left: 20px;
        background-color: white;
      }
    }
  }

  .videoCard{
    width: 250px;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
  }

  .videoContainer{
    width: 100%;
    color: white;
  }

  .videoImg{
    width:100%;
    height: 150px;
  }
</style>
