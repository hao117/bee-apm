<template>
  <Card title="请求耗时区间统计" :loading="loading">
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

    async function queryData() {
      const data = await apiHttp.post<Array<OptionDataItemObject<number>>>({url: "/dashboard/getRequestBarData"});
      const yData = new Array<string>()
      for (const d of data) {
        yData.push(d.name as string);
      }
      setOptions({
        xAxis: {
          max: 'dataMax',
        },
        yAxis: {
          type: 'category',
          data: yData,
          inverse: true,
          animationDuration: 300,
          animationDurationUpdate: 300
        },
        series: [{
          type: 'bar',
          data: data,
          label: {
            show: true,
            position: 'right',
            valueAnimation: true
          }
        }],
        legend: {
          show: true
        },
        grid: {left: '1%', right: '5%', top: '2%', bottom: 0, containLabel: true},
        animationDuration: 0,
        animationDurationUpdate: 3000,
        animationEasing: 'linear',
        animationEasingUpdate: 'linear'
      });
    };
    watch(
      () => props.loading,
      () => {
        if (props.loading) {
          return;
        }
        queryData();
      },
      {immediate: true}
    );
    return {chartRef};
  },
});
</script>
