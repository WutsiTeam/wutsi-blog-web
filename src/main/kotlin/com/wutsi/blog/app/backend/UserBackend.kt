package com.wutsi.blog.app.backend

import com.wutsi.blog.client.user.GetUserResponse
import com.wutsi.blog.client.user.SearchUserRequest
import com.wutsi.blog.client.user.SearchUserResponse
import com.wutsi.http.Http
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class UserBackend (private val http: Http) {
    @Value("\${wutsi.backend.user.endpoint}")
    private lateinit var endpoint: String

    fun get(id:Long): GetUserResponse {
        return http.get("$endpoint/$id", GetUserResponse::class.java).body
    }

    fun search(request: SearchUserRequest): SearchUserResponse {
        return http.post("$endpoint/search", request, SearchUserResponse::class.java).body
    }
}