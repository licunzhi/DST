<template>
  <div>
    <template v-if="routerPath.key==='DataSourceTools'">
      <dst></dst>
    </template>
    <template v-else-if="routerPath.key==='zookeeper'">
      <zookeeper-wrapper></zookeeper-wrapper>
    </template>
    <template v-else-if="routerPath.key==='kafka'">
      <zookeeper-wrapper></zookeeper-wrapper>
    </template>
    <template v-else-if="routerPath.key==='es'">
      <zookeeper-wrapper></zookeeper-wrapper>
    </template>
  </div>
</template>

<script>
import ZookeeperWrapper from '@/components/zookeeper/ZookeeperWrapper'
import dst from '@/components/dst/dst'
import KafkaWrapper from '@/components/kafka/KafkaWrapper'
import EsWrapper from '@/components/es/EsWrapper'

export default {
  name: 'RouterTabView',
  props: ['routerPath'],
  data () {
    return {}
  },
  components: {
    ZookeeperWrapper,
    dst,
    KafkaWrapper,
    EsWrapper
  }
}
</script>

<style scoped>

</style>
