package com.wutsi.blog.app.scenarios

import com.wutsi.blog.SeleniumTestSupport
import com.wutsi.blog.app.util.PageName
import org.junit.Test
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus


class ReadControllerTest: SeleniumTestSupport() {
    companion object {
        private const val PUBLISHED_ID = "10"
        private const val DRAFT_ID = "20"
    }

    override fun setupWiremock() {
        stub(HttpMethod.GET, "/v1/story/$PUBLISHED_ID", HttpStatus.OK, "v1/story/get_published.json")
        stub(HttpMethod.GET, "/v1/story/$DRAFT_ID", HttpStatus.OK, "v1/story/get_draft.json")

        stub(HttpMethod.GET, "/v1/user/[0-9]+", HttpStatus.OK, "v1/user/get.json")
    }


    @Test
    fun `user should read published story`() {
        driver?.get("$url/read/$PUBLISHED_ID/looks-good")

        assertCurrentPageIs(PageName.READ)
    }

    @Test
    fun `user should be redirected to 404 when he try to read draft story`() {
        driver?.get("$url/read/$DRAFT_ID/looks-good")

        assertCurrentPageIs(PageName.ERROR_404)
    }

    @Test
    fun `user should be redirected to 404 when he try to read invalid story`() {
        driver?.get("$url/read/9999999/looks-good")

        assertCurrentPageIs(PageName.ERROR_404)
    }

    @Test
    fun `user should share they story on Facebook`() {
        driver?.get("$url/read/$PUBLISHED_ID/looks-good")

        val url = "http://localhost:8081/read/$PUBLISHED_ID/lorem-ipsum"

        assertElementCount(".share .share-facebook", 2)
        assertElementAttribute(".share .share-facebook", "href", "https://www.facebook.com/sharer/sharer.php?display=page&u=$url")
        assertElementAttribute(".share .share-facebook", "target", "_blank")
    }

    @Test
    fun `user should share they story on Twitter`() {
        driver?.get("$url/read/$PUBLISHED_ID/lorem-ipsum")

        val url = "http://localhost:8081/read/$PUBLISHED_ID/lorem-ipsum"

        assertElementCount(".share .share-twitter", 2)
        assertElementAttribute(".share .share-twitter", "href", "http://www.twitter.com/intent/tweet?url=$url")
        assertElementAttribute(".share .share-facebook", "target", "_blank")
    }

    @Test
    fun `user should share they story on LinkedIn`() {
        driver?.get("$url/read/$PUBLISHED_ID/lorem-ipsum")

        val url = "http://localhost:8081/read/$PUBLISHED_ID/lorem-ipsum"

        assertElementCount(".share .share-linkedin", 2)
        assertElementAttribute(".share .share-linkedin", "href", "https://www.linkedin.com/shareArticle?mini=true&url=$url")
    }

    @Test
    fun `article should contains META headers`() {
        driver?.get("$url/read/$PUBLISHED_ID/looks-good")

        val title = "Lorem Ipsum"
        val description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry"

        assertElementAttribute("head base", "href", "http://localhost:8081/")
        assertElementAttribute("head title", "text", title)
        assertElementAttribute("head meta[name='description']", "content", description)
        assertElementAttribute("head meta[name='robots']", "content", "all")

        assertElementAttribute("head meta[property='og:title']", "content", title)
        assertElementAttribute("head meta[property='og:description']","content", description)
        assertElementAttribute("head meta[property='og:type']", "content", "article")
        assertElementAttribute("head meta[property='og:url']", "content", "http://localhost:8081/read/10/lorem-ipsum")
        assertElementAttribute("head meta[property='og:image']", "content", "https://images.pexels.com/photos/2167395/pexels-photo-2167395.jpeg")
        assertElementAttribute("head meta[property='og:site_name']", "content", "Wutsi")
        assertElementAttribute("head meta[property='article:author']", "content", "Ray Sponsible")
        assertElementAttributeStartsWith("head meta[property='article:modified_time']", "content", "2020-03-27T")
        assertElementAttributeStartsWith("head meta[property='article:published_time']", "content", "2020-03-27T")
        assertElementCount("head meta[property='article:tag']", 3)

        assertElementAttribute("head meta[name='twitter:card']", "content", "summary")
        assertElementAttribute("head meta[name='twitter:site']", "content", "@raysponsible")
        assertElementAttribute("head meta[name='twitter:creator']", "content", "@raysponsible")

    }
}
