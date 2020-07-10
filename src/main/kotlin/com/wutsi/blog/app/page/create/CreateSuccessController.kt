package com.wutsi.blog.app.page.create

import com.wutsi.blog.app.common.controller.AbstractPageController
import com.wutsi.blog.app.common.service.RequestContext
import com.wutsi.blog.app.util.PageName
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/create/success")
class CreateSuccessController(
        requestContext: RequestContext
): AbstractPageController(requestContext) {
    override fun pageName() = PageName.CREATE_SUCCESS

    @GetMapping
    fun index(): String {
        return "page/create/success"
    }

}
