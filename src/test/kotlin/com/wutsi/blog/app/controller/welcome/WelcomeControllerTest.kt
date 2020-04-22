package com.wutsi.blog.app.controller.welcome

import com.wutsi.blog.SeleniumTestSupport
import com.wutsi.blog.app.util.PageName
import org.junit.Test
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus


class WelcomeControllerTest : SeleniumTestSupport() {
    override fun setupWiremock() {
        super.setupWiremock()

        stub(HttpMethod.POST, "/v1/user/1", HttpStatus.OK)
    }

    @Test
    fun `set name` () {
        gotoPage()

        input(".form-control", "ray.sponsible1")
        click("#btn-next")

        assertCurrentPageIs(PageName.WELCOME_FULLNAME)
    }

    @Test
    fun `set name empty` () {
        gotoPage()

        input(".form-control", "")
        click("#btn-next")

        assertCurrentPageIs(PageName.WELCOME)
    }



    @Test
    fun `set fullname` () {
        gotoPage()

        click("#btn-next")

        input(".form-control", "Ray S.")
        click("#btn-next")

        assertCurrentPageIs(PageName.WELCOME_EMAIL)
    }

    @Test
    fun `set fullname empty` () {
        gotoPage()

        click("#btn-next")

        input(".form-control", "")
        click("#btn-next")

        assertCurrentPageIs(PageName.WELCOME_EMAIL)
    }

    @Test
    fun `set fullname back` () {
        gotoPage()

        click("#btn-next")

        click("#btn-previous")

        assertCurrentPageIs(PageName.WELCOME)
    }



    @Test
    fun `set email` () {
        gotoPage()

        click("#btn-next")
        click("#btn-next")

        input(".form-control", "ray.sponsible1@gmail.com")
        click("#btn-next")

        assertCurrentPageIs(PageName.WELCOME_BIOGRAPHY)
    }

    @Test
    fun `set email back` () {
        gotoPage()

        click("#btn-next")
        click("#btn-next")

        click("#btn-previous")

        assertCurrentPageIs(PageName.WELCOME_FULLNAME)
    }

    @Test
    fun `set biography` () {
        gotoPage()

        click("#btn-next")
        click("#btn-next")
        click("#btn-next")

        input(".form-control", "This is a nice bio")
        click("#btn-next")

        assertCurrentPageIs(PageName.WELCOME_PICTURE)
    }

    @Test
    fun `set biography back` () {
        gotoPage()

        click("#btn-next")
        click("#btn-next")
        click("#btn-next")

        click("#btn-previous")

        assertCurrentPageIs(PageName.WELCOME_EMAIL)
    }

    @Test
    fun `set picture` () {
        stub(HttpMethod.GET, "/v1/user/@/ray.sponsible", HttpStatus.OK, "v1/user/get-user1.json")
        gotoPage()

        click("#btn-next")
        click("#btn-next")
        click("#btn-next")
        click("#btn-next")
        click("#btn-next")

        assertCurrentPageIs(PageName.BLOG)
    }
    @Test
    fun `set picture back` () {
        gotoPage()

        click("#btn-next")
        click("#btn-next")
        click("#btn-next")
        click("#btn-next")

        click("#btn-previous")

        assertCurrentPageIs(PageName.WELCOME_BIOGRAPHY)
    }

    private fun gotoPage() {
        login()
        navigate("$url/welcome")

        assertElementNotPresent(".label-danger")
        assertCurrentPageIs(PageName.WELCOME)
    }
}
