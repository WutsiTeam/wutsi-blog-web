package com.wutsi.blog.app.controller.user

import com.wutsi.blog.SeleniumTestSupport
import com.wutsi.blog.app.util.PageName
import org.junit.Test
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

class BlogControllerTest: SeleniumTestSupport() {
    override fun setupWiremock() {
        super.setupWiremock()

        stub(HttpMethod.GET, "/v1/user/@/ray.sponsible", HttpStatus.OK, "v1/user/get-user1.json")
    }

    @Test
    fun `blog page` () {
        gotoPage()

        assertElementAttribute(".author img", "src", "https://avatars3.githubusercontent.com/u/39621277?v=4")
        assertElementAttributeEndsWith(".author a", "href", "/@/ray.sponsible")
        assertElementText(".author p", "Ray sponsible is a test user")

        assertElementCount(".post", 4)
        assertElementNotPresent("#welcome")
        assertElementNotPresent("#create-first-story")
    }

    @Test
    fun `empty blog page` () {
        stub(HttpMethod.GET, "/v1/user/@/ray.sponsible", HttpStatus.OK, "v1/user/get-user99.json")
        stub(HttpMethod.POST, "/v1/story/search", HttpStatus.OK, "v1/story/search-empty.json")

        gotoPage()

        assertElementCount(".post", 0)
        assertElementNotPresent("#welcome")
        assertElementNotPresent("#create-first-story")
    }

    @Test
    fun `welcome new user` () {
        stub(HttpMethod.GET, "/v1/user/@/ray.sponsible", HttpStatus.OK, "v1/user/get-user1-first-login.json")
        stub(HttpMethod.POST, "/v1/story/search", HttpStatus.OK, "v1/story/search-empty.json")

        gotoPage(true)

        assertElementCount(".post", 0)

        Thread.sleep(5000)
        assertElementPresent("#welcome")
        assertElementAttributeEndsWith("#btn-create-story", "href", "/editor")
        assertElementAttributeEndsWith("#btn-syndicate-story", "href", "/me/syndicate")

        assertElementNotPresent("#create-first-story")
    }

    @Test
    fun `MY empty blog page` () {
        stub(HttpMethod.POST, "/v1/story/search", HttpStatus.OK, "v1/story/search-empty.json")
        gotoPage(true)

        assertElementCount(".post", 0)

        assertElementNotPresent("#welcome")

        assertElementPresent("#create-first-story")
        assertElementAttributeEndsWith("#btn-create-story", "href", "/editor")
        assertElementAttributeEndsWith("#btn-syndicate-story", "href", "/me/syndicate")
    }

    @Test
    fun `META headers`() {
        gotoPage()

        val title = "Ray Sponsible"
        val description = "Ray sponsible is a test user"

        assertElementAttribute("head title", "text", title)
        assertElementAttribute("head meta[name='description']", "content", description)
        assertElementAttribute("head meta[name='robots']", "content", "all")

        assertElementAttribute("head meta[property='og:title']", "content", title)
        assertElementAttribute("head meta[property='og:description']","content", description)
        assertElementAttribute("head meta[property='og:type']", "content", "profile")
        assertElementAttribute("head meta[property='og:url']", "content", "http://localhost:8081/@/ray.sponsible")
        assertElementAttribute("head meta[property='og:image']", "content", "https://avatars3.githubusercontent.com/u/39621277?v=4")
        assertElementAttribute("head meta[property='og:site_name']", "content", "Wutsi")
    }

    fun gotoPage(login: Boolean = false) {
        if (login) {
            login()
            click("nav .nav-item")
            click("nav .dropdown-item-user a")
        } else {
            driver.get("$url/@/ray.sponsible")
        }


        assertCurrentPageIs(PageName.BLOG)
    }
}