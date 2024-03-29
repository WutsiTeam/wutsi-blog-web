package com.wutsi.blog.app.page.story.service

import com.wutsi.blog.app.page.story.model.TagModel
import com.wutsi.blog.sdk.TagApi
import org.springframework.stereotype.Service

@Service
class TagService(
    private val api: TagApi,
    private val mapper: TagMapper
) {
    fun search(query: String): List<TagModel> {
        val tags = api.search(query).tags
        return tags.map { mapper.toTagModel(it) }
    }
}
