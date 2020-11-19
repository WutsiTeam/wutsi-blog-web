package com.wutsi.blog.app.page.home

import com.wutsi.blog.app.common.controller.AbstractPageController
import com.wutsi.blog.app.common.service.RequestContext
import com.wutsi.blog.app.page.schemas.WutsiSchemasGenerator
import com.wutsi.blog.app.util.PageName
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/")
class HomeController(
        private val schemas: WutsiSchemasGenerator,
        requestContext: RequestContext
): AbstractPageController(requestContext) {
    override fun pageName() = PageName.HOME

    override fun shouldBeIndexedByBots() = true

    override fun shouldShowGoogleOneTap() = true

    override fun page() = createPage(
            title = requestContext.getMessage("page.home.metadata.title"),
            description = requestContext.getMessage("page.home.metadata.description"),
            schemas = schemas.generate(),
            showNotificationOptIn = true,
            rssUrl = "/rss"
    )

    @GetMapping()
    fun index(model: Model): String {
        return "page/home/index"
    }
}
