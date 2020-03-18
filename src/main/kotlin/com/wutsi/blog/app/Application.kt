package com.wutsi.blog.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@SpringBootApplication
@EnableWebSecurity
@ComponentScan(value=[
    "com.wutsi.partner",
    "com.wutsi.http",
    "com.wutsi.core"
])
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
