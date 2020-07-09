package com.wutsi.blog.app.page.settings

import com.wutsi.blog.app.controller.AbstractPageController
import com.wutsi.blog.app.model.UserAttributeForm
import com.wutsi.blog.app.service.RequestContext
import com.wutsi.blog.app.service.UserService
import com.wutsi.blog.app.util.PageName
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@RequestMapping("/me/settings")
class SettingsController(
        private val userService: UserService,
        requestContext: RequestContext
): AbstractPageController(requestContext) {
    override fun pageName() = PageName.SETTINGS

    @GetMapping
    fun index(): String {
        return "page/settings/index"
    }

    @ResponseBody
    @PostMapping(produces = ["application/json"], consumes = ["application/json"])
    fun set(@RequestBody request: UserAttributeForm): Map<String, Any?> {
        try {

            userService.set(request)
            return mapOf("id" to requestContext.currentUser()?.id)

        } catch (ex: Exception) {
            val key = errorKey(ex)
            return mapOf(
                    "id" to requestContext.currentUser()?.id,
                    "error" to requestContext.getMessage(key)
            )
        }
    }
}