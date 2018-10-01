<template>
    <div>
        <el-card>
            <el-form ref="form" :model="form" label-width="80px" width="100%">
                <el-row :gutter="20">
                    <el-col :span="7">
                        <el-form-item label="应用分组">
                            <el-select v-model="form.group" placeholder="请选择">
                                <el-option key="bbk" label="步步高" value="bbk"></el-option>
                                <el-option key="xtc" label="小天才" value="xtc"></el-option>
                                <el-option key="imoo" label="imoo" value="imoo"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="7">
                        <el-form-item label="应用实例">
                            <el-select v-model="form.server" placeholder="请选择">
                                <el-option key="bbk" label="步步高" value="bbk"></el-option>
                                <el-option key="xtc" label="小天才" value="xtc"></el-option>
                                <el-option key="imoo" label="imoo" value="imoo"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="7">
                        <el-form-item label="排序">
                            <el-select v-model="form.sort" placeholder="请选择">
                                <el-option key="bbk" label="步步高" value="bbk"></el-option>
                                <el-option key="xtc" label="小天才" value="xtc"></el-option>
                                <el-option key="imoo" label="imoo" value="imoo"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="3" align="right">
                        <el-button @click="queryRequestList(1)" type="primary">查&nbsp;&nbsp;询</el-button>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="7">
                        <el-form-item label="gId"  style="margin-bottom: 0px">
                            <el-input v-model="form.gId"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="7">
                        <el-form-item label="应用IP"  style="margin-bottom: 0px">
                            <el-input v-model="form.ip"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
        </el-card>
        <el-card style="margin-top: -2px">
            <el-row>
                <el-col :span="24">
                    <ve-histogram :legend-visible='false' :extend="requestCharExtend" :data="requestChartData" :settings="requestChartSettings"></ve-histogram>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="24">
                    <el-table :data="requestTableData.rows" border class="table">
                        <el-table-column prop="id" label="ID" width="150">
                        </el-table-column>
                        <el-table-column prop="gId" label="GID" width="150">
                        </el-table-column>
                        <el-table-column prop="ip" label="IP" width="120">
                        </el-table-column>
                        <el-table-column prop="group" label="应用分组" width="120">
                        </el-table-column>
                        <el-table-column prop="server" label="应用实例" width="120">
                        </el-table-column>
                        <el-table-column prop="url" label="URL" >
                        </el-table-column>
                        <el-table-column label="操作" width="180" align="center">
                            <template slot-scope="scope">
                                <el-button type="text">参数</el-button>
                                <el-button type="text">调用链</el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                    <div class="pagination">
                        <el-pagination background  @current-change="handleCurrentChange" layout="prev, pager, next" :total="requestTableData.pageTotal" :current-page="requestTableData.currPageNum">
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
        data: function(){
            this.requestChartSettings = {
            }
            this.requestCharExtend = {
                'xAxis.0.axisLabel.rotate': 60
            }
            return {
                requestChartData: {
                    columns: ['time', '请求量'],
                    rows: [{'time': '1/1', '请求量': 1393},
                        {'time': '1/1', '请求量': 993},
                        {'time': '1/2', '请求量': 893},
                        {'time': '1/3', '请求量': 2393},
                        {'time': '1/4', '请求量': 1893},
                        {'time': '1/5', '请求量': 1293},
                        {'time': '1/6', '请求量': 1593},
                        {'time': '1/7', '请求量': 113},
                        {'time': '1/8', '请求量': 993}]
                },
                requestTableData: {
                    currPageNum:1,
                    pageTotal: 150,
                    rows: []
                },
                form:{
                    gId:'',
                    server:'',
                    group:'',
                    ip:'',
                    sort:''
                }
            }
        },
        created(){
            this.getPickerDate();
            this.queryRequestList(1);
        },
        methods: {
            getPickerDate(){
                const self = this;
                bus.$on("pickerDateEvent",function (val) {
                    self.pickerDate = val;
                });
                bus.$emit("getPickerDateEvent");
            },
            onSubmit() {
                this.$message.success('提交成功！');
            },
            // 分页导航
            handleCurrentChange(val) {
                this.queryRequestList(val);
            },
            // 表格数据
            queryRequestList(pageNum) {
                const url = "/api/request/list";
                this.$axios.post(url, {
                    pageNum: pageNum,
                    beginTime: moment(this.pickerDate[0]).format('YYYY-MM-DD HH:mm'),
                    endTime:moment(this.pickerDate[1]).format('YYYY-MM-DD HH:mm')
                }).then((res) => {
                    console.log("==>queryRequestList=%o",res);
                    this.requestTableData.rows = res.data.rows;
                    this.requestTableData.currPageNum = res.data.pageNum;
                    this.requestTableData.pageTotal = res.data.pageTotal;
                })
            }
        }
    }
</script>
