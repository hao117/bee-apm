<template>
  <Card title="异常趋势图" :loading="loading">
    <div ref="chartRef" :style="{ height, width }"></div>
  </Card>
</template>
<script lang="ts">
import {Card} from 'ant-design-vue';
import {LineSeriesOption} from 'echarts/types/dist/echarts';
import {defineComponent, ref, Ref, watch} from 'vue';
import {useECharts} from '/@/hooks/web/useECharts';
import {apiHttp} from "/@/utils/http/axios";
import {OptionDataValue} from "echarts/types/src/util/types";

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

    async function getData() {
      let dataMap = await apiHttp.post<Map<string, Array<OptionDataValue>>>({url: "/dashboard/getErrorLineData"});
      console.log(JSON.stringify(dataMap));
      let series: Array<LineSeriesOption> = new Array<LineSeriesOption>();
      let legendData: Array<string> = new Array<string>();
      for (let key in dataMap) {
        legendData.push(key);
        series.push({
          name: key,
          data: dataMap[key],
          type: 'line'
        })
      }
      ;
      setOptions({
        tooltip: {
          trigger: 'axis',
        },
        legend: {
          data: legendData,
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: [
            '00:00',
            '01:00',
            '02:00',
            '03:00',
            '04:00',
            '05:00',
            '06:00',
            '07:00',
            '08:00',
            '09:00',
            '10:00',
            '11:00',
            '12:00',
            '13:00',
            '14:00',
            '15:00',
            '16:00',
            '17:00',
            '18:00',
            '19:00',
            '20:00',
            '21:00',
            '22:00',
            '23:00',
          ]
        },
        yAxis: {
          type: 'value',
        },
        grid: {left: '1%', right: '1%', top: '2  %', bottom: 0, containLabel: true},
        series: series,
      });
    };
    watch(
      () => props.loading,
      () => {
        if (props.loading) {
          return;
        }
        getData();
      },
      {immediate: true}
    );
    return {chartRef};
  },
});
</script>
