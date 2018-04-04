<template>
    <div>
        <icon :type='iconName'></icon>
        <IconsChoose :getIcon="getIconName"></IconsChoose>
    </div>
</template>

<script>
import IconsChoose from '@/components/iconsChoose/IconsChoose'

export default{
  name: 'module',
  data: function () {
    return {
      iconName: ''
    }
  },
  components: {
    IconsChoose
  },
  methods: {
    getIconName: function (iconName) {
      this.iconName = iconName
    }
  }
}
</script>
