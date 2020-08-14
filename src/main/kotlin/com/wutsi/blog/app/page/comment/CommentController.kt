package com.wutsi.blog.app.page.comment

import com.wutsi.blog.app.common.controller.AbstractPageController
import com.wutsi.blog.app.common.service.RequestContext
import com.wutsi.blog.app.page.comment.model.CreateCommentForm
import com.wutsi.blog.app.page.comment.service.CommentService
import com.wutsi.blog.app.page.story.service.StoryService
import com.wutsi.blog.app.util.PageName
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@RequestMapping("/comment")
class CommentController(
        private val comments: CommentService,
        private val stories: StoryService,
        requestContext: RequestContext
): AbstractPageController(requestContext) {
    override fun pageName() = PageName.COMMENT

    @GetMapping()
    fun index(
            @RequestParam storyId: Long,
            model: Model
    ): String {
        val story = stories.get(storyId)
        model.addAttribute("story", story)

        return "page/comment/index"
    }

    @GetMapping("/list")
    fun list(
            @RequestParam storyId: Long,
            @RequestParam(required = false, defaultValue = "50") limit: Int=50,
            @RequestParam(required = false, defaultValue = "0") offset: Int = 0,
            model: Model
    ): String {
        val items = comments.list(storyId, limit, offset)
        model.addAttribute("comments", items)

        return "page/comment/list"
    }

    @ResponseBody
    @PostMapping(produces = ["application/json"], consumes = ["application/json"])
    fun create(@RequestBody form: CreateCommentForm): Map<String, Long> {
        val id = comments.create(form)
        return mapOf("commentId" to id)
    }
}
