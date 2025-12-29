# 基础镜像
FROM eclipse-temurin:21-jre-alpine

# 1. 先换源（加速下载）并安装时区
# 将这两行放在最前面，这样这一层会被所有微服务共享缓存，只需执行一次
RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories && \
    apk add --no-cache tzdata && \
    cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone

# 设置工作目录
WORKDIR /app

# 2. 定义构建参数
ARG JAR_FILE

# 3. 最后再复制 Jar 包（这是唯一变化的部分）
COPY ${JAR_FILE} app.jar

EXPOSE 8080

# 启动命令
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]