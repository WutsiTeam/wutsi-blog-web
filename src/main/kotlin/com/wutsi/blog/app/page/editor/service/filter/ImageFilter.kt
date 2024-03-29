package com.wutsi.blog.app.page.editor.service.filter

import com.wutsi.blog.app.common.service.ImageKitService
import com.wutsi.blog.app.common.service.RequestContext
import com.wutsi.blog.app.page.editor.service.Filter
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class ImageFilter(
    private val imageKitService: ImageKitService,
    private val requestContext: RequestContext,
    private val desktopThumbnailLargeWidth: Int,
    private val mobileThumbnailLargeWidth: Int
) : Filter {
    override fun filter(html: Document) {
        html.select("img")
            .forEach {
                filter(it)
            }
    }

    private fun filter(img: Element) {
        img.attr("loading", "lazy")

        if (requestContext.isMobileUserAgent()) {
            filter(img, mobileThumbnailLargeWidth)
        } else {
            filter(img, desktopThumbnailLargeWidth)
        }
    }

    private fun filter(img: Element, maxWidth: Int) {
        val width = attrAsInt(img, "width")
        if (width > maxWidth) {
            val url = img.attr("src")
            img.attr("src", imageKitService.transform(url, width = maxWidth.toString()))
            img.attr("width", maxWidth.toString())
            img.removeAttr("height")
        }
    }

    private fun attrAsInt(elt: Element, name: String): Int {
        try {
            return elt.attr(name).toInt()
        } catch (ex: Exception) {
            return 0
        }
    }
}
