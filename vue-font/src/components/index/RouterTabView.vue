<template>
  <div>
    <template v-if="routerPath.key==='DataSourceTools'">
      <dst></dst>
    </template>
    <template v-else-if="routerPath.key==='zookeeper'">
      <zookeeper-wrapper></zookeeper-wrapper>
    </template>
  </div>
</template>

<script>
import ZookeeperWrapper from '@/components/zookeeper/ZookeeperWrapper'
import dst from '@/components/dst/dst'

export default {
  name: 'RouterTabView',
  props: ['routerPath'],
  data () {
    return {}
  },
  components: {
    ZookeeperWrapper,
    dst
  }
}
</script>

<style scoped>

</style>
