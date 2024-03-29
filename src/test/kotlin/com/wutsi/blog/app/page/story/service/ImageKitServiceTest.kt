package com.wutsi.blog.app.page.story.service

import com.wutsi.blog.app.common.service.ImageKitService
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ImageKitServiceTest {
    val service = ImageKitService(true, "http://www.google.com", "http://www.imagekit.io/43043094")

    @Test
    fun transformWidthAndHeight() {
        val url = "http://www.google.com/img/a/b/1.png"
        val result = service.transform(url, "200px", "150px")

        assertEquals("http://www.imagekit.io/43043094/img/a/b/tr:w-200px,h-150px/1.png", result)
    }

    @Test
    fun transformWidth() {
        val url = "http://www.google.com/img/a/b/1.png"
        val result = service.transform(url, "200px")

        assertEquals("http://www.imagekit.io/43043094/img/a/b/tr:w-200px/1.png", result)
    }

    @Test
    fun transformHeight() {
        val url = "http://www.google.com/img/a/b/1.png"
        val result = service.transform(url, null, "150px")

        assertEquals("http://www.imagekit.io/43043094/img/a/b/tr:h-150px/1.png", result)
    }

    @Test
    fun transformWithFocus() {
        val url = "http://www.google.com/img/a/b/1.png"
        val result = service.transform(url, "400px", "150px", true)

        assertEquals("http://www.imagekit.io/43043094/img/a/b/tr:w-400px,h-150px,fo-auto/1.png", result)
    }

    @Test
    fun transformNone() {
        val url = "http://www.google.com/img/a/b/1.png"
        val result = service.transform(url)

        assertEquals("http://www.imagekit.io/43043094/img/a/b/1.png", result)
    }

    @Test
    fun transformInvalidOrigin() {
        val url = "http://www.yo.com/img/a/b/1.png"
        val result = service.transform(url)

        assertEquals(url, result)
    }

    @Test
    fun notEnabled() {
        val service = ImageKitService(false, "http://www.google.com", "http://www.imagekit.io/43043094")
        val url = "http://www.google.com/img/a/b/1.png"
        val result = service.transform(url, "100", "200")

        assertEquals(url, result)
    }
}
