package com.wutsi.blog.app.controller.story

import com.wutsi.blog.app.controller.AbstractPageController
import com.wutsi.blog.app.model.StoryForm
import com.wutsi.blog.app.model.StoryModel
import com.wutsi.blog.app.service.RequestContext
import com.wutsi.blog.app.service.StoryService
import com.wutsi.blog.app.util.PageName
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class EditorController(
        private val service: StoryService,
        requestContext: RequestContext
): AbstractPageController(requestContext) {
    override fun pageName() = PageName.EDITOR

    @GetMapping("/editor")
    fun create(model: Model): String {
        model.addAttribute("storyId", 0)
        return "page/editor"
    }

    @GetMapping("/editor/{id}")
    fun update(@PathVariable id:Long, @RequestParam error:String?=null, model: Model): String {
        model.addAttribute("storyId", id)
        model.addAttribute("error", error)
        return "page/editor"
    }

    @ResponseBody
    @GetMapping("/editor/fetch/{id}", produces = ["application/json"])
    fun fetch(@PathVariable id:Long): StoryModel {
        val story = service.get(id)
        super.checkOwnership(story)

        return story
    }

    @ResponseBody
    @PostMapping("/editor/save", produces = ["application/json"], consumes = ["application/json"])
    fun save(@RequestBody editor: StoryForm): StoryForm {
        return service.save(editor)
    }
}