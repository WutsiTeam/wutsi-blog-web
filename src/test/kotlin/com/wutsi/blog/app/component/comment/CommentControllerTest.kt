package com.wutsi.blog.app.component.comment

import com.wutsi.blog.SeleniumMobileTestSupport
import com.wutsi.blog.app.util.PageName
import org.junit.Test
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus


class CommentControllerTest: SeleniumMobileTestSupport() {
    override fun setupWiremock() {
        super.setupWiremock()

        stub(HttpMethod.POST, "/v1/comment/search", HttpStatus.OK, "v1/comment/search.json")
        stub(HttpMethod.POST, "/v1/comment", HttpStatus.OK, "v1/comment/create.json")

        stub(HttpMethod.GET, "/v1/story/20", HttpStatus.OK, "v1/story/get-story20-published.json")

        stub(HttpMethod.POST, "/v1/recommendation/search", HttpStatus.OK, "v1/recommendation/search.json")

        stub(HttpMethod.GET, "/v1/user/@/ray.sponsible", HttpStatus.OK, "v1/user/get-user1.json")
    }

    @Test
    fun `home page showing comment count` () {
        driver.get(url)

        Thread.sleep(1000)
        assertElementCount(".comment-badge", 7)
        assertElementText(".comment-badge #comment-count-20", "3")
        assertElementText(".comment-badge #comment-count-21", "5")
        assertElementText(".comment-badge #comment-count-22", "1")
        assertElementText(".comment-badge #comment-count-23", "")
        assertElementText(".comment-badge #comment-count-24", "")
        assertElementText(".comment-badge #comment-count-25", "")
        assertElementText(".comment-badge #comment-count-26", "")
    }

    @Test
    fun `blog page showing comment count` () {
        driver.get("$url/@/ray.sponsible")

        Thread.sleep(1000)
        assertElementText(".comment-badge #comment-count-20", "3")
        assertElementText(".comment-badge #comment-count-21", "5")
        assertElementText(".comment-badge #comment-count-22", "1")
        assertElementText(".comment-badge #comment-count-23", "")
        assertElementText(".comment-badge #comment-count-24", "")
        assertElementText(".comment-badge #comment-count-25", "")
        assertElementText(".comment-badge #comment-count-26", "")
    }

    @Test
    fun `reader has comment icon with count`() {
        gotoPage()

        Thread.sleep(1000)
        assertElementPresent(".comment-widget .comment-badge .fa-comment-alt")
        assertElementText(".comment-widget .comment-badge .comment-count", "3")
        assertElementNotVisible(".comment-widget .comment-widget-content")
    }

    @Test
    fun `reader has comment icon`() {
        stub(HttpMethod.POST, "/v1/comment/count", HttpStatus.OK, "v1/comment/count_0.json")
        gotoPage()

        Thread.sleep(1000)
        assertElementPresent(".comment-widget .comment-badge .fa-comment-alt")
        assertElementText(".comment-widget .comment-badge .comment-count", "")
        assertElementNotVisible(".comment-widget .comment-widget-content")
    }

    @Test
    fun `comment pane opens when query parameter sent to reader`() {
        driver.get("$url/read/20/test?comment=1")

        Thread.sleep(1000)
        assertElementVisible(".comment-widget .comment-widget-content")
        assertElementCount(".comment-widget .comment", 3)
    }

    @Test
    fun `comment pane does not open when query parameter not sent to reader`() {
        driver.get("$url/read/20/test")

        Thread.sleep(1000)
        assertElementNotVisible(".comment-widget .comment-widget-content")
    }

    @Test
    fun `user add a comment`() {
        gotoPage(true)

        click(".comment-widget .comment-badge")

        Thread.sleep(3000)
        assertElementVisible(".comment-widget .comment-widget-content")
        assertElementCount(".comment-widget .comment", 3)

        click(".comment-widget textarea")
        input(".comment-widget  textarea", "new comment")

        stub(HttpMethod.POST, "/v1/comment/search", HttpStatus.OK, "v1/comment/search_4_comments.json")
        stub(HttpMethod.POST, "/v1/comment/count", HttpStatus.OK, "v1/comment/count_4.json")
        click(".comment-widget .btn-submit")

        Thread.sleep(1000)
        assertElementCount(".comment-widget .comment", 4)

        click(".comment-widget .close")
        Thread.sleep(1000)
        assertElementNotVisible(".comment-widget .comment-widget-content")
        assertElementText(".comment-widget .comment-badge .comment-count", "4")
    }

    @Test
    fun `anonymous is redirected to login when adding a comment`() {
        gotoPage()

        click(".comment-widget .comment-badge")

        Thread.sleep(1000)
        assertElementVisible(".comment-widget .comment-widget-content")
        click(".comment-widget textarea")

        assertCurrentPageIs(PageName.LOGIN)
    }

    fun gotoPage(login: Boolean = false) {
        if (login){
            login()
        }

        driver.get(url)
        click(".post a")
    }
}