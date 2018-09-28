<template>
    <div>
        <el-row :gutter="20">
            <el-col :span="24">
                <el-row :gutter="20" class="mgb20">
                    <el-col :span="6">
                        <el-card shadow="hover" :body-style="{padding: '0px'}">
                            <div class="grid-content grid-con-error">
                                <i class="el-icon-lx-warn grid-con-icon"></i>
                                <div class="grid-cont-right">
                                    <div class="grid-num">{{statData.error}}</div>
                                    <div>异常量</div>
                                </div>
                            </div>
                        </el-card>
                    </el-col>
                    <el-col :span="6">
                        <el-card shadow="hover" :body-style="{padding: '0px'}">
                            <div class="grid-content grid-con-inst">
                                <i class="el-icon-lx-apps grid-con-icon"></i>
                                <div class="grid-cont-right">
                                    <div class="grid-num">{{statData.inst}}</div>
                                    <div>实例数</div>
                                </div>
                            </div>
                        </el-card>
                    </el-col>
                    <el-col :span="6">
                        <el-card shadow="hover" :body-style="{padding: '0px'}">
                            <div class="grid-content grid-con-visit">
                                <i class="el-icon-lx-people grid-con-icon"></i>
                                <div class="grid-cont-right">
                                    <div class="grid-num">{{statData.req}}</div>
                                    <div>请求量</div>
                                </div>
                            </div>
                        </el-card>
                    </el-col>
                    <el-col :span="6">
                        <el-card shadow="hover" :body-style="{padding: '0px'}">
                            <div class="grid-content grid-con-log">
                                <i class="el-icon-lx-rank grid-con-icon"></i>
                                <div class="grid-cont-right">
                                    <div class="grid-num">{{statData.log}}</div>
                                    <div>日志量</div>
                                </div>
                            </div>
                        </el-card>
                    </el-col>
                </el-row>
            </el-col>
        </el-row>
        <el-row :gutter="20" class="mgb20">
            <el-col :span="9">
                <el-card>
                    <ve-pie :data="errorPieData" :title="errorPieTitle" :settings="errorPieSettings"></ve-pie>
                </el-card>
            </el-col>
            <el-col :span="15">
                <el-card>
                    <ve-line :data="errorLineData" :title="errorLineTitle" :extend="chartLineExtend" :settings="errorLineSettings"></ve-line>
                </el-card>
            </el-col>
        </el-row>
        <el-row :gutter="20">
            <el-col :span="9">
                <el-card shadow="hover">
                    <ve-bar :data="requestBarData" :legend-visible='false' :title="requestBarTitle" :settings="requestBarSettings"></ve-bar>
                </el-card>
            </el-col>
            <el-col :span="15">
                <el-card shadow="hover">
                    <ve-line :data="requestLineData" :title="requestLineTitle" :extend="requestLineExtend" :settings="requestLineSettings"></ve-line>
                </el-card>
            </el-col>
        </el-row>
    </div>
</template>
<style src="../css/dashboard.css"></style>
<script>
    import bus from '../common/bus';

    export default {
        name: 'dashboard',
        data() {
            this.chartLineExtend = {
                'xAxis.0.axisLabel.rotate': 60,
                'xAxis.0.boundaryGap':false
            }
            this.errorPieSettings = {
                limitShowNum: 8
            }
            this.errorPieTitle = {
                text:"异常占比",
                bottom:"10",
                left: 'middle'
            }
            this.errorLineSettings = {
                area: true
            }
            this.errorLineTitle = {
                text:"异常趋势图",
                bottom:"10",
                left: 'middle'
            }
            this.requestBarSettings = {
            }
            this.requestBarTitle = {
                text:'请求耗时区间统计',
                bottom:"10",
                left: 'middle'
            }
            this.requestLineSettings = {
            }
            this.requestLineExtend = {
                series: {
                    smooth: false
                },
                'xAxis.0.axisLabel.rotate': 60,
                'xAxis.0.boundaryGap':false
            },
            this.requestLineTitle = {
                text:"请求量趋势图（耗时区间）",
                bottom:"10",
                left: 'middle'
            }
            return {
                pickerDate:[],
                name: localStorage.getItem('ms_username'),
                statData:{
                    req: 0,
                    log: 0,
                    inst: 0,
                    error: 0
                },
                errorPieData: {
                    title:'异常占比',
                    columns: ['name', 'value'],
                    rows: []
                },
                errorLineData: {
                    columns: [],
                    rows: []
                },
                requestBarData:{
                    columns: ['区间', '请求数量'],
                    rows: [
                        { '区间': '3000', '请求数量': 1393},
                        { '区间': '1500', '请求数量': 1393},
                        { '区间': '1000', '请求数量': 3530},
                        { '区间': '800', '请求数量': 2923},
                        { '区间': '500', '请求数量': 1723 },
                        { '区间': '300', '请求数量': 3792},
                        { '区间': '200', '请求数量': 4593 }
                    ]
                },
                requestLineData: {
                    columns: ['time', '0~200',"200~500","500~1000","1000~2000","2000~3000","3000~*"],
                    rows: [
                        { 'time': '1月1日', '0~200': 123 ,"200~500":183,"500~1000":583,"1000~2000":783,"2000~3000":383,"3000~*":203},
                        { 'time': '1月2日', '0~200': 89 ,"200~500":200,"500~1000":300,"1000~2000":600,"2000~3000":500,"3000~*":103},
                        { 'time': '1月3日', '0~200': 200 ,"200~500":383,"500~1000":183,"1000~2000":900,"2000~3000":300,"3000~*":183},
                        { 'time': '1月4日', '0~200': 123 ,"200~500":345,"500~1000":583,"1000~2000":383,"2000~3000":283,"3000~*":233},
                        { 'time': '1月5日', '0~200': 134 ,"200~500":183,"500~1000":555,"1000~2000":183,"2000~3000":513,"3000~*":333},
                        { 'time': '1月6日', '0~200': 59 ,"200~500":422,"500~1000":432,"1000~2000":742,"2000~3000":183,"3000~*":643}
                    ]
                }

            }
        },
        components: {
        },
        computed: {
            role() {
                return this.name === 'admin' ? '超级管理员' : '普通用户';
            }
        },
        created(){
            this.getPickerDate();
            this.getStatData();
            this.getErrorPieData();
            this.getErrorLineData();
            this.handleListener();
            this.changeDate();

        },
        activated(){
            this.handleListener();
        },
        deactivated(){
            window.removeEventListener('resize', this.renderChart);
            bus.$off('collapse', this.handleBus);
        },
        methods: {
            changeDate(){
                const now = new Date().getTime();
                this.chartData.forEach((item, index) => {
                    const date = new Date(now - (6 - index) * 86400000);
                    item.name = `${date.getFullYear()}/${date.getMonth()+1}/${date.getDate()}`
                })
            },
            handleListener(){
                bus.$on('collapse', this.handleBus);
                // 调用renderChart方法对图表进行重新渲染
                window.addEventListener('resize', this.renderChart)
            },
            handleBus(msg){
                setTimeout(() => {
                    this.renderChart()
                }, 300);
            },
            renderChart(){
                this.$refs.bar.renderChart();
                this.$refs.line.renderChart();
            },
            getStatData(){
                const url = "/api/dashboard/stat";
                this.$axios.post(url, {
                    dateTime: "2018-09-24 17:12"
                }).then((res) => {
                    console.log(res.data);
                    this.statData = res.data;
                })
            },
            getErrorPieData(){
                const url = "/api/dashboard/getErrorPieData";
                this.$axios.post(url, {
                    beginTime: "2018-09-24 17:12",
                    endTime:"2018-09-24 17:13"
                }).then((res) => {
                    console.log(res.data);
                    this.errorPieData.rows = res.data.list;
                })
            },
            getErrorLineData(){
                const url = "/api/dashboard/getErrorLineData";
                this.$axios.post(url, {
                    beginTime: "2018-09-24 17:12",
                    endTime:"2018-09-24 17:13"
                }).then((res) => {
                    console.log(res.data);
                    this.errorLineData.rows = res.data.rows;
                    this.errorLineData.columns = res.data.columns;
                })
            },
            getPickerDate(){
                console.log("====>sendGetPickerDateEvent");
                const self = this;
                bus.$on("pickerDateEvent",function (val) {
                    self.pickerDate = val;
                    console.log("====>rec:%o",self.pickerDate);
                });
                bus.$emit("getPickerDateEvent");
            }
        }
    }
</script>
