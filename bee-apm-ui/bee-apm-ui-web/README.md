# BeeAPM


## 安装步骤 ##

	cd bee-apm-ui-web   // 进入目录
	npm install         // 安装项目依赖，等待安装完成之后

## 本地开发 ##

	// 开启服务器，浏览器访问 http://localhost:8080
	npm run dev

## 构建生产 ##

	// 执行构建命令，生成的dist文件夹放在服务器下即可访问
	npm run build


### 增加菜单

举个栗子

第一步：增加路由，在目录 src/router/index.js 中，引入该菜单的路由

```JavaScript
{
    // 富文本编辑器组件
    path: '/editor',
    component: resolve => require(['../components/page/VueEditor.vue'], resolve)
},
```

第二步：引入该菜单的文件。在目录 src/components/page/ 增加 VueEditor.vue 文件。

第三步：增加该页面的入口。在目录 src/components/common/Sidebar.vue 中，添加下面这段代码。

```js
{
	index: 'editor',
	title: '富文本编辑器'
},
```
