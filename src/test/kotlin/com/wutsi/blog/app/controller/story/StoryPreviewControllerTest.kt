package com.wutsi.blog.app.controller.story

import com.wutsi.blog.SeleniumTestSupport
import com.wutsi.blog.app.util.PageName
import org.junit.Test
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus


class StoryPreviewControllerTest: SeleniumTestSupport() {
    override fun setupWiremock() {
        super.setupWiremock()

        stub(HttpMethod.GET, "/v1/story/20", HttpStatus.OK, "v1/story/get-story20-draft.json")
        stub(HttpMethod.GET, "/v1/story/99", HttpStatus.OK, "v1/story/get-story99-user99.json")
        stub(HttpMethod.POST, "/v1/story/search", HttpStatus.OK, "v1/story/search-draft.json")

        stub(HttpMethod.GET, "/v1/user/99", HttpStatus.OK, "v1/user/get-user99.json")
    }

    @Test
    fun `owner can preview his draft story`() {
        gotoPage()

        assertElementCount(".share", 0)
//        assertCurrentPageIs(PageName.STORY_PREVIEW)
    }

    @Test
    fun `superuser can preview any draft story`() {
        stub(HttpMethod.GET, "/v1/user/.+", HttpStatus.OK, "v1/user/get-user-superuser.json")
        gotoPage()
        assertElementCount(".share", 0)
//        assertCurrentPageIs(PageName.STORY_PREVIEW)
    }

    @Test
    fun `anonymous user cannot preview any story`() {
        gotoPage(false)
        assertCurrentPageIs(PageName.LOGIN)
    }

    @Test
    fun `user cannot preview another user story`() {
        gotoPage()
        driver.get("$url/read/99?preview=true")

        assertCurrentPageIs(PageName.ERROR_403)
    }



    fun gotoPage(login: Boolean = true) {
        if (login){
            login()

            click("nav .nav-item")
            click("#navbar-draft")
            click(".story:first-child .dropdown .btn")
            click(".story .menu-item-preview")
        } else {
            driver.get("$url/me/story/20/preview")
        }
    }

}