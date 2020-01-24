<template>
    <div>
        <el-row :gutter="20">
            <el-col :span="24">
                <el-row :gutter="20" class="mgb20">
                    <el-col :span="6">
                        <el-card shadow="hover" :body-style="{padding: '0px'}">
                            <div class="grid-content grid-con-error">
                                <i class="el-icon-lx-notice grid-con-icon"></i>
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
                                    <div>采集量</div>
                                </div>
                            </div>
                        </el-card>
                    </el-col>
                </el-row>
            </el-col>
        </el-row>
        <el-row :gutter="20" class="mgb20">
            <el-col :span="9">
                <el-card >
                    <ve-pie :data="errorPieData" :theme="light" :title="errorPieTitle" :extend="errorPieExtend"  :settings="errorPieSettings"></ve-pie>
                </el-card>
            </el-col>
            <el-col :span="15">
                <el-card>
                    <ve-line :data="errorLineData" :theme="light" :title="errorLineTitle" :extend="errorLineExtend" :settings="errorLineSettings"></ve-line>
                </el-card>
            </el-col>
        </el-row>
        <el-row :gutter="20">
            <el-col :span="9">
                <el-card shadow="hover">
                    <ve-bar :data="requestBarData" :theme="light" :legend-visible='false' :title="requestBarTitle" :extend="requestBarExtend" :settings="requestBarSettings"></ve-bar>
                </el-card>
            </el-col>
            <el-col :span="15">
                <el-card shadow="hover">
                    <ve-line :data="requestLineData" :theme="light" :title="requestLineTitle" :extend="requestLineExtend" :settings="requestLineSettings"></ve-line>
                </el-card>
            </el-col>
        </el-row>
    </div>
</template>
<style src="../css/dashboard.css"></style>
<script>
    import bus from '../common/bus';
    let moment = require("moment");
    const light = require('echarts/lib/theme/light');
    export default {
        name: 'dashboard',
        data() {
            return {
                light:light,
                pickerDate:[],
                activeTagTitle:'仪表盘',
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
                    rows: []
                },
                requestLineData: {
                    columns: ['time', '0-200',"200-500","500-1000","1000-2000","2000-5000","5000-*"],
                    rows: []
                },
                errorLineExtend: {
                    'xAxis.0.axisLabel.rotate': 60,
                    'xAxis.0.boundaryGap':false,
                    series(v) {
                        v.forEach(i => {
                            i.barMaxWidth = 30
                        })
                        return v
                    },
                    toolbox: {
                        y:15,
                        feature: {
                            mark : {show: true},
                            magicType : {show: true, type: ['line', 'bar','stack','tiled']}
                        }
                    },
                },
                errorPieSettings : {
                    limitShowNum: 8
                },
                errorPieExtend : {
                },
                errorPieTitle : {
                    text:"异常占比",
                    bottom:"10",
                    left: 'middle'
                },
                errorLineSettings : {
                    area: true
                },
                errorLineTitle : {
                    text:"异常趋势图",
                    bottom:"10",
                    left: 'middle'
                },
                requestBarSettings : {
                },
                requestBarExtend : {
                    series(v) {
                        v.forEach(i => {
                            i.barMaxWidth = 30
                        })
                        return v
                    },
                },
                requestBarTitle : {
                    text:'请求耗时区间统计',
                    bottom:"10",
                    left: 'middle'
                },
                requestLineSettings: {
                },
                requestLineExtend: {
                    series(v) {
                        v.forEach(i => {
                            i.barMaxWidth = 30
                        })
                        return v
                    },
                    'xAxis.0.axisLabel.rotate': 60,
                    'xAxis.0.boundaryGap':false,
                    toolbox: {
                        feature: {
                            mark : {show: true},
                            magicType : {show: true, type: ['line', 'bar','stack','tiled']}
                        }
                    }
                },
                requestLineTitle : {
                    text:"请求量趋势图（耗时区间）",
                    bottom:"10",
                    left: 'middle'
                },

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
            this.onRefresh();
            this.getStatData();
            this.getErrorPieData();
            this.getErrorLineData();
            this.getRequestBarData();
            this.getRequestLineData();
        },
        activated(){
        },
        deactivated(){
        },
        methods: {
            onRefresh(){
                let self = this;
                bus.$on("refreshTag",function (val) {
                    if(val === 'dashboard'){
                        console.log("dashboard refresh.................")
                        self.getStatData();
                        self.getErrorPieData();
                        self.getErrorLineData();
                        self.getRequestBarData();
                        self.getRequestLineData();
                    }
                });

            },
            getBeginTime(){
                return moment(this.pickerDate[0]).format('YYYY-MM-DD HH:mm');
            },
            getEndTime(){
                return moment(this.pickerDate[1]).format('YYYY-MM-DD HH:mm');
            },
            getStatData(){
                const url = "/api/dashboard/stat";
                this.$axios.post(url, {
                    beginTime: this.getBeginTime(),
                    endTime:this.getEndTime(),
                }).then((res) => {
                    console.log(res.data);
                    this.statData = res.data;
                })
            },
            getErrorPieData(){
                const url = "/api/dashboard/getErrorPieData";
                this.$axios.post(url, {
                    beginTime: this.getBeginTime(),
                    endTime:this.getEndTime(),
                }).then((res) => {
                    console.log(res.data);
                    this.errorPieData.rows = res.data.result;
                })
            },
            getErrorLineData(){
                const url = "/api/dashboard/getErrorLineData";
                this.$axios.post(url, {
                    beginTime: this.getBeginTime(),
                    endTime:this.getEndTime(),
                }).then((res) => {
                    console.log(res.data);
                    this.errorLineData.rows = res.data.rows;
                    this.errorLineData.columns = res.data.columns;
                })
            },
            getPickerDate(){//时间选择框选择的日期
                const self = this;
                bus.$on("pickerDateEvent",function (val) {
                    self.pickerDate = val;
                });
                bus.$emit("getPickerDateEvent");
            },
            getRequestBarData(){
                const url = "/api/dashboard/getRequestBarData";
                this.$axios.post(url, {
                    beginTime: this.getBeginTime(),
                    endTime:this.getEndTime(),
                }).then((res) => {
                    console.log("==>getRequestBarData:%o",res);
                    this.requestBarData.rows = res.data.result;
                })
            },
            getRequestLineData(){
                const url = "/api/dashboard/getRequestLineData";
                this.$axios.post(url, {
                    beginTime: this.getBeginTime(),
                    endTime:this.getEndTime(),
                }).then((res) => {
                    console.log("==>getRequestLineData:%o",res);
                    this.requestLineData.rows = res.data.rows;
                })
            }
        }
    }
</script>
