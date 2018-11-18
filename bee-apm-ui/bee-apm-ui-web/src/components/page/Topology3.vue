<template>
    <div id="view">
        <button v-on:click="add_node">push</button>
        <div id="cy"></div>
    </div>
</template>
<script>
    import cytoscape from 'cytoscape';
    import coseBilkent from 'cytoscape-cose-bilkent';
    cytoscape.use( coseBilkent );
    export default {
        name: 'Cytoscape',
        components: {},
        created: function () {
        },
        data: function () {
            return {
                input: '',
                output: '',
                msg: 'vue to cytoscape',
                count: 0
            }
        },
        methods: {
            add_node: function () {
                console.info(this.cy)
                this.cy.add([
                    { 'group': 'nodes', data: { 'id': 'node' + this.count }, position: { x: 300, y: 200 } },
                    {'group': 'edges', data: {'id': 'edge' + this.count, 'source': 'node' + this.count, 'target': 'cat'}}
                ])
            },
            view_init: function () {
                this.cy = cytoscape(
                    {
                        container: document.getElementById('cy'),
                        boxSelectionEnabled: false,
                        autounselectify: true,
                        style: cytoscape.stylesheet()
                            .selector('node')
                            .css({
                                'height': 80,
                                'width': 80,
                                'background-fit': 'cover',
                                'border-color': '#000',
                                'border-width': 3,
                                'border-opacity': 0.5,
                                'content': 'data(name)',
                                'text-valign': 'center'
                            })
                            .selector('edge')
                            .css({
                                'width': 6,
                                'target-arrow-shape': 'triangle',
                                'line-color': '#ffaaaa',
                                'target-arrow-color': '#ffaaaa',
                                'curve-style': 'bezier'
                            }),
                        elements: {
                            nodes: [
                                { data: { id: 'cat' } },
                                { data: { id: 'bird' } },
                                { data: { id: 'ladybug' } },
                                { data: { id: 'aphid' } },
                                { data: { id: 'rose' } },
                                { data: { id: 'grasshopper' } },
                                { data: { id: 'plant' } },
                                { data: { id: 'wheat' } }
                            ],
                            edges: [
                                { data: { source: 'cat', target: 'bird' } },
                                { data: { source: 'bird', target: 'ladybug' } },
                                { data: { source: 'bird', target: 'grasshopper' } },
                                { data: { source: 'grasshopper', target: 'plant' } },
                                { data: { source: 'grasshopper', target: 'wheat' } },
                                { data: { source: 'ladybug', target: 'aphid' } },
                                { data: { source: 'aphid', target: 'rose' } }
                            ]
                        },
                        layout: {
                            name: 'cose-bilkent',
                            directed: true,
                            padding: 10
                        }
                    }
                )
            }
        },
        computed: {
        },
        mounted: function () {
            this.view_init()
        }
    }

</script>

<style scoped>
    #cy {
        width: 100%;
        height: 80%;
        position: absolute;
        top: 50px;
        left: 0px;
        text-align: left;
    }

    body {
        font: 14px helvetica neue, helvetica, arial, sans-serif;
    }
</style>
