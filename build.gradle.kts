plugins {
    java
    id("org.springframework.boot") version "3.4.5"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.openapi.generator") version "7.12.0"
}

group = "com"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework:spring-aspects")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("io.swagger.core.v3:swagger-annotations:2.2.30")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.19.0")
    implementation("org.openapitools:jackson-databind-nullable:0.2.6")
    implementation("jakarta.validation:jakarta.validation-api:3.1.1")
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    implementation("io.springfox:springfox-swagger-ui:3.0.0")
    implementation("io.springfox:springfox-swagger2:3.0.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("io.gsonfire:gson-fire:1.9.0")
    implementation("org.liquibase:liquibase-core")
    implementation("org.mapstruct:mapstruct:1.6.3")
    implementation("org.junit.jupiter:junit-jupiter-api:5.12.0")
    implementation("com.google.code.findbugs:jsr305:3.0.2")
    implementation("javax.validation:validation-api:2.0.1.Final")
    implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j:3.2.1")
    implementation("org.springframework.retry:spring-retry:2.0.12")
    implementation("org.springframework.kafka:spring-kafka:3.3.6")


    compileOnly("org.projectlombok:lombok")
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.1.0")

    runtimeOnly("org.postgresql:postgresql")

    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<JavaCompile> {
    dependsOn(tasks.openApiGenerate)
}

sourceSets {
    main {
        java {
            srcDir("$buildDir/generated/openapi/src/main/java")
        }
    }
}
openApiGenerate {
    generatorName.set("spring")
    inputSpec.set("$rootDir/openapi/openapi.yaml")
    outputDir.set("${layout.buildDirectory.get()}/generated/openapi")
    apiPackage.set("org.openapi.template")
    invokerPackage.set("org.openapi.template.invoker")
    modelPackage.set("org.openapi.template.model")
    configOptions.set(
            mapOf(
                    "useSpringBoot3" to "true",
                    "interfaceOnly" to "true",
                    "delegatePattern" to "true",
                    "useTags" to "true",
                    "dateLibrary" to "java8",
                    "useJakartaEE" to "true"
            )
    )
}

tasks.withType<Test> {
    useJUnitPlatform()
}
