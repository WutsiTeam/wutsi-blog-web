package com.wutsi.blog.app.page.editor.service

import com.wutsi.blog.app.page.editor.service.link.DefaultLinkExtractor
import com.wutsi.blog.app.page.editor.service.link.YouTubeLinkExtractor
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import kotlin.test.assertTrue

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("qa")
class LinkExtractorProviderTest {
    @Autowired
    lateinit var provider: LinkExtractorProvider

    @Test
    fun youtube() {
        val extractor = provider.get("https://www.youtube.com/watch?v=buS6MIrPBuc")
        assertTrue(extractor is YouTubeLinkExtractor)
    }

    @Test
    fun other() {
        val extractor = provider.get("https://www.google.com")
        assertTrue(extractor is DefaultLinkExtractor)
    }
}
