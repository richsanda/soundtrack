FROM openjdk:8-jdk-alpine
EXPOSE 8080
ADD /target/soundtrack.jar soundtrack.jar
ENTRYPOINT [ \
"java", \
"-jar", \
"-Dspring.profiles.active=dev", \
"soundtrack.jar" \
]