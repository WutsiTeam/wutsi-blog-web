package com.wutsi.blog.app

import com.wutsi.platform.EnableWutsiCore
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableWebSecurity
@ComponentScan(
    value = [
        "com.wutsi.blog"
    ]
)
@EnableWutsiCore
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
