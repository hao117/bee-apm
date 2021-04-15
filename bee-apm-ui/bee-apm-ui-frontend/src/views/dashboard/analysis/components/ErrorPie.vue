<template>
  <Card title="异常占比" :loading="loading">
    <div ref="chartRef" :style="{ width, height }"></div>
  </Card>
</template>
<script lang="ts">
import {defineComponent, Ref, ref, watch} from 'vue';

import {Card} from 'ant-design-vue';
import {useECharts} from '/@/hooks/web/useECharts';
import {apiHttp} from "/@/utils/http/axios";
import {OptionDataItemObject} from "echarts/types/src/util/types";

export default defineComponent({
  components: {Card},
  props: {
    loading: Boolean,
    width: {
      type: String as PropType<string>,
      default: '100%',
    },
    height: {
      type: String as PropType<string>,
      default: '300px',
    },
  },
  setup(props) {
    const chartRef = ref<HTMLDivElement | null>(null);
    const {setOptions} = useECharts(chartRef as Ref<HTMLDivElement>);

    async function getPieData() {
      let data = await apiHttp.post<Array<OptionDataItemObject<number>>>({url: "/dashboard/getErrorPieData"});
      setOptions({
        tooltip: {
          trigger: 'item',
        },
        series: [
          {
            name: '错误占比',
            type: 'pie',
            radius: '80%',
            center: ['50%', '50%'],
            data: data,
            roseType: 'radius',
            animationType: 'scale',
            animationEasing: 'exponentialInOut',
            animationDelay: function () {
              return Math.random() * 400;
            },
          },
        ],
      });
    };
    watch(
      () => props.loading,
      () => {
        if (props.loading) {
          return;
        }
        getPieData();
      },
      {immediate: true}
    );
    return {chartRef};
  },
});
</script>
