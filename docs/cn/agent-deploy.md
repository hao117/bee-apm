### 1、打包
~~~shell
cd bee-apm-agent
mvn package
~~~
打包生成的文件在`/bee-apm/packages`目录里
包含配置文件`config.yml`、`bee-agent.jar`、采集插件`plugins`目录、传输插件`transmit`、依赖lib目录`ext-lib`

### 2、ext-lib
  自行下载servlet-api.jar并复制到/packages/ext-lib目录中

### 3、vm配置
~~~shell
-javaagent:/home/packages/bee-agent.jar
-Dbee.config=/home/packages/xxx.yml
-Dbee.env=xxx
-Dbee.app=xxx
-Dbee.inst=xxx
-Dbee.ip=xxx
-Dbee.port=xxx
~~~
其中`xxx`为根据自己的情况设置相应的值
- `javaagent` 指定`bee-agent.jar`包位置
- `bee.config` 指定配置文件的位置，不配置的话默认和`bee-agent.jar`同级目录的`config.yml`配置文件，如果配置了指定位置，`以指定的配置文件为准`
- `bee.env` 配置环境名称（比如生产prod，测试test，开发dev）
- `bee.app` 配置应用名称或者工程名称（比如应用名称crm）
- `bee.inst` 配置应用实例名称（应用部署了多个实例，比如有三个实例，可以命名为crm01、crm02，crm03）
- `bee.port` 配置应用端口
- `bee.ip` 配置应用主机IP


**备注：**
- 在启动应用之前，记得先启动zookeeper，bee-agent依赖zookeeper来生成id值。
- 调整配置文件`config.yml`里`plugins.process.interceptPoints.typeMatch.include.nameStartsWith`节点的值为您应用的包名，当然您也可以配置其它规则，具体参考配置文件里相关注释说明