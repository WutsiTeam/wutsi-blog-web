package com.wutsi.blog.app.controller

import com.wutsi.blog.app.editor.PublishEditor
import com.wutsi.blog.app.service.RequestContext
import com.wutsi.blog.app.service.StoryService
import com.wutsi.blog.app.util.PageName
import com.wutsi.core.exception.ConflictException
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable

@Controller
class StoryPublishController(
        private val service: StoryService,
        requestContext: RequestContext
): AbstractPageController(requestContext) {
    override fun page() = PageName.STORY_PUBLISH

    @GetMapping("/story/{id}/publish")
    fun index(@PathVariable id:Long, model: Model): String {
        val story = service.get(id)

        model.addAttribute("story", story)
        return "page/story/publish"
    }

    @GetMapping("/story/publish/submit")
    fun submit(@ModelAttribute editor: PublishEditor): String {
        try {
            service.publish(editor)
            return "redirect:/story/${editor.id}/confirmation"
        } catch (ex: ConflictException) {
            return "redirect:/story/${editor.id}/editor?error=publish_error"
        }
    }
}