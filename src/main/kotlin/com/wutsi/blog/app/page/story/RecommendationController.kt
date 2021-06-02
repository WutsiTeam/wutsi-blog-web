package com.wutsi.blog.app.page.story

import com.wutsi.blog.app.common.controller.AbstractPageController
import com.wutsi.blog.app.common.service.RequestContext
import com.wutsi.blog.app.page.story.service.RecentViewsService
import com.wutsi.blog.app.page.story.service.RecommendationService
import com.wutsi.blog.app.page.story.service.StoryService
import com.wutsi.blog.app.util.PageName
import com.wutsi.blog.client.story.SearchStoryRequest
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class RecommendationController(
    private val storyService: StoryService,
    private val recommendationService: RecommendationService,
    private val recentViewsService: RecentViewsService,
    requestContext: RequestContext
) : AbstractPageController(requestContext) {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(RecommendationController::class.java)
    }

    override fun pageName() = PageName.RECOMMEND

    @GetMapping("/recommend")
    fun recommend(
        @RequestParam storyId: Long,
        @RequestParam(required = false, defaultValue = "summary") layout: String = "summary",
        model: Model
    ): String {
        try {
            similarityRecommender(storyId, model)
            model.addAttribute("layout", layout)
        } catch (ex: Exception) {
            LOGGER.warn("Unable to find Story recommendations", ex)
        }
        return "page/story/recommend"
    }

    fun similarityRecommender(storyId: Long, model: Model) {
        val limit = 10
        val similarStoryIds: List<Long> = recommendationService.similar(storyId, 2 * limit)
        if (similarStoryIds.isEmpty())
            return

        // Bubble down viewed stories
        val viewedStoryIds: List<Long> = recentViewsService.get()
        val recommendIds = merge(similarStoryIds, viewedStoryIds, limit)

        // Fetch the stories
        val storyIds = recommendIds.toMutableList()
        if (!recommendIds.contains(storyId))
            storyIds.add(storyId)
        val stories = storyService.search(
            SearchStoryRequest(
                storyIds = storyIds,
                limit = storyIds.size
            )
        ).map { it.id to it }.toMap()

        model.addAttribute("stories", recommendIds.map { stories[it] }.filterNotNull())
        model.addAttribute("blog", stories[storyId]?.user)
    }

    private fun merge(similarStoryIds: List<Long>, viewedStoryIds: List<Long>, limit: Int): List<Long> {
        val ids = similarStoryIds.toMutableList()
        ids.removeAll(viewedStoryIds)
        ids.addAll(viewedStoryIds)
        return ids.take(limit)
    }
}
