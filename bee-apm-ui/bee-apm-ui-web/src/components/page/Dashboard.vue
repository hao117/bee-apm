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
                    <ve-line :data="errorLineData" :title="errorLineTitle" :extend="chartExtend" :settings="errorLineSettings"></ve-line>
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
            this.chartExtend = {
                'xAxis.0.axisLabel.rotate': 60
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
                    columns: ['日期', '访问用户'],
                    rows: [
                        { '日期': 'crm-cust-dev-1', '访问用户': 1393 },
                        { '日期': 'crm-cust-dev-2', '访问用户': 3530 },
                        { '日期': 'crm-cust-dev-3', '访问用户': 2923 },
                        { '日期': 'crm-cust-dev-4', '访问用户': 1723 },
                        { '日期': 'crm-cust-dev-5', '访问用户': 3792 },
                        { '日期': 'crm-cust-dev-6', '访问用户': 4593 },
                        { '日期': 'crm-cust-dev-7', '访问用户': 493 },
                        { '日期': 'crm-cust-dev-8', '访问用户': 593 },
                        { '日期': 'crm-cust-dev-9', '访问用户': 1593 },
                        { '日期': 'crm-cust-dev-10', '访问用户': 3593 },
                        { '日期': 'crm-cust-dev-11', '访问用户': 993 },
                        { '日期': 'crm-cust-dev-12', '访问用户': 593 },

                    ]
                },
                errorLineData: {
                    columns: ['日期', 'app1', 'app2', 'app3'],
                    rows: [
                        { '日期': '1/1', 'app1': 1393, 'app2': 1093, 'app3': 123 },
                        { '日期': '1/2', 'app1': 3530, 'app2': 3230, 'app3': 3345 },
                        { '日期': '1/3', 'app1': 2923, 'app2': 2623, 'app3': 345 },
                        { '日期': '1/4', 'app1': 1723, 'app2': 1423, 'app3': 2344 },
                        { '日期': '1/5', 'app1': 3792, 'app2': 3492, 'app3': 1234 },
                        { '日期': '1/6', 'app1': 4593, 'app2': 4293, 'app3': 543 },
                        { '日期': '1/7', 'app1': 3792, 'app2': 3492, 'app3': 1234 },
                        { '日期': '1/8', 'app1': 3342, 'app2': 312, 'app3': 2234 },
                        { '日期': '1/9', 'app1': 3562, 'app2': 6492, 'app3': 1434 },
                        { '日期': '1/10', 'app1': 3592, 'app2': 3002, 'app3': 734 }


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
                }
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
            this.getStatData();
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
            }
        }
    }
</script>
