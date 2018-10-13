## 开发调试
### 1、启动bee-apm-ui-server
   - 执行net.beeapm.ui.WebServerApplication::main
### 2、启动bee-apm-ui-web
   - 执行命令：npm run dev
   - 访问http://localhost:8080

## 部署
 - cd bee-apm-ui/bee-apm-ui-server
 - mvn package
 - 复制bee-apm/bee-apm-ui/bee-apm-ui-server/target/bee-apm-ui.war进行部署
