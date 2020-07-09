package com.wutsi.blog.app.page.editor

import com.wutsi.blog.app.model.Permission
import com.wutsi.blog.app.page.story.AbstractStoryController
import com.wutsi.blog.app.page.story.service.StoryService
import com.wutsi.blog.app.page.story.service.TopicService
import com.wutsi.blog.app.service.RequestContext
import com.wutsi.blog.app.util.PageName
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class EditorShareController(
        private val topicService: TopicService,
        service: StoryService,
        requestContext: RequestContext,

        @Value("\${wutsi.base-url}") private val websiteUrl: String
): AbstractStoryController(service, requestContext) {
    override fun pageName() = PageName.EDITOR_SHARE

    override fun requiredPermissions() = listOf(Permission.editor)

    @GetMapping("/me/story/{id}/share")
    fun index(
            @PathVariable id:Long,
            model: Model
    ): String {
        val story = getStory(id)

        model.addAttribute("story", story)
        model.addAttribute("storyUrl", "${websiteUrl}${story.slug}")
        return "page/editor/share"
    }
}