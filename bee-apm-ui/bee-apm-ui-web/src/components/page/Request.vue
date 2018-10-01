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
                            <el-select v-model="form.server" placeholder="请选择">
                                <el-option key="bbk" label="步步高" value="bbk"></el-option>
                                <el-option key="xtc" label="小天才" value="xtc"></el-option>
                                <el-option key="imoo" label="imoo" value="imoo"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="3" align="right">
                        <el-button type="primary">查  询</el-button>
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
                    <el-table :data="data" border class="table" ref="multipleTable" @selection-change="handleSelectionChange">
                        <el-table-column prop="date" label="ID" width="150">
                        </el-table-column>
                        <el-table-column prop="date" label="GID" width="150">
                        </el-table-column>
                        <el-table-column prop="name" label="IP" width="120">
                        </el-table-column>
                        <el-table-column prop="name" label="应用分组" width="120">
                        </el-table-column>
                        <el-table-column prop="name" label="应用实例" width="120">
                        </el-table-column>
                        <el-table-column prop="address" label="URL" :formatter="formatter">
                        </el-table-column>
                        <el-table-column label="操作" width="180" align="center">
                            <template slot-scope="scope">
                                <el-button type="text" icon="el-icon-edit" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
                                <el-button type="text" icon="el-icon-delete" class="red" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                </el-col>
            </el-row>
        </el-card>
    </div>
</template>

<script>
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
                options:[
                    {
                        value: 'guangdong',
                        label: '广东省',
                        children: [
                            {
                                value: 'guangzhou',
                                label: '广州市',
                                children: [
                                    {
                                        value: 'tianhe',
                                        label: '天河区'
                                    },
                                    {
                                        value: 'haizhu',
                                        label: '海珠区'
                                    }
                                ]
                            },
                            {
                                value: 'dongguan',
                                label: '东莞市',
                                children: [
                                    {
                                        value: 'changan',
                                        label: '长安镇'
                                    },
                                    {
                                        value: 'humen',
                                        label: '虎门镇'
                                    }
                                ]
                            }
                        ]
                    },
                    {
                        value: 'hunan',
                        label: '湖南省',
                        children: [
                            {
                                value: 'changsha',
                                label: '长沙市',
                                children: [
                                    {
                                        value: 'yuelu',
                                        label: '岳麓区'
                                    }
                                ]
                            }
                        ]
                    }
                ],
                form: {
                    name: '',
                    region: '',
                    date1: '',
                    date2: '',
                    delivery: true,
                    type: ['步步高'],
                    resource: '小天才',
                    desc: '',
                    options: []
                }
            }
        },
        methods: {
            onSubmit() {
                this.$message.success('提交成功！');
            }
        }
    }
</script>
