package com.wutsi.blog.app.page.story.service

import com.wutsi.blog.app.backend.SortBackend
import com.wutsi.blog.app.backend.StoryBackend
import com.wutsi.blog.app.page.editor.model.PublishForm
import com.wutsi.blog.app.page.editor.model.ReadabilityModel
import com.wutsi.blog.app.page.story.model.StoryForm
import com.wutsi.blog.app.page.story.model.StoryModel
import com.wutsi.blog.app.model.UserModel
import com.wutsi.blog.app.service.RequestContext
import com.wutsi.blog.app.service.UserService
import com.wutsi.blog.client.story.ImportStoryRequest
import com.wutsi.blog.client.story.PublishStoryRequest
import com.wutsi.blog.client.story.SaveStoryRequest
import com.wutsi.blog.client.story.SaveStoryResponse
import com.wutsi.blog.client.story.SearchStoryRequest
import com.wutsi.blog.client.story.SortAlgorithmType
import com.wutsi.blog.client.story.SortStoryRequest
import com.wutsi.blog.client.story.StoryStatus
import com.wutsi.blog.client.story.StorySummaryDto
import com.wutsi.blog.client.user.SearchUserRequest
import com.wutsi.editorjs.html.EJSHtmlWriter
import com.wutsi.editorjs.json.EJSJsonReader
import org.jsoup.Jsoup
import org.springframework.stereotype.Service
import java.io.StringWriter

@Service
class StoryService(
        private val requestContext: RequestContext,
        private val mapper: StoryMapper,
        private val storyBackend: StoryBackend,
        private val sortBackend: SortBackend,
        private val ejsJsonReader: EJSJsonReader,
        private val ejsHtmlWriter: EJSHtmlWriter,
        private val userService: UserService
) {
    fun save(editor: StoryForm): StoryForm {
        var response = SaveStoryResponse()
        val request = toSaveStoryRequest(editor)
        if (shouldCreate(editor)){
            response = storyBackend.create(request)
        } else if (shouldUpdate(editor)) {
            response = storyBackend.update(editor.id!!, request)
        }

        return StoryForm(
                id = response.storyId,
                title = editor.title,
                content = editor.content
        )
    }

    fun get(id: Long): StoryModel {
        val story = storyBackend.get(id).story
        val user = userService.get(story.userId)
        return mapper.toStoryModel(story, user)
    }

    fun search(request: SearchStoryRequest): List<StoryModel> {
        val stories = storyBackend.search(request).stories
        if (stories.isEmpty()){
            return emptyList()
        }

        val users = searchUserMap(stories)
        return stories.map { mapper.toStoryModel(it, users[it.userId]) }
    }

    fun sort(stories: List<StoryModel>, algorithm: SortAlgorithmType, statsHoursOffset: Int, bubbleDownViewedStories:Boolean = true): List<StoryModel> {
        if (stories.size <= 1) {
            return stories
        }

        val response = sortBackend.sort(SortStoryRequest(
                storyIds = stories.map { it.id },
                bubbleDownViewedStories =  bubbleDownViewedStories,
                userId = requestContext.currentUser()?.id,
                algorithm = algorithm,
                statsHoursOffset = statsHoursOffset
        ))
        val storyMap = stories.map { it.id to it }.toMap()
        return response.storyIds
                .map { storyMap[it] }
                .filter { it != null } as List<StoryModel>

    }

    fun publish(editor: PublishForm){
        storyBackend.publish(editor.id, PublishStoryRequest(
                topidId = editor.topicId.toLong(),
                tags = editor.tags
        ))
    }

    fun count(status: StoryStatus): Int {
        val request = SearchStoryRequest(
                userId = requestContext.currentUser()?.id,
                status = status,
                limit = Int.MAX_VALUE
        )
        return storyBackend.count(request).total
    }

    fun import(url: String): Long {
        val request = ImportStoryRequest(
                url = url,
                accessToken = requestContext.accessToken()
        )
        return storyBackend.import(request).storyId
    }

    fun readability(id: Long): ReadabilityModel {
        val result = storyBackend.readability(id).readability
        return mapper.toReadabilityModel(result)
    }

    private fun shouldUpdate(editor: StoryForm) =  editor.id != null && editor.id > 0L

    private fun shouldCreate(editor: StoryForm) = (editor.id == null || editor.id == 0L) && !isEmpty(editor)

    private fun isEmpty(editor: StoryForm): Boolean {
        if (editor.title.trim().isNotEmpty()){
            return false
        }

        val doc = ejsJsonReader.read(editor.content)
        val html = StringWriter()
        ejsHtmlWriter.write(doc, html)
        return Jsoup.parse(html.toString()).body().text().trim().isEmpty()
    }

    private fun toSaveStoryRequest(editor: StoryForm) = SaveStoryRequest(
            contentType = "application/editorjs",
            content = editor.content,
            title = editor.title,
            accessToken = requestContext.accessToken()
    )


    private fun searchUserMap(stories: List<StorySummaryDto>): Map<Long, UserModel?> {
        val userIds = stories.map { it.userId }.toSet().toList()
        return userService.search(SearchUserRequest(
                userIds = userIds,
                limit = userIds.size,
                offset = 0
        ))
                .map { it.id to it }
                .toMap()
    }
}
