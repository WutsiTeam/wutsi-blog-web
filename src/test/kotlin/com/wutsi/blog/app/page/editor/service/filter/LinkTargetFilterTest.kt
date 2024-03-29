package com.wutsi.blog.app.page.editor.service.filter

import org.jsoup.Jsoup
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class LinkTargetFilterTest {
    private val filter = LinkTargetFilter("https://www.wutsi.com")

    @Test
    fun filterExternal() {
        val doc = Jsoup.parse("<body>Hello <a href='https://www.google.ca/foo.html'>world</a></body>")
        filter.filter(doc)

        doc.select("a").forEach {
            assertEquals("_new", it.attr("target"))
        }
    }

    @Test
    fun filterInternal() {
        val doc = Jsoup.parse("<body>Hello <a href='https://www.wutsi.com/foo.html'>world</a></body>")
        filter.filter(doc)

        doc.select("a").forEach {
            assertEquals("", it.attr("target"))
        }
    }
}
