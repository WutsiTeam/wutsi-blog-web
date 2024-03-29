package com.wutsi.blog.app.backend

import com.wutsi.blog.client.story.CountStoryResponse
import com.wutsi.blog.client.story.GetStoryReadabilityResponse
import com.wutsi.blog.client.story.GetStoryResponse
import com.wutsi.blog.client.story.ImportStoryRequest
import com.wutsi.blog.client.story.ImportStoryResponse
import com.wutsi.blog.client.story.PublishStoryRequest
import com.wutsi.blog.client.story.PublishStoryResponse
import com.wutsi.blog.client.story.RecommendStoryRequest
import com.wutsi.blog.client.story.RecommendStoryResponse
import com.wutsi.blog.client.story.SaveStoryRequest
import com.wutsi.blog.client.story.SaveStoryResponse
import com.wutsi.blog.client.story.SearchStoryRequest
import com.wutsi.blog.client.story.SearchStoryResponse
import com.wutsi.blog.client.story.SortStoryRequest
import com.wutsi.blog.client.story.SortStoryResponse
import com.wutsi.blog.client.story.TranslateStoryResponse
import com.wutsi.core.http.Http
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class StoryBackend(
    private val http: Http
) {
    @Value("\${wutsi.backend.story.endpoint}")
    private lateinit var endpoint: String

    fun create(request: SaveStoryRequest): SaveStoryResponse {
        return http.post(endpoint, request, SaveStoryResponse::class.java).body!!
    }

    fun update(id: Long, request: SaveStoryRequest): SaveStoryResponse {
        return http.post("$endpoint/$id", request, SaveStoryResponse::class.java).body!!
    }

    fun get(id: Long): GetStoryResponse {
        return http.get("$endpoint/$id", GetStoryResponse::class.java).body!!
    }

    fun translate(id: Long, language: String): TranslateStoryResponse {
        return http.get("$endpoint/$id/translate?language=$language", TranslateStoryResponse::class.java).body!!
    }

    fun delete(id: Long) {
        http.delete("$endpoint/$id")
    }

    fun readability(id: Long): GetStoryReadabilityResponse {
        return http.get("$endpoint/$id/readability", GetStoryReadabilityResponse::class.java).body!!
    }

    fun search(request: SearchStoryRequest): SearchStoryResponse {
        return http.post("$endpoint/search", request, SearchStoryResponse::class.java).body!!
    }

    fun count(request: SearchStoryRequest): CountStoryResponse {
        return http.post("$endpoint/count", request, CountStoryResponse::class.java).body!!
    }

    fun publish(id: Long, request: PublishStoryRequest): PublishStoryResponse {
        return http.post("$endpoint/$id/publish", request, PublishStoryResponse::class.java).body!!
    }

    fun import(request: ImportStoryRequest): ImportStoryResponse {
        return http.post("$endpoint/import", request, ImportStoryResponse::class.java).body!!
    }

    fun sort(request: SortStoryRequest): SortStoryResponse {
        return http.post("$endpoint/sort", request, SortStoryResponse::class.java).body!!
    }

    fun recommend(request: RecommendStoryRequest): RecommendStoryResponse {
        return http.post("$endpoint/recommend", request, RecommendStoryResponse::class.java).body!!
    }
}
