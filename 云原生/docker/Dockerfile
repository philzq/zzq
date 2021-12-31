# 定制一个基于jdk11的镜像
FROM shinyay/adoptopenjdk11-minimum
# 工作目录
WORKDIR D:\Users\zhiqiangzhou\Desktop\docker\test
# 将运行的文件copy到容器内
COPY spring-boot-test .
# 暴露端口
EXPOSE 8230
# 容器内运行的命令
ENTRYPOINT ["java","-jar","spring-boot-test-0.0.1-SNAPSHOT.jar"]

