package com.wutsi.blog.app.page.rss

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import com.rometools.rome.feed.rss.Channel
import com.wutsi.blog.SeleniumTestSupport
import com.wutsi.blog.client.user.SearchUserResponse
import com.wutsi.blog.fixtures.UserApiFixtures
import org.junit.jupiter.api.Test
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.web.client.RestTemplate
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BlogRssControllerTest : SeleniumTestSupport() {
    val rest = RestTemplate()

    override fun setupWiremock() {
        super.setupWiremock()

        stub(HttpMethod.POST, "/v1/story/search", HttpStatus.OK, "v1/story/search-user3.json")
    }

    @Test
    fun `return RSS for user`() {
        val users = listOf(
            UserApiFixtures.createUserSummaryDto(3, "roger.milla", "Roger Milla")
        )
        doReturn(SearchUserResponse(users = users)).whenever(userApi).search(any())

        val response = rest.getForEntity("$url/@/roger.milla/rss", Channel::class.java)
        assertEquals(HttpStatus.OK, response.statusCode)

        validateRSS(response.body!!)
    }

    @Test
    fun `return RSS for user by date range`() {
        val response =
            rest.getForEntity("$url/@/roger.milla/rss?startDate=2020-12-01&endDate=2020-12-10", Channel::class.java)
        assertEquals(HttpStatus.OK, response.statusCode)

        validateRSS(response.body!!)
    }

    private fun validateRSS(channel: Channel) {
        assertEquals("Roger Milla(@roger.milla) RSS Feed", channel.title)
        assertEquals("Just the best african soccer player ever!", channel.description)
        assertTrue(channel.link.endsWith("/@/roger.milla"))

        assertEquals(2, channel.items.size)

        assertEquals("Cameroon-Argentina", channel.items[0].title)
        assertEquals("Passe decisive: 1-0", channel.items[0].description.value)
        assertEquals("Roger Milla", channel.items[0].author)
        assertTrue(channel.items[0].link.endsWith("/read/20/cameroon-argentina"))
        assertEquals(1, channel.items[0].enclosures.size)
        assertEquals("image/jpeg", channel.items[0].enclosures[0].type)
        assertEquals(
            "https://images.pexels.com/photos/2167395/pexels-photo-2167395.jpeg",
            channel.items[0].enclosures[0].url
        )

        assertEquals("Cameroon Rumania", channel.items[1].title)
        assertEquals("2 buts de Rogers", channel.items[1].description.value)
        assertEquals("Roger Milla", channel.items[1].author)
        assertTrue(channel.items[1].link.endsWith("/read/21/cameroon-rumania"))
        assertTrue(channel.items[1].enclosures.isEmpty())
    }
}
