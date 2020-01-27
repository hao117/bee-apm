<template>
    <div>
        <el-row v-show="isShowTable">
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
                            <el-button @click="queryList(1)" type="primary">查&nbsp;&nbsp;询</el-button>
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
                        <ve-histogram style="height: 250px" height="100%" :legend-visible='false' :extend="chartExtend"
                                      :data="chartData" :settings="chartSettings"></ve-histogram>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-table :data="tableData.rows" border stripe>
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
                            <el-table-column prop="method" label="方法" :formatter="methodFormatter" min-width="250">
                            </el-table-column>
                            <el-table-column label="操作" width="180" align="center" fixed="right">
                                <template slot-scope="_">
                                    <el-button type="text" @click="queryById(_.row,'nvl','参数')">参数</el-button>
                                    <el-button type="text" @click="queryCallTree(_.row)">链路</el-button>
                                </template>
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
        </el-row>
        <el-row v-show="isShowTree">
            <el-card>
                <el-row>
                    <el-col :span="24" align="right" style="padding-bottom: 10px">
                        <el-button type="primary" @click="backButtonEvent()">返回</el-button>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <vxe-table
                            size="mini"
                            height="700"
                            row-key
                            show-overflow
                            highlight-hover-row
                            ref="xTree"
                            :tree-config="{children: 'children', expandAll: true, line: true,  iconOpen: 'fa fa-minus-square-o', iconClose: 'fa fa-plus-square-o'}"
                            :data="tableTreeListData">
                            <vxe-table-column title="链路" tree-node>
                                <template slot-scope="_">
                                    <span v-if="_.row.type=='req'" style="color: #3e5df0">{{_.row.text}}</span>
                                    <span v-else-if="_.row.type=='sql'" style="color: #c05d06">{{_.row.text}}</span>
                                    <span v-else>{{_.row.text}}</span>
                                    <a style="color:#cccccc;font-weight: bolder">|</a>
                                    <a style="color: #ffb601">{{_.row.app}}</a>
                                </template>
                            </vxe-table-column>
                            <vxe-table-column field="spend" title="耗时(ms)" width="80"></vxe-table-column>
                            <vxe-table-column title="操作" width="80">
                                <template slot-scope="_">
                                    <el-button type="text" @click="queryById(_.row,'nvl','参数')">参数</el-button>
                                </template>
                            </vxe-table-column>
                        </vxe-table>
                    </el-col>
                </el-row>
            </el-card>
        </el-row>
        <el-dialog
            :title="dialogTitle"
            :visible.sync="dialogVisible"
            width="60%">
            <el-input v-model="dialogTextarea" :rows="22" type="textarea" readonly>
            </el-input>
            <span slot="footer" class="dialog-footer">
                <el-button type="primary" @click="dialogVisible = false">关 闭</el-button>
            </span>
        </el-dialog>
    </div>
</template>
<style>
    .el-dialog__body {
        padding: 0px 20px !important;
        border-top: 1px;
        border-top-color: #1f2f3d;
    }

    textarea {
        overflow-wrap: normal !important;
        white-space: pre !important;
        padding: 10px !important;
    }
</style>
<script>
    import bus from '../common/bus';
    import JSONFormatter from "json-fmt";
    let moment = require("moment");

    export default {
        name: 'method',
        data: function () {
            return {
                tableTreeListData: [], //调用链数据
                isShowTable: true,
                isShowTree: false,
                dialogVisible: false,
                dialogTextarea: '',
                dialogTitle: '',
                chartSettings: {},
                chartExtend: {
                    'xAxis.0.axisLabel.rotate': 60,
                    series(v) {
                        v.forEach(i => {
                            i.barMaxWidth = 30
                        })
                        return v
                    },
                    grid: {
                        top: 10,
                        bottom: 5,
                        height: 230,
                    },
                },
                chartData: {
                    columns: ['time', '请求量'],
                    rows: []
                },
                tableData: {
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
            this.queryList(1);
            this.queryAppGroupList();
        },
        methods: {
            onRefresh() {
                let self = this;
                bus.$on("refreshTag", function (val) {
                    console.log("refreshTag =========== " + val);
                    if (val === 'method') {
                        console.log("method refresh.................")
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
                if (pageNum == 1) {
                    this.queryChartData();
                }
                const url = "/api/method/list";
                let params = this.form;
                params.pageNum = pageNum;
                params.beginTime = this.getBeginTime();
                params.endTime = this.getEndTime();
                this.$axios.post(url, params).then((res) => {
                    console.log("==>queryMethodtList=%o", res);
                    this.tableData.rows = res.data.rows;
                    this.tableData.currPageNum = res.data.pageNum;
                    this.tableData.pageTotal = res.data.pageTotal;
                })
            },
            // 柱状图数据
            queryChartData() {
                const url = "/api/method/chart";
                let params = this.form;
                params.beginTime = this.getBeginTime();
                params.endTime = this.getEndTime();
                this.$axios.post(url, params).then((res) => {
                    console.log("==>queryMethodChartData=%o", res);
                    this.chartData.rows = res.data.rows;
                })
            },
            timeFormatter(row, column) {
                return row.time.substring(11, 19);
            },
            methodFormatter(row, column) {
                return row.tags.clazz + "." + row.tags.method;
            },
            queryById(row,index,title) {
                if(index == "nvl" && row.type == "req"){
                    index = "bee-request-body";
                }else if(index == "nvl" && row.type == "proc"){
                    index = "bee-process-param";
                }
                const url = "/api/common/queryById";
                let params = {id: row.id, index: index};
                params.beginTime = this.getBeginTime();
                params.endTime = this.getEndTime();
                this.$axios.post(url, params).then((res) => {
                    console.log("==>queryById,title=%s，result=%o", title,res);
                    this.dialogVisible = true;
                    this.dialogTitle = title;
                    let fmt = new JSONFormatter(JSONFormatter.PRETTY);
                    if(index == "bee-process-param"){
                        let content = res.data.result.tags.param;
                        console.log("dialogTextarea=%o",content);
                        fmt.append(content);
                    }else{
                        let content = res.data.result.tags.body;
                        console.log("dialogTextarea=%o",content);
                        fmt.append(content);
                    }
                    this.dialogTextarea = fmt.flush();
                })
            },
            queryCallTree(row) {
                console.log("==>queryCallTree row=%o", row);
                const url = "/api/request/callTree";
                let params = {gid: row.gid, time: row.time};
                this.$axios.post(url, params).then((res) => {
                    console.log("==>queryCallTree result=%o", res.data.result);
                    this.$refs.xTree.reloadData(res.data.result);
                })
                this.isShowTable = false;
                this.isShowTree = true;
            },
            backButtonEvent() {
                this.isShowTable = true;
                this.isShowTree = false;
                this.tableTreeListData = [];
            },
        }
    }
</script>
