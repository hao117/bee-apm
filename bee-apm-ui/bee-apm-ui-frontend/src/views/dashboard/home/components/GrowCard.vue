<template>
  <div class="md:flex">
    <template v-for="(item, index) in growCardList" :key="item.title">
      <Card
        size="small"
        :loading="$attrs.loading"
        :title="item.title"
        class="md:w-1/4 w-full !md:mt-0 !mt-4"
        :class="[index + 1 < 4 && '!md:mr-4']"
        :canExpan="false"
      >
        <template #extra>
          <Tag :color="item.color">{{ item.action }}</Tag>
        </template>

        <div class="py-4 px-4 flex justify-between">
          <CountTo :startVal="1" :endVal="item.value" class="text-2xl"/>
          <Icon :icon="item.icon" :size="40" :color="item.color"/>
        </div>
      </Card>
    </template>
  </div>
</template>
<script lang="ts">
import {defineComponent, ref} from 'vue';

import {CountTo} from '/@/components/CountTo/index';
import {Icon} from '/@/components/Icon';
import {Tag, Card} from 'ant-design-vue';
import {SummaryResultModel} from "/@/api/dashboard/model/dashboardModel";
import {summaryApi} from "/@/api/dashboard/dashboard";


export default defineComponent({
  components: {CountTo, Tag, Card, Icon},
  setup() {
    let growCardList = ref<Array<SummaryResultModel>>([]);
    async function querySummary() {
      growCardList.value = await summaryApi();
      console.log(new Date() + " growCardList1 = " + JSON.stringify(growCardList));
    }
    querySummary();
    return {growCardList};
  }
});
</script>
