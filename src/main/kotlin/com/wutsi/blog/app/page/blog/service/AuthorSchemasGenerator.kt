package com.wutsi.blog.app.page.blog.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.wutsi.blog.app.page.settings.model.UserModel
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class AuthorSchemasGenerator(
        private val objectMapper: ObjectMapper,

        @Value("\${wutsi.base-url}") private val baseUrl: String
) {

    fun generate(author: UserModel): String {
        val schemas = generateMap(author)
        return objectMapper.writeValueAsString(schemas)
    }

    private fun generateMap(author: UserModel): Map<String, Any> {
        val schemas = mutableMapOf<String, Any>()
        schemas["@context"] = "https://schema.org/"
        schemas["@type"] = "Person"
        schemas["name"] = author.fullName
        schemas["url"] = "${baseUrl}${author.slug}"
        if (author.pictureUrl != null) {
            schemas["image"] = author.pictureUrl
        }
        if (author.hasSocialLinks) {
            schemas["sameAs"] = arrayListOf(
                    author.facebookUrl,
                    author.linkedinUrl,
                    author.youtubeUrl,
                    author.twitterUrl
            ).filter { it != null }
        }
        return schemas
    }
}
