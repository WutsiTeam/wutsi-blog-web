package com.wutsi.blog.app.page.story

import com.wutsi.blog.app.common.service.RequestContext
import com.wutsi.blog.app.page.follower.service.FollowerService
import com.wutsi.blog.app.page.story.service.StoryService
import com.wutsi.blog.app.security.model.Permission
import com.wutsi.blog.app.util.PageName
import com.wutsi.editorjs.json.EJSJsonReader
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class StoryPreviewController(
    ejsJsonReader: EJSJsonReader,
    followerService: FollowerService,
    service: StoryService,
    requestContext: RequestContext
) : AbstractStoryReadController(ejsJsonReader, followerService, service, requestContext) {

    override fun pageName() = PageName.STORY_PREVIEW

    override fun requiredPermissions() = listOf(Permission.previewer)

    @GetMapping("/me/story/{id}/preview")
    fun preview(@PathVariable id: Long, model: Model): String {
        loadPage(id, model)
        model.addAttribute("preview", true)
        return "page/story/read"
    }
}
