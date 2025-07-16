FROM eclipse-temurin:21-jdk as build

WORKDIR /app

COPY target/rinha-backend-antonio-0.0.1-SNAPSHOT.jar /app/build/libs/app.jar

# Stage 2: Cria JRE otimizada com jlink
FROM eclipse-temurin:21.0.7_6-jdk-alpine AS jre-build
RUN mkdir -p /opt/server /jre
WORKDIR /jre

COPY --from=build /app/build/libs/app.jar /opt/server/app.jar

# Extrai dependências e módulos necessários via jdeps
RUN jar xf /opt/server/app.jar && \
    jdeps \
    --ignore-missing-deps \
    --print-module-deps \
    --multi-release 21 \
    --recursive \
    --class-path 'BOOT-INF/lib/*' \
    /opt/server/app.jar > modules.txt

# Cria um JRE customizado com apenas os módulos necessários + crypto
RUN $JAVA_HOME/bin/jlink \
    --add-modules $(cat modules.txt),jdk.crypto.ec \
    --strip-debug \
    --no-man-pages \
    --no-header-files \
    --compress=2 \
    --output /javaruntime

# Stage 3: Imagem final minimalista
FROM alpine:3.21.3

# Instala dependências mínimas necessárias
RUN apk --no-cache add libstdc++ ca-certificates tzdata && \
    update-ca-certificates && \
    mkdir -p /opt/java /opt/server

# Copia o JRE customizado e o app Kotlin compilado
COPY --from=jre-build /javaruntime /opt/java/jre
COPY --from=build /app/build/libs/app.jar /opt/server/app.jar

# Configura variáveis de ambiente
ENV JAVA_HOME=/opt/java/jre
ENV PATH="${JAVA_HOME}/bin:${PATH}"
ENV TZ=America/Sao_Paulo

WORKDIR /opt/server
EXPOSE 8080

# Inicia a aplicação Kotlin com configuração de memória eficiente
ENTRYPOINT ["java", \
"-XX:MaxRAMPercentage=95", \
"-XshowSettings:vm",\
"-XX:+UseContainerSupport",\
"-Duser.language=pt",\
"-Xms300m",\
"-Xmx300m",\
"-Duser.country=BR",\
"-jar",\
"app.jar"]

