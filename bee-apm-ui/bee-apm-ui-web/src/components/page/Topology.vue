<template>
    <div>
        <el-card>
            <el-row :gutter="20">
                <el-col :span="24">
                    <div id="myChart" style="height: 900px;width: 100%"></div>
                </el-col>
            </el-row>
        </el-card>
    </div>
</template>

<script>
    import bus from '../common/bus';
    let moment = require("moment");
    import echarts from 'echarts'

    export default {
        name: 'topology',
        data () {
            return {
                msg: 'Welcome to Your Vue.js App'
            }
        },
        mounted(){
            this.drawLine();
        },
        methods: {
            drawLine(){
                let myChart = echarts.init(document.getElementById('myChart'))
                let option = {
                    title: { text: '拓扑图' },
                    tooltip: {
                        formatter: function (x) {
                            return x.data.des;
                        }
                    },
                    series: [
                        {
                            type: 'graph',
                            layout: 'force',
                            symbolSize: 80,
                            roam: true,
                            edgeSymbol: ['circle', 'arrow'],
                            edgeSymbolSize: [4, 10],
                            edgeLabel: {
                                normal: {
                                    textStyle: {
                                        fontSize: 20
                                    }
                                }
                            },
                            force: {
                                repulsion: 2500,
                                edgeLength: [10, 50]
                            },
                            draggable: true,
                            itemStyle: {
                                normal: {
                                    color: '#0cbffe'
                                }
                            },
                            lineStyle: {
                                normal: {
                                    width: 2,
                                    color: '#4b565b'
                                }
                            },
                            edgeLabel: {
                                normal: {
                                    show: true,
                                    formatter: function (x) {
                                        return x.data.name;
                                    }
                                }
                            },
                            label: {
                                normal: {
                                    show: true,
                                    textStyle: {
                                    }
                                }
                            },
                            data: [
                                {
                                    name: 'user',
                                    itemStyle: {
                                        normal: {
                                            color: '#51cc12'
                                        }
                                    }
                                },{
                                    name: 'app-1'
                                },{
                                    name: 'app-2'
                                },{
                                    name: 'app-3'
                                },{
                                    name: 'app-4'
                                },{
                                    name: 'app-5'
                                },{
                                    name: 'app-6'
                                },{
                                    name: 'app-7'
                                },{
                                    name: 'db'
                                },{
                                    name: 'cache'
                                }
                            ],
                            links: [
                                {
                                    source: 'user',
                                    target: 'app-1',
                                    name: '1'
                                }, {
                                    source: 'app-1',
                                    target: 'app-2',
                                    name: '2'
                                }, {
                                    source: 'user',
                                    target: 'app-2',
                                    name: '2'
                                }, {
                                    source: 'user',
                                    target: 'app-3',
                                    name: '1'
                                }, {
                                    source: 'app-2',
                                    target: 'app-3',
                                    name: '1'
                                }, {
                                    source: 'app-3',
                                    target: 'app-7',
                                    name: '1'
                                }, {
                                    source: 'app-3',
                                    target: 'app-4',
                                    name: '1'
                                }, {
                                    source: 'app-4',
                                    target: 'db',
                                    name: '5'
                                }, {
                                    source: 'app-5',
                                    target: 'cache',
                                    name: '3'
                                }, {
                                    source: 'app-3',
                                    target: 'app-5',
                                    name: '2'
                                }, {
                                    source: 'app-4',
                                    target: 'app-6',
                                    name: '3'
                                }, {
                                    source: 'app-6',
                                    target: 'app-3',
                                    name: '3'
                                }, {
                                    source: 'app-6',
                                    target: 'app-5',
                                    name: '3'
                                }]
                        }
                    ]
                };
                myChart.setOption(option);
            }
        }
    }

</script>
