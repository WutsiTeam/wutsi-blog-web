package com.wutsi.blog.app.component.comment

import com.wutsi.blog.SeleniumMobileTestSupport
import org.junit.jupiter.api.Test
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.TestPropertySource

@TestPropertySource(
    properties = [
        "wutsi.toggles.comment=false"
    ]
)
class CommentControllerToggleOffTest : SeleniumMobileTestSupport() {
    override fun setupWiremock() {
        super.setupWiremock()

        stub(HttpMethod.GET, "/v1/story/20", HttpStatus.OK, "v1/story/get-story20-published.json")
    }

    @Test
    fun `home page not showing comment count`() {
        login()
        driver.get(url)

        Thread.sleep(1000)
        assertElementNotPresent("#comment-count-20")
        assertElementNotPresent("#comment-count-21")
        assertElementNotPresent("#comment-count-22")
        assertElementNotPresent("#comment-count-23")
        assertElementNotPresent("#comment-count-24")
        assertElementNotPresent("#comment-count-25")
        assertElementNotPresent("#comment-count-26")
    }

    @Test
    fun `blog page not showing comment count`() {
        driver.get("$url/@/ray.sponsible")

        Thread.sleep(1000)
        assertElementNotPresent("#comment-count-20")
        assertElementNotPresent("#comment-count-21")
        assertElementNotPresent("#comment-count-22")
        assertElementNotPresent("#comment-count-23")
        assertElementNotPresent("#comment-count-24")
        assertElementNotPresent("#comment-count-25")
        assertElementNotPresent("#comment-count-26")
    }

    @Test
    fun `reader without the comment widget`() {
        driver.get("$url/@/ray.sponsible")

        Thread.sleep(1000)
        assertElementNotPresent(".comment-widget")
    }
}
