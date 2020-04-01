package com.wutsi.blog.app.controller

import com.wutsi.blog.app.model.PageModel
import com.wutsi.blog.app.model.StoryModel
import com.wutsi.blog.app.service.RequestContext
import com.wutsi.blog.app.service.StoryService
import com.wutsi.blog.app.util.ModelAttributeName
import com.wutsi.blog.app.util.PageName
import com.wutsi.core.exception.NotFoundException
import com.wutsi.editorjs.html.EJSHtmlWriter
import com.wutsi.editorjs.json.EJSJsonReader
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import java.io.StringWriter

@Controller
class ReadController(
        private val storyService: StoryService,
        private val ejsJsonReader: EJSJsonReader,
        private val ejsHtmlWriter: EJSHtmlWriter,
        requestContext: RequestContext
): AbstractPageController(requestContext) {

    override fun pageName() = PageName.READ

    override fun shouldBeIndexedByBots() = true


    @GetMapping("/read/{id}/{title}")
    fun read(@PathVariable id: Long, @PathVariable title: String, model: Model): String {
        return read(id, model)
    }

    @GetMapping("/read/{id}")
    fun read(@PathVariable id: Long, model: Model): String {
        val story = storyService.get(id)
        model.addAttribute("story", story)
        ensurePublished(story)

        val html = toHtml(story)
        model.addAttribute("html", html)

        model.addAttribute(ModelAttributeName.PAGE, toPage(story))
        return "page/read"
    }

    private fun toHtml(story: StoryModel): String {
        if (story.content == null){
            return ""
        }

        val ejs = ejsJsonReader.read(story.content)
        val html = StringWriter()
        ejsHtmlWriter.write(ejs, html)
        return html.toString()
    }

    private fun ensurePublished(story: StoryModel) {
        if (!story.published){
            throw NotFoundException("story_not_published")
        }
    }

    private fun toPage(story: StoryModel)= PageModel(
            name = pageName(),
            title = story.title!!,
            description = story.summary!!,
            type = "article",
            url = url(story),
            imageUrl = story.thumbmailUrl,
            author = story.user.fullName,
            publishedTime = story.publishedDateTimeISO8601,
            modifiedTime = story.modificationDateTimeISO8601,
            baseUrl = baseUrl,
            robots = robots(),
            tags = story.tags.map { it.name }
    )

}
