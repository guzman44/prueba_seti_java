# Dockerfile
FROM eclipse-temurin:11-jre

# Crear directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el jar construido en el contenedor
COPY target/seti.franquicia-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto 9008
EXPOSE 9008

# Configuracion 
ENV SPRING_PROFILES_ACTIVE=aws

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]