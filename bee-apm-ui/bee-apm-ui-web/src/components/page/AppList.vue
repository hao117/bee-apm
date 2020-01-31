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
                        <el-form-item label="IP" style="margin-bottom: 0px">
                            <el-input v-model="form.ip"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="3" align="right">
                        <el-button @click="queryList(1)" type="primary">查&nbsp;&nbsp;询</el-button>
                    </el-col>
                </el-row>
            </el-form>
        </el-card>
        <el-card style="margin-top: -2px">
            <el-row>
                <el-col :span="24">
                    <el-table :data="tableData.rows" border stripe>
                        <el-table-column prop="app" label="应用" width="150">
                        </el-table-column>
                        <el-table-column prop="inst" label="实例" width="150">
                        </el-table-column>
                        <el-table-column prop="ip" label="IP" width="150">
                        </el-table-column>
                        <el-table-column prop="env" label="环境" width="150">
                        </el-table-column>
                        <el-table-column prop="tags.version" label="版本" width="150">
                        </el-table-column>
                        <el-table-column prop="time" label="时间" width="100" :formatter="timeFormatter">
                        </el-table-column>
                    </el-table>
                    <div class="pagination">
                        <el-pagination background @current-change="handlePageChange" layout="prev, pager, next"
                                       :total="tableData.pageTotal" :current-page="tableData.currPageNum">
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
        name: 'appList',
        data: function () {
            return {
                tableData: {
                    currPageNum: 1,
                    pageTotal: 150,
                    rows: []
                },
                form: {
                    env: null,
                    app: null,
                    ip: null,
                },
                envOptions: [],
                appOptions: []
            }
        },
        created() {
            this.onRefresh();
            this.getPickerDate();
            this.queryEnvGroupList();
            this.queryList(1);
            this.queryAppGroupList();
        },
        methods: {
            onRefresh() {
                let self = this;
                bus.$on("refreshTag", function (val) {
                    console.log("refreshTag =========== " + val);
                    if (val === 'appList') {
                        console.log("appList refresh.................")
                        self.getPickerDate();
                        self.queryEnvGroupList();
                        self.queryAppGroupList();
                        self.queryList(1);
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
                this.queryList(val);
            },
            // 表格数据
            queryList(pageNum) {
                const url = "/api/app/info/list";
                let params = this.form;
                params.pageNum = pageNum;
                params.beginTime = this.getBeginTime();
                params.endTime = this.getEndTime();
                this.$axios.post(url, params).then((res) => {
                    console.log("==>queryAppList=%o", res);
                    this.tableData.rows = res.data.rows;
                    this.tableData.currPageNum = res.data.pageNum;
                    this.tableData.pageTotal = res.data.pageTotal;
                })
            },
            timeFormatter(row, column) {
                return row.time.substring(11, 19);
            }
        }
    }
</script>
