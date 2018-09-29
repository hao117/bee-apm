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
    let moment = require("moment");

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
                activeTagTitle:'系统首页',
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
                    columns: ['time', '0~200',"200~500","500~1000","1000~2000","2000~3000","3000~*"],
                    rows: []
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
            this.onTagTitle();
            this.getPickerDate();
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
            },
            onTagTitle(){
                let self = this;
                bus.$on("tagTitle",function (val) {
                    self.activeTagTitle = val;
                    console.log("====>系统首页----activeTagTitle="+self.activeTagTitle);
                });
            },
            getRequestBarData(){
                const url = "/api/dashboard/getRequestBarData";
                this.$axios.post(url, {
                    beginTime: moment(this.pickerDate[0]).format('YYYY-MM-DD HH-mm'),
                    endTime:moment(this.pickerDate[1]).format('YYYY-MM-DD HH-mm'),
                }).then((res) => {
                    console.log("==>getRequestBarData:%o",res);
                    this.requestBarData.rows = res.data.rows;
                })
            },
            getRequestLineData(){
                const url = "/api/dashboard/getRequestLineData";
                this.$axios.post(url, {
                    beginTime: moment(this.pickerDate[0]).format('YYYY-MM-DD HH-mm'),
                    endTime:moment(this.pickerDate[1]).format('YYYY-MM-DD HH-mm'),
                }).then((res) => {
                    console.log("==>getRequestLineData:%o",res);
                    this.requestLineData.rows = res.data.rows;
                })
            }

        }
    }
</script>
