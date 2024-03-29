package com.wutsi.blog.app.page.schemas

import com.fasterxml.jackson.databind.ObjectMapper
import com.wutsi.blog.app.page.settings.model.UserModel
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class PersonSchemasGeneratorTest {
    private val generator = PersonSchemasGenerator(
        ObjectMapper(),
        "https://www.wutsi.com"
    )

    @Test
    fun generate() {
        val author = UserModel(
            id = 1L,
            name = "ray.sponsible",
            fullName = "Ray Sponsible",
            slug = "/@/ray.sponsible",
            pictureUrl = "https://www.picture.com/ray.sponsible.png",
            hasSocialLinks = true,
            facebookUrl = "https://www.facebook.com/ray.sponsible",
            twitterUrl = "https://www.twitter.com/ray.sponsible",
            youtubeUrl = "https://www.youtube.com/channel/ray.sponsible",
            linkedinUrl = "https://www.linkedin.com/user/ray.sponsible"
        )
        val json = generator.generate(author)

        val expected = "{" +
            "\"@context\":\"https://schema.org/\"," +
            "\"@type\":\"Person\"," +
            "\"id\":\"https://www.wutsi.com/person/1\"," +
            "\"name\":\"Ray Sponsible\"," +
            "\"image\":\"https://www.picture.com/ray.sponsible.png\"," +
            "\"url\":\"https://www.wutsi.com/@/ray.sponsible\"," +
            "\"sameAs\":[" +
            "\"https://www.facebook.com/ray.sponsible\"," +
            "\"https://www.linkedin.com/user/ray.sponsible\"," +
            "\"https://www.youtube.com/channel/ray.sponsible\"," +
            "\"https://www.twitter.com/ray.sponsible\"" +
            "]}"
        assertEquals(expected, json)
    }
}
