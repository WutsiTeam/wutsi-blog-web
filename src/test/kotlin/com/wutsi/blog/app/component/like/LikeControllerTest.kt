package com.wutsi.blog.app.component.like

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import com.wutsi.blog.SeleniumMobileTestSupport
import com.wutsi.blog.client.like.CountLikeResponse
import com.wutsi.blog.client.like.CreateLikeResponse
import com.wutsi.blog.client.like.SearchLikeRequest
import com.wutsi.blog.fixtures.LikeApiFixtures
import org.junit.jupiter.api.Test
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

class LikeControllerTest : SeleniumMobileTestSupport() {
    override fun setupWiremock() {
        super.setupWiremock()

        stub(HttpMethod.GET, "/v1/story/20", HttpStatus.OK, "v1/story/get-story20-published.json")
    }

    @Test
    fun `blog page showing like count`() {
        val count = CountLikeResponse(
            counts = listOf(
                LikeApiFixtures.createLikeCountDto(20, 2),
                LikeApiFixtures.createLikeCountDto(21, 1),
                LikeApiFixtures.createLikeCountDto(22, 3)
            )
        )
        doReturn(count).whenever(likeApi).count(any<SearchLikeRequest>())

        driver.get("$url/@/ray.sponsible")

        Thread.sleep(5000)
        assertElementText("#like-count-20", "2")
        assertElementText("#like-count-21", "1")
        assertElementText("#like-count-22", "3")
        assertElementText(".like-badge #like-count-23", "")
        assertElementText(".like-badge #like-count-24", "")
        assertElementText(".like-badge #like-count-25", "")
        assertElementText(".like-badge #like-count-26", "")
    }

    @Test
    fun `user like a story`() {
        login()
        driver.get("$url/read/20/test")

        val create = CreateLikeResponse(likeId = 11)
        doReturn(create).whenever(likeApi).create(any())

        val count = CountLikeResponse(
            counts = listOf(
                LikeApiFixtures.createLikeCountDto(20, 2)
            )
        )
        doReturn(count).whenever(likeApi).count(any())
        click(".like-widget .like-badge")

        Thread.sleep(5000)
        assertElementText(".like-widget .like-badge .like-count", "2")
    }

    @Test
    fun `anonymous like a story`() {
        driver.get("$url/read/20/test")

        val create = CreateLikeResponse(likeId = 11)
        doReturn(create).whenever(likeApi).create(any())

        val count = CountLikeResponse(
            counts = listOf(
                LikeApiFixtures.createLikeCountDto(20, 2)
            )
        )
        doReturn(count).whenever(likeApi).count(any<SearchLikeRequest>())
        click(".like-widget .like-badge")

        Thread.sleep(5000)
        assertElementText(".like-widget .like-badge .like-count", "2")
    }
}
