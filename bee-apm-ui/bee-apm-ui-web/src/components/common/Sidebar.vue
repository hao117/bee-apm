<template>
    <div class="sidebar icon_lists clear">
        <el-menu class="sidebar-el-menu" :default-active="onRoutes" :collapse="collapse" background-color="#324157"
            text-color="#bfcbd9" active-text-color="#20a0ff" unique-opened router>
            <template v-for="item in items">
                <template v-if="item.subs">
                    <el-submenu :index="item.index" :key="item.index">
                        <template slot="title">
                            <i :class="item.icon"></i><span slot="title">{{ item.title }}</span>
                        </template>
                        <template v-for="subItem in item.subs">
                            <el-submenu v-if="subItem.subs" :index="subItem.index" :key="subItem.index">
                                <template slot="title">{{ subItem.title }}</template>
                                <el-menu-item v-for="(threeItem,i) in subItem.subs" :key="i" :index="threeItem.index">
                                    {{ threeItem.title }}
                                </el-menu-item>
                            </el-submenu>
                            <el-menu-item v-else :index="subItem.index" :key="subItem.index">
                                {{ subItem.title }}
                            </el-menu-item>
                        </template>
                    </el-submenu>
                </template>
                <template v-else>
                    <el-menu-item :index="item.index" :key="item.index">
                        <i :class="item.icon"></i><span slot="title">{{ item.title }}</span>
                    </el-menu-item>
                </template>
            </template>
        </el-menu>
    </div>
</template>

<script>
    import bus from '../common/bus';
    export default {
        data() {
            return {
                collapse: false,
                items: [
                    {
                        icon: 'el-icon-lx-home',
                        index: 'dashboard',
                        title: '仪表盘'
                    },
                    {
                        icon: 'icon beeicon bee-gongzuoliu',
                        index: 'topology',
                        title: '拓扑图'
                    },
                    {
                        icon: 'el-icon-lx-people',
                        index: 'request',
                        title: '请求查询'
                    },
                    {
                        icon: 'el-icon-lx-searchlist',
                        index: 'method',
                        title: '方法查询'
                    },
                    {
                        icon: 'icon beeicon bee-qiandao_o',
                        index: '3',
                        title: '数据库',
                        subs: [
                            {
                                index: 'form',
                                title: 'SQL'
                            },
                            {
                                index: 'tx',
                                title: '事务',
                            }
                        ]
                    },
                    {
                        icon: 'el-icon-lx-info',
                        index: 'icon',
                        title: 'Logger查询'
                    },
                    {
                        icon: 'el-icon-lx-apps',
                        index: 'charts',
                        title: '实例信息'
                    },
                    {
                        icon: 'icon beeicon bee-caidan',
                        index: '6',
                        title: '更多',
                        subs: [
                            {
                                index: 'permission',
                                title: '权限测试'
                            },
                            {
                                index: '404',
                                title: '404页面'
                            }
                        ]
                    }
                ]
            }
        },
        computed:{
            onRoutes(){
                return this.$route.path.replace('/','');
            }
        },
        created(){
            // 通过 Event Bus 进行组件间通信，来折叠侧边栏
            bus.$on('collapse', msg => {
                this.collapse = msg;
            })
        }
    }
</script>

<style scoped>
    .sidebar{
        display: block;
        position: absolute;
        left: 0;
        top: 70px;
        bottom:0;
        overflow-y: scroll;
    }
    .sidebar::-webkit-scrollbar{
        width: 0;
    }
    .sidebar-el-menu:not(.el-menu--collapse){
        width: 250px;
    }
    .sidebar > ul {
        height:100%;
    }
</style>
