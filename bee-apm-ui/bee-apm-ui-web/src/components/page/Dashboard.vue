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
                                    <div>访问量</div>
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
            <el-col :span="8">
                <el-card>
                    <ve-pie :data="errorPieData" :title="errorPieTitle" :settings="errorPieSettings"></ve-pie>
                </el-card>
            </el-col>
            <el-col :span="16">
                <el-card>
                    <ve-line :data="errorLineData" :title="errorLineTitle" :extend="chartLineExtend" :settings="errorLineSettings"></ve-line>
                </el-card>
            </el-col>
        </el-row>
        <el-row :gutter="20">
            <el-col :span="12">
                <el-card shadow="hover">
                    <schart ref="bar" class="schart" canvasId="bar" :data="chartData" type="bar" :options="options"></schart>
                </el-card>
            </el-col>
            <el-col :span="12">
                <el-card shadow="hover">
                    <schart ref="line" class="schart" canvasId="line" :data="chartData" type="line" :options="options2"></schart>
                </el-card>
            </el-col>
        </el-row>
    </div>
</template>
<style src="../css/dashboard.css"></style>
<script>
    import Schart from 'vue-schart';
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
            return {
                errorTitle:"ttttt",
                name: localStorage.getItem('ms_username'),
                chartData:[
                    {
                        name: '2018/09/04',
                        value: 1083
                    },
                    {
                        name: '2018/09/05',
                        value: 941
                    },
                    {
                        name: '2018/09/06',
                        value: 1139
                    },
                    {
                        name: '2018/09/07',
                        value: 816
                    },
                    {
                        name: '2018/09/08',
                        value: 1500
                    },
                    {
                        name: '2018/09/09',
                        value: 328
                    },
                    {
                        name: '2018/09/10',
                        value: 1065
                    }
                ],
                statData:{
                    req: 0,
                    log: 0,
                    inst: 0,
                    error: 0
                },
                errorPieData: {
                    title:'异常占比',
                    columns: ['name', 'value'],
                    rows: [
                        { 'name': 'crm-cust-dev-1', 'value': 1393 },
                        { 'name': 'crm-cust-dev-2', 'value': 3530 },
                        { 'name': 'crm-cust-dev-3', 'value': 2923 },
                        { 'name': 'crm-cust-dev-4', 'value': 1723 },
                        { 'name': 'crm-cust-dev-5', 'value': 3792 },
                        { 'name': 'crm-cust-dev-6', 'value': 4593 },
                        { 'name': 'crm-cust-dev-7', 'value': 493 },
                        { 'name': 'crm-cust-dev-8', 'value': 593 },
                        { 'name': 'crm-cust-dev-9', 'value': 1593 },
                        { 'name': 'crm-cust-dev-10', 'value': 3593 },
                        { 'name': 'crm-cust-dev-11', 'value': 1993 },
                        { 'name': 'crm-cust-dev-12', 'value': 593 },

                    ]
                },
                errorLineData: {
                    columns: ['time', 'app1', 'app2', 'app3'],
                    rows: [
                        { 'time': '1/1', 'app1': 1393, 'app2': 1093, 'app3': 123 },
                        { 'time': '1/2', 'app1': 3530, 'app2': 3230, 'app3': 3345 },
                        { 'time': '1/3', 'app1': 2923, 'app2': 2623, 'app3': 345 },
                        { 'time': '1/4', 'app1': 1723, 'app2': 1423, 'app3': 2344 },
                        { 'time': '1/5', 'app1': 3792, 'app2': 3492, 'app3': 1234 },
                        { 'time': '1/6', 'app1': 4593, 'app2': 4293, 'app3': 543 },
                        { 'time': '1/7', 'app1': 3792, 'app2': 3492, 'app3': 1234 },
                        { 'time': '1/8', 'app1': 3342, 'app2': 312, 'app3': 2234 },
                        { 'time': '1/9', 'app1': 3562, 'app2': 6492, 'app3': 1434 },
                        { 'time': '1/10', 'app1': 3592, 'app2': 3002, 'app3': 734 }


                    ]
                },
                options: {
                    title: '最近七天每天的用户访问量',
                    showValue: false,
                    fillColor: 'rgb(45, 140, 240)',
                    bottomPadding: 30,
                    topPadding: 30
                },
                options2: {
                    title: '最近七天用户访问趋势',
                    fillColor: '#FC6FA1',
                    axisColor: '#008ACD',
                    contentColor: '#EEEEEE',
                    bgColor: '#F5F8FD',
                    bottomPadding: 30,
                    topPadding: 30
                },
                pickerDate:[]
            }
        },
        components: {
            Schart
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
