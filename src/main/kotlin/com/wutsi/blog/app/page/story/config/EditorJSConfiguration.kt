package com.wutsi.blog.app.page.story.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.wutsi.blog.app.page.story.service.HtmlImageService
import com.wutsi.blog.app.page.editor.service.EJSFilterSet
import com.wutsi.blog.app.page.editor.service.ImageKitFilter
import com.wutsi.blog.app.page.editor.service.ImageLozadFilter
import com.wutsi.blog.app.page.editor.service.LinkFilter
import com.wutsi.editorjs.html.EJSHtmlReader
import com.wutsi.editorjs.html.EJSHtmlWriter
import com.wutsi.editorjs.html.tag.TagProvider
import com.wutsi.editorjs.json.EJSJsonReader
import com.wutsi.editorjs.json.EJSJsonWriter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class EditorJSConfiguration(private val objectMapper: ObjectMapper) {
    @Bean
    fun htmlWriter() = EJSHtmlWriter(tagProvider())

    @Bean
    fun htmlReader() = EJSHtmlReader(tagProvider())

    @Bean
    fun jsonReader() = EJSJsonReader(objectMapper)

    @Bean
    fun jsonWriter() = EJSJsonWriter(objectMapper)

    @Bean
    fun tagProvider() = TagProvider()

    @Autowired
    @Bean
    fun ejsFilterSet(imageSize: HtmlImageService) = EJSFilterSet(arrayListOf(
            LinkFilter(),
            ImageKitFilter(imageSize),
            ImageLozadFilter()  /* should be the LAST image filter */
    ))

    @Bean
    fun linkFilter() = LinkFilter()
}