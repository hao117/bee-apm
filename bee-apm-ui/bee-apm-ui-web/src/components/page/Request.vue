<template>
    <div>
        <el-card>
            <el-form ref="form" :model="form" label-width="80px" width="100%">
                <el-row :gutter="20">
                    <el-col :span="7">
                        <el-form-item label="环境">
                            <el-select v-model="form.env" placeholder="请选择">
                                <el-option
                                    v-for="item in envOptions"
                                    :key="item.value"
                                    :label="item.name"
                                    :value="item.value">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="7">
                        <el-form-item label="应用">
                            <el-select v-model="form.app" placeholder="请选择">
                                <el-option
                                    v-for="item in appOptions"
                                    :key="item.value"
                                    :label="item.name"
                                    :value="item.value">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="7">
                        <el-form-item label="排序">
                            <el-select v-model="form.sort" placeholder="请选择">
                                <el-option key="time" label="时间" value="time"></el-option>
                                <el-option key="spend" label="耗时" value="spend"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="3" align="right">
                        <el-button @click="queryRequestList(1)" type="primary">查&nbsp;&nbsp;询</el-button>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="7">
                        <el-form-item label="gId" style="margin-bottom: 0px">
                            <el-input v-model="form.gid"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="7">
                        <el-form-item label="IP" style="margin-bottom: 0px">
                            <el-input v-model="form.ip"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
        </el-card>
        <el-card style="margin-top: -2px">
            <el-row>
                <el-col :span="24">
                    <ve-histogram :legend-visible='false' :extend="requestCharExtend" :data="requestChartData"
                                  :settings="requestChartSettings"></ve-histogram>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="24">
                    <el-table :data="requestTableData.rows" border stripe>
                        <el-table-column prop="id" label="ID" width="170" fixed>
                        </el-table-column>
                        <el-table-column prop="time" label="时间" width="150" :formatter="timeFormatter" fixed>
                        </el-table-column>
                        <el-table-column prop="gid" label="GID" width="170">
                        </el-table-column>
                        <el-table-column prop="ip" label="IP" width="120">
                        </el-table-column>
                        <el-table-column prop="env" label="环境" width="120">
                        </el-table-column>
                        <el-table-column prop="app" label="应用" width="120">
                        </el-table-column>
                        <el-table-column prop="spend" label="耗时" width="100">
                        </el-table-column>
                        <el-table-column prop="tags.url" label="URL" min-width="250">
                        </el-table-column>
                        <el-table-column label="操作" width="180" align="center" fixed="right">
                            <template slot-scope="scope">
                                <el-button type="text">参数</el-button>
                                <el-button type="text">调用链</el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                    <div class="pagination">
                        <el-pagination background @current-change="handlePageChange" layout="prev, pager, next"
                                       :total="requestTableData.pageTotal" :current-page="requestTableData.currPageNum">
                        </el-pagination>
                    </div>
                </el-col>
            </el-row>
        </el-card>
    </div>
</template>

<script>
    import bus from '../common/bus';

    let moment = require("moment");

    export default {
        name: 'request',
        data: function () {
            return {
                requestChartSettings: {},
                requestCharExtend: {
                    'xAxis.0.axisLabel.rotate': 60,
                    series(v) {
                        v.forEach(i => {
                            i.barMaxWidth = 50
                        })
                        return v
                    }
                },
                requestChartData: {
                    columns: ['time', '请求量'],
                    rows: []
                },
                requestTableData: {
                    currPageNum: 1,
                    pageTotal: 150,
                    rows: []
                },
                form: {
                    gid: null,
                    env: null,
                    app: null,
                    ip: null,
                    sort: null
                },
                envOptions: [],
                appOptions: []
            }
        },
        created() {
            this.onRefresh();
            this.getPickerDate();
            this.queryEnvGroupList();
            this.queryRequestList(1);
            this.queryAppGroupList();
        },
        methods: {
            onRefresh() {
                let self = this;
                bus.$on("refreshTag", function (val) {
                    console.log("refreshTag =========== " + val);
                    if (val === 'request') {
                        console.log("request refresh.................")
                        self.getPickerDate();
                        self.queryEnvGroupList();
                        self.queryAppGroupList();
                        self.queryRequestList(1);
                    }
                });
            },
            getBeginTime() {
                return moment(this.pickerDate[0]).format('YYYY-MM-DD HH:mm');
            },
            getEndTime() {
                return moment(this.pickerDate[1]).format('YYYY-MM-DD HH:mm');
            },
            getPickerDate() {
                const self = this;
                bus.$on("pickerDateEvent", function (val) {
                    self.pickerDate = val;
                });
                bus.$emit("getPickerDateEvent");
            },
            queryEnvGroupList() {
                const url = "/api/common/getGroupList";
                this.$axios.post(url, {
                    beginTime: this.getBeginTime(),
                    endTime: this.getEndTime(),
                    group: "env"
                }).then((res) => {
                    console.log("==>queryEnvGroupList=%o", res);
                    this.envOptions = res.data.result;
                })
            },
            queryAppGroupList() {
                const url = "/api/common/getGroupList";
                this.$axios.post(url, {
                    beginTime: this.getBeginTime(),
                    endTime: this.getEndTime(),
                    group: "app"
                }).then((res) => {
                    console.log("==>queryAppGroupList=%o", res);
                    this.appOptions = res.data.result;
                })
            },
            // 分页导航
            handlePageChange(val) {
                this.queryRequestList(val);
            },
            // 表格数据
            queryRequestList(pageNum) {
                if (pageNum == 1) {
                    this.getRequestChartData();
                }
                const url = "/api/request/list";
                let params = this.form;
                params.pageNum = pageNum;
                params.beginTime = this.getBeginTime();
                params.endTime = this.getEndTime();
                this.$axios.post(url, params).then((res) => {
                    console.log("==>queryRequestList=%o", res);
                    this.requestTableData.rows = res.data.rows;
                    this.requestTableData.currPageNum = res.data.pageNum;
                    this.requestTableData.pageTotal = res.data.pageTotal;
                })
            },
            // 柱状图数据
            getRequestChartData() {
                const url = "/api/request/chart";
                let params = this.form;
                params.beginTime = this.getBeginTime();
                params.endTime = this.getEndTime();
                this.$axios.post(url, params).then((res) => {
                    console.log("==>getRequestChartData=%o", res);
                    this.requestChartData.rows = res.data.rows;
                })
            },
            timeFormatter(row, column) {
                return row.time.substring(11, 19);
            }
        }
    }
</script>
