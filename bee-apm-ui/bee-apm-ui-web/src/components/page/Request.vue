<template>
    <div>
        <el-row v-show="isShowTable">
            <el-card>
                <el-form ref="form" :model="form" label-width="80px" width="100%">
                    <el-row :gutter="20">
                        <el-col :span="5">
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
                        <el-col :span="5">
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
                        <el-col :span="5">
                            <el-form-item label="排序">
                                <el-select v-model="form.sort" placeholder="请选择">
                                    <el-option key="time" label="时间" value="time"></el-option>
                                    <el-option key="spend" label="耗时" value="spend"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="5">
                            <el-form-item label="类型">
                                <el-select v-model="form.entry" placeholder="请选择">
                                    <el-option key="all" label="全部" value=""></el-option>
                                    <el-option key="entry" label="入口请求" value="nvl"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="2" align="right">
                            <el-button @click="queryRequestList(1)" type="primary">查&nbsp;&nbsp;询</el-button>
                        </el-col>
                    </el-row>
                    <el-row :gutter="20">
                        <el-col :span="10">
                            <el-form-item label="gId" style="margin-bottom: 0px">
                                <el-input v-model="form.gid"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="10">
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
                        <ve-histogram style="height: 250px" height="100%" :legend-visible='false'
                                      :extend="requestCharExtend" :data="requestChartData"
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
                                <template slot-scope="_">
                                    <el-button type="text" @click="queryById(_.row,'bee-request-body','请求参数')">入参
                                    </el-button>
                                    <el-button type="text" @click="queryById(_.row,'bee-response-body','请求返回值')">回参
                                    </el-button>
                                    <el-button type="text" @click="showVisTopology(_.row)">拓扑</el-button>
                                    <el-button type="text" @click="queryCallTree(_.row)">链路</el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                        <div class="pagination">
                            <el-pagination background @current-change="handlePageChange" layout="prev, pager, next"
                                           :total="requestTableData.pageTotal"
                                           :current-page="requestTableData.currPageNum">
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
        <el-row v-show="isShowTopology">
            <el-card>
                <el-row>
                    <el-col :span="12" align="left" style="padding-left: 5px;padding-top: 10px">
                        调用关系拓扑图
                    </el-col>
                    <el-col :span="12" align="right" style="padding-bottom: 10px">
                        <el-button type="primary" @click="backButtonEvent()">返回</el-button>
                    </el-col>
                </el-row>
                <el-row>
                    <el-card id="vis-topology" style="height: 700px">
                    </el-card>
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
    import '../iconfont/iconfont.css';
    import Vis from 'vis-network';
    import JSONFormatter from "json-fmt";

    let moment = require("moment");

    export default {
        name: 'request',
        components: {},
        data: function () {
            return {
                tableTreeListData: [], //调用链数据
                isShowTable: true,
                isShowTree: false,
                dialogVisible: false,
                dialogTextarea: '',
                dialogTitle: '',
                isShowTopology: false,
                visTopology: {
                    nodes: [],
                    edges: [],
                    container: null,
                    options: {
                        autoResize: true,
                        nodes: {
                            shape: 'image',
                            borderWidth: 2,
                            shadow: true,
                            size: 20,
                            image: {
                                selected: 'static/img/app1.png',
                                unselected: 'static/img/app1.png'
                            }
                        },
                        edges: {
                            shadow: true
                        }
                    },
                    data: {}
                },
                requestChartSettings: {},
                requestCharExtend: {
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
                requestChartData: {
                    columns: ['time', '请求量'],
                    rows: []
                },
                requestTableData: {
                    currPageNum: 1,
                    pageTotal: 150,
                    rows: []
                },
                treeData: [],
                form: {
                    gid: null,
                    env: null,
                    app: null,
                    ip: null,
                    sort: null,
                    entry: null,
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
            },
            queryById(row, index, title) {
                if (index == "nvl" && row.type == "req") {
                    index = "bee-request-body";
                } else if (index == "nvl" && row.type == "proc") {
                    index = "bee-process-param";
                }
                const url = "/api/common/queryById";
                let params = {id: row.id, index: index};
                params.beginTime = this.getBeginTime();
                params.endTime = this.getEndTime();
                this.$axios.post(url, params).then((res) => {
                    console.log("==>queryById,title=%s，result=%o", title, res);
                    this.dialogVisible = true;
                    this.dialogTitle = title;
                    let fmt = new JSONFormatter(JSONFormatter.PRETTY);
                    if (index == "bee-process-param") {
                        let content = res.data.result.tags.param;
                        console.log("dialogTextarea=%o", content);
                        fmt.append(content);
                    } else {
                        let content = res.data.result.tags.body;
                        console.log("dialogTextarea=%o", content);
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
                this.isShowTopology = false;
                this.tableTreeListData = [];
            },
            showVisTopology(row) {
                let _this = this;
                console.log("==>initVisTopology row=%o", row);
                const url = "/api/request/topology";
                let params = {gid: row.gid, time: row.time};
                this.$axios.post(url, params).then((res) => {
                    console.log("==>initVisTopology result=%o", res);
                    let data = res.data.result;
                    _this.visTopology.nodes = new Vis.DataSet(_this.formatNodes(data.nodes));
                    _this.visTopology.edges = new Vis.DataSet(_this.formatEdges(data.edges));
                    _this.visTopology.container = document.getElementById('vis-topology');
                    _this.visTopology.data = {
                        nodes: _this.visTopology.nodes,
                        edges: _this.visTopology.edges
                    };
                    _this.network = new Vis.Network(_this.visTopology.container, _this.visTopology.data, _this.visTopology.options);
                })
                this.isShowTable = false;
                this.isShowTopology = true;
            },
            formatNodes(nodes) {
                for (let i = 0; i < nodes.length; i++) {
                    let node = nodes[i]
                    if (node.label == 'nvl') {
                        node.label = 'start';
                        node.image = {
                            selected: 'static/img/user.png',
                            unselected: 'static/img/user.png'
                        }
                    }
                }
                return nodes;
            },
            formatEdges(edges) {
                for (let i = 0; i < edges.length; i++) {
                    let edge = edges[i];
                    edge.arrows = 'to';
                    edge.label = edge.times + '';
                }
                return edges;
            }
        }
    }
</script>


