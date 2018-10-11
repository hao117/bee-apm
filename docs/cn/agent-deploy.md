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
-Dbee.server= xxx
-Dbee.port= xxx
-Dbee.cluster= xxx
~~~
其中`xxx`为根据自己的情况设置相应的值
`bee.server`配置应用名称
`bee.port`配置应用容器端口号
`bee.cluster`配置应用集群名称