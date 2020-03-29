package com.wutsi.blog.app.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.wutsi.editorjs.html.EJSHtmlReader
import com.wutsi.editorjs.html.EJSHtmlWriter
import com.wutsi.editorjs.html.tag.TagProvider
import com.wutsi.editorjs.json.EJSJsonReader
import com.wutsi.editorjs.json.EJSJsonWriter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class EditorJSConfiguration {
    @Bean
    fun htmlWriter() = EJSHtmlWriter(tagProvider())

    @Bean
    fun htmlReader() = EJSHtmlReader(tagProvider())

    @Bean
    fun jsonReader() = EJSJsonReader(objectMapper())

    @Bean
    fun jsonWriter() = EJSJsonWriter(objectMapper())

    @Bean
    fun tagProvider() = TagProvider()

    @Bean
    fun objectMapper () = ObjectMapper()
}