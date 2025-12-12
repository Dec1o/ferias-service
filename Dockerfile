# Etapa 1: Build da aplicação
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copia os arquivos de dependências primeiro (melhor cache)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o código fonte e faz o build
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Imagem final otimizada
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Cria um usuário não-root para segurança
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copia o JAR da etapa de build
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta da aplicação
EXPOSE 8080

# Configurações JVM otimizadas
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Comando de inicialização
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]