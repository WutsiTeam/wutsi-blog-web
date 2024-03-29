package com.wutsi.blog.app.page.story

import com.wutsi.blog.SeleniumMobileTestSupport
import com.wutsi.blog.app.util.PageName
import com.wutsi.blog.fixtures.UserApiFixtures
import org.junit.jupiter.api.Test
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

class ReadControllerTest : SeleniumMobileTestSupport() {
    override fun setupWiremock() {
        super.setupWiremock()

        stub(HttpMethod.POST, "/v1/story/recommend", HttpStatus.OK, "v1/story/recommend.json")

        stub(HttpMethod.GET, "/v1/story/20", HttpStatus.OK, "v1/story/get-story20-published.json")
        stub(HttpMethod.GET, "/v1/story/99", HttpStatus.OK, "v1/story/get-story99-user99.json")
    }

    @Test
    fun `story menu available for story owner`() {
        login()
        driver.get("$url/read/20/test")

        assertElementPresent("#story-menu")
    }

    @Test
    fun `story menu not available for non-story owner`() {
        stub(HttpMethod.GET, "/v1/story/20", HttpStatus.OK, "v1/story/get-story20-user99.json")
        login()
        driver.get("$url/read/20/test")

        assertElementNotPresent("#story-menu")
    }

    @Test
    fun `story menu not available for anonymous`() {
        driver.get("$url/read/20/looks-good")
        assertElementNotPresent("#story-menu")
    }

    @Test
    fun `story tracking not available for story owner`() {
        login()
        driver.get("$url/read/20/test")

        assertElementNotPresent("#track-script")
    }

    @Test
    fun `story tracking not available for super-user`() {
        givenUser(1, name = "ray.sponsible", fullName = "Ray Sponsible", blog = true, superUser = true)

        login()
        driver.get("$url/read/20/test")

        assertElementNotPresent("#track-script")
    }

    @Test
    fun `story tracking available for non-story owner`() {
        stub(HttpMethod.GET, "/v1/story/20", HttpStatus.OK, "v1/story/get-story20-user99.json")
        login()
        driver.get("$url/read/20/test")

        assertElementPresent("#track-script")
    }

    @Test
    fun `story tracking available for anonymous`() {
        driver.get("$url/read/20/test")

        assertElementPresent("#track-script")
    }

    @Test
    fun `anonymous can read published story`() {
        driver.get("$url/read/20/test")

        assertCurrentPageIs(PageName.READ)

        assertElementAttribute(".author-card img", "src", "https://avatars3.githubusercontent.com/u/39621277?v=4")
        assertElementAttributeEndsWith(".author-card a", "href", "/@/ray.sponsible")
        assertElementText(".author-card .bio", UserApiFixtures.DEFAULT_BIOGRAPHY)

        assertElementAttribute(".author-card .website", "href", "https://www.me.com/ray.sponsible")
        assertElementAttribute(".author-card .website", "wutsi-track-event", "link-website")
        assertElementAttribute(".author-card .website", "wutsi-track-value", "website")

        assertElementAttribute(".author-card .facebook", "href", "https://www.facebook.com/ray.sponsible")
        assertElementAttribute(".author-card .facebook", "wutsi-track-event", "link-facebook")
        assertElementAttribute(".author-card .facebook", "wutsi-track-value", "facebook")

        assertElementAttribute(".author-card .twitter", "href", "https://www.twitter.com/ray.sponsible")
        assertElementAttribute(".author-card .twitter", "wutsi-track-event", "link-twitter")
        assertElementAttribute(".author-card .twitter", "wutsi-track-value", "twitter")

        assertElementAttribute(".author-card .linkedin", "href", "https://www.linkedin.com/in/ray.sponsible")
        assertElementAttribute(".author-card .linkedin", "wutsi-track-event", "link-linkedin")
        assertElementAttribute(".author-card .linkedin", "wutsi-track-value", "linkedin")

        assertElementText("h1", "Lorem Ipsum")
        assertElementText("h2.tagline", "This is awesome story!")

        assertElementNotPresent("#nav.super-user")
        assertElementNotPresent("#story-menu")
    }

    @Test
    fun `draft story cannot be read`() {
        stub(HttpMethod.GET, "/v1/story/20", HttpStatus.OK, "v1/story/get-story20-draft.json")
        driver.get("$url/read/20/looks-good")

        assertCurrentPageIs(PageName.ERROR_403)
    }

    @Test
    fun `not-live story cannot be read`() {
        stub(HttpMethod.GET, "/v1/story/20", HttpStatus.OK, "v1/story/get-story20-not-live.json")
        driver.get("$url/read/20/looks-good")

        assertCurrentPageIs(PageName.ERROR_403)
    }

    @Test
    fun `invalid story redirect to 4040`() {
        driver.get("$url/read/9999999/looks-good")

        assertCurrentPageIs(PageName.ERROR_404)
    }

    @Test
    fun `share to Social Network`() {
        driver.get("$url/read/20/looks-good")

        assertElementNotVisible(".share-box")
        click("#share-menu")

        assertElementVisible(".share-box")
        assertElementAttribute(".share-box .btn-facebook", "wutsi-share-target", "facebook")
        assertElementAttribute(".share-box .btn-facebook", "wutsi-story-id", "20")

        assertElementAttribute(".share-box .btn-twitter", "wutsi-share-target", "twitter")
        assertElementAttribute(".share-box .btn-twitter", "wutsi-story-id", "20")

        assertElementAttribute(".share-box .btn-whatsapp", "wutsi-share-target", "whatsapp")
        assertElementAttribute(".share-box .btn-whatsapp", "wutsi-story-id", "20")

        assertElementAttribute(".share-box .btn-messenger", "wutsi-share-target", "messenger")
        assertElementAttribute(".share-box .btn-messenger", "wutsi-story-id", "20")

        assertElementAttribute(".share-box .btn-linkedin", "wutsi-share-target", "linkedin")
        assertElementAttribute(".share-box .btn-linkedin", "wutsi-story-id", "20")

        assertElementAttribute(".share-box .btn-telegram", "wutsi-share-target", "telegram")
        assertElementAttribute(".share-box .btn-telegram", "wutsi-story-id", "20")
    }

    @Test
    fun `open share box on read`() {
        login()
        driver.get("$url/read/20/looks-good?share=1")

        assertElementVisible(".share-box")
    }

    @Test
    fun `imported content`() {
        stub(HttpMethod.GET, "/v1/story/20", HttpStatus.OK, "v1/story/get-story-imported.json")
        driver.get("$url/read/20/looks-good")

        assertElementAttribute(
            "head link[rel=canonical]",
            "href",
            "https://kamerkongossa.cm/2020/01/07/a-yaounde-on-rencontre-le-sous-developpement-par-les-chemins-quon-emprunte-pour-leviter"
        )
    }

    @Test
    fun `META headers`() {
        driver.get("$url/read/20/looks-good")

        val title = "Lorem Ipsum"
        val description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry"

        assertElementAttribute("html", "lang", "en")
        assertElementAttribute("head title", "text", "$title | Wutsi")
        assertElementAttribute("head meta[name='description']", "content", description)
        assertElementAttribute("head meta[name='robots']", "content", "index,follow")

        assertElementAttribute("head meta[property='og:title']", "content", title)
        assertElementAttribute("head meta[property='og:description']", "content", description)
        assertElementAttribute("head meta[property='og:type']", "content", "article")
        assertElementAttribute("head meta[property='og:url']", "content", "http://localhost:8081/read/20/lorem-ipsum")
        assertElementAttribute(
            "head meta[property='og:image']",
            "content",
            "https://images.pexels.com/photos/2167395/pexels-photo-2167395.jpeg"
        )
        assertElementAttribute("head meta[property='og:site_name']", "content", "Wutsi")
        assertElementAttribute("head meta[property='article:author']", "content", "Ray Sponsible")
        assertElementAttributeStartsWith("head meta[property='article:modified_time']", "content", "2020-03-27T")
        assertElementAttributeStartsWith("head meta[property='article:published_time']", "content", "2020-03-27T")
        assertElementCount("head meta[property='article:tag']", 3)

        assertElementAttribute("head meta[name='wutsi:story_id']", "content", "20")
        assertElementPresent("head meta[name='wutsi:hit_id']")

        assertElementAttribute("head meta[name='facebook:app_id']", "content", "629340480740249")
    }

    @Test
    fun `Twitter card summary_large_image`() {
        driver.get("$url/read/20/looks-good")

        val title = "Lorem Ipsum"
        val description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry"

        assertElementAttribute("head meta[name='twitter:title']", "content", title)
        assertElementAttribute("head meta[name='twitter:description']", "content", description)
        assertElementAttribute("head meta[name='twitter:card']", "content", "summary_large_image")
        assertElementAttribute("head meta[name='twitter:site']", "content", "@ray.sponsible")
        assertElementAttribute("head meta[name='twitter:creator']", "content", "@ray.sponsible")
    }

    @Test
    fun `Twitter card summary`() {
        stub(HttpMethod.GET, "/v1/story/20", HttpStatus.OK, "v1/story/get-story30-no_thumbnail.json")
        driver.get("$url/read/20/looks-good")

        val title = "Lorem Ipsum"
        val description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry"

        assertElementNotPresent("head meta[property='og:image']")

        assertElementAttribute("head meta[name='twitter:title']", "content", title)
        assertElementAttribute("head meta[name='twitter:description']", "content", description)
        assertElementAttribute("head meta[name='twitter:card']", "content", "summary")
        assertElementAttribute("head meta[name='twitter:site']", "content", "@ray.sponsible")
        assertElementAttribute("head meta[name='twitter:creator']", "content", "@ray.sponsible")
    }

    @Test
    fun `Google Analytics`() {
        driver.get("$url/read/20/looks-good")
        assertElementPresent("script#ga-code")
    }

    @Test
    fun `Facebook Pixel`() {
        driver.get("$url/read/20/looks-good")
        assertElementPresent("script#fb-pixel-code")
    }

    @Test
    fun `Schemas script`() {
        driver.get("$url/read/20/test")

        assertElementPresent("script[type='application/ld+json']")
    }
}
