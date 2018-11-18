<template>
    <div>
        <el-card>
            <el-row :gutter="20">
                <el-col :span="24">
                    <div id="myTopology" style="height: 900px;width: 100%"></div>
                </el-col>
            </el-row>
        </el-card>
    </div>
</template>
<script>
    import bus from '../common/bus';
    import cytoscape from 'cytoscape';
    import coseBilkent from 'cytoscape-cose-bilkent';
    cytoscape.use( coseBilkent );
    let moment = require("moment");
    let userPos = {};
    export default {
        name: 'topology',
        data: function () {
            return {}
        },
        created(){

        },
        computed:{
        },
        mounted(){
            this.init();
        },
        methods: {
            getStyle(){
                return cytoscape.stylesheet()
                    .selector('node[sla]')
                    .css({
                        width: 60,
                        height: 60,
                        'text-valign': 'bottom',
                        'text-halign': 'center',
                        'font-family': 'Microsoft YaHei',
                        //      content: 'sla----sla',
                        'text-margin-y': 10,
                        'border-width': 10,
                        'border-color': '#2FC25B',
                        label: 'data(name)',
                    })
                    .selector(':selected')
                    .css({
                        'background-color': '#2FC25B'
                    })
                    .selector('.faded')
                    .css({
                        opacity: 0.25,
                        'text-opacity': 0,
                    })
                    .selector('node[!sla]')
                    .css({
                        width: 60,
                        height: 60,
                        'text-valign': 'bottom',
                        'text-halign': 'center',
                        'background-color': '#fff',
                        'background-image': ele => `http://localhost:8000/img/node/${ele.data('type') ? ele.data('type').toUpperCase() : 'UNDEFINED'}.png`,
                        'background-width': '60%',
                        'background-height': '60%',
                        'border-width': 0,
                        'font-family': 'Microsoft YaHei',
                        label: 'data(name)',
                        // 'text-margin-y': 5,
                    })
                    .selector('edge')
                    .css({
                        'curve-style': 'bezier',
                        'control-point-step-size': 100,
                        'target-arrow-shape': 'triangle',
                        'arrow-scale': 1.7,
                        'target-arrow-color':  ele => (ele.data('isAlert') ? 'rgb(204, 0, 51)' : 'rgb(147, 198, 174)'),
                        'line-color':  ele => (ele.data('isAlert') ? 'rgb(204, 0, 51)' : 'rgb(147, 198, 174)'),
                        width: 3,
                        label: ele => `http \n  cpm / 5 ms`,
                        'text-wrap': 'wrap',
                        color: 'rgb(110, 112, 116)',
                        'text-rotation': 'autorotate',
                    });
            },
            init(){
                let self = this;
                let myData = {
                    "edges": [
                        {
                            "data": {
                                "source": "user",
                                "target": 1,
                                "isAlert": true,
                                "callType": "rpc",
                                "cpm": 948,
                                "avgResponseTime": 1221,
                                "id": "100-1"
                            }
                        },
                        {
                            "data": {
                                "source": 1,
                                "target": 2,
                                "isAlert": false,
                                "callType": "rpc",
                                "cpm": 471,
                                "avgResponseTime": 1209,
                                "id": "1-2"
                            }
                        },
                        {
                            "data": {
                                "source": 2,
                                "target": 1,
                                "isAlert": true,
                                "callType": "http",
                                "cpm": 393,
                                "avgResponseTime": 2397,
                                "id": "2-1"
                            }
                        },
                        {
                            "data": {
                                "source": 1,
                                "target": 200,
                                "isAlert": true,
                                "callType": "http",
                                "cpm": 972,
                                "avgResponseTime": 4256,
                                "id": "1-200"
                            }
                        },
                        {
                            "data": {
                                "source": 1,
                                "target": 201,
                                "isAlert": true,
                                "callType": "rpc",
                                "cpm": 1180,
                                "avgResponseTime": 3437,
                                "id": "1-201"
                            }
                        },
                        {
                            "data": {
                                "source": 2,
                                "target": 202,
                                "isAlert": false,
                                "callType": "dubbo",
                                "cpm": 194,
                                "avgResponseTime": 2626,
                                "id": "2-202"
                            }
                        },
                        {
                            "data": {
                                "source": 2,
                                "target": 203,
                                "isAlert": true,
                                "callType": "rpc",
                                "cpm": 1738,
                                "avgResponseTime": 1443,
                                "id": "2-203"
                            }
                        },
                        {
                            "data": {
                                "source": 2,
                                "target": 204,
                                "isAlert": false,
                                "callType": "http",
                                "cpm": 319,
                                "avgResponseTime": 1415,
                                "id": "2-204"
                            }
                        },
                        {
                            "data": {
                                "source": 1,
                                "target": 301,
                                "isAlert": false,
                                "callType": "http",
                                "cpm": 319,
                                "avgResponseTime": 1415,
                                "id": "1-301"
                            }
                        },
                        {
                            "data": {
                                "source": 203,
                                "target": 3,
                                "isAlert": false,
                                "callType": "http",
                                "cpm": 319,
                                "avgResponseTime": 1415,
                                "id": "203-3"
                            }
                        }
                    ],
                    "nodes": [
                        /**/
                        {
                            "data": {
                                "id": "user",
                                "name": "User",
                                "type": "USER",
                                "size": 60
                            }
                        },
                        {
                            "data": {
                                "id": 204,
                                "name": "Jessica Clark",
                                "type": "MYSQL",
                                "size": 60
                            }
                        },
                        {
                            "data": {
                                "id": 1,
                                "name": "Daniel Davis",
                                "type": "tomcat",
                                "cpm": 1986,
                                "sla": 81.9,
                                "apdex": 0.15,
                                "avgResponseTime": 549,
                                "isAlarm": false,
                                "numOfServer": 82,
                                "numOfServerAlarm": 28,
                                "numOfServiceAlarm": 20,
                                "size": 120
                            }
                        },
                        {
                            "data": {
                                "id": 2,
                                "name": "Maria Walker",
                                "type": "tomcat",
                                "cpm": 926,
                                "sla": 65.49,
                                "apdex": 0.45,
                                "avgResponseTime": 507,
                                "isAlarm": false,
                                "numOfServer": 31,
                                "numOfServerAlarm": 43,
                                "numOfServiceAlarm": 32,
                                "size": 79.61904761904762
                            }
                        },
                        {
                            "data": {
                                "id": 3,
                                "name": "Mary Lewis",
                                "type": "SPRINGMVC",
                                "cpm": 411,
                                "sla": 82.9,
                                "apdex": 0.81,
                                "avgResponseTime": 633,
                                "isAlarm": true,
                                "numOfServer": 4,
                                "numOfServerAlarm": 88,
                                "numOfServiceAlarm": 11,
                                "size": 60
                            }
                        },
                        {
                            "data": {
                                "id": 200,
                                "name": "Sharon Perez",
                                "type": "MYSQL",
                                "size": 60
                            }
                        },
                        {
                            "data": {
                                "id": 201,
                                "name": "Paul Walker",
                                "type": "Oracle",
                                "size": 60
                            }
                        },
                        {
                            "data": {
                                "id": 202,
                                "name": "Dorothy Miller",
                                "type": "Oracle",
                                "size": 60
                            }
                        },
                        {
                            "data": {
                                "id": 203,
                                "name": "Ruth Anderson",
                                "type": "MYSQL",
                                "size": 60
                            }
                        },{
                            "data": {
                                "id": 301,
                                "name": "tom-301",
                                "type": "tomcat",
                                "cpm": 1986,
                                "sla": 81.9,
                                "apdex": 0.15,
                                "avgResponseTime": 549,
                                "isAlarm": false,
                                "numOfServer": 82,
                                "numOfServerAlarm": 28,
                                "numOfServiceAlarm": 20,
                                "size": 120
                            }
                        }
                    ]
                };

                let layout = {
                    name: 'cose-bilkent',
                    animate: true,
                    idealEdgeLength: 200,
                    edgeElasticity: 0.1,
                    animate: true,
                    transform:function (node, position ){
                        if(node.data().id == 'user'){
                            // position.x = -250;
                            // position.y = 410;
                            //position = userPos;
                            console.log("===============user===>%o",position);
                        }
                        if(node.data().id == '1'){
                            // position.x = -250;
                            // position.y = 410;
                            userPos.y = position.y;
                            userPos.x = position.x - 300;
                            console.log("===============1===>%o",position);
                        }
                        return position;
                    }
                }

                let cy = window.cy = cytoscape({
                    container: document.getElementById('myTopology'),
                    zoom: 1,
                    maxZoom: 1,
                    boxSelectionEnabled: true,
                    wheelSensitivity: 0.2,
                    style: self.getStyle(),
                    directed: true,
                    elements:myData,
                    layout: layout
                });
            },
            changePosition(){
                console.log("======11111========%o",userPos);
                let pos = window.cy.$("#usr").position();
                console.log("=======aaaa=======%o",pos);
            }
        }

    }

</script>
