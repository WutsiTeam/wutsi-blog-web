package com.wutsi.blog.app.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.scribejava.core.oauth.OAuth20Service
import com.wutsi.blog.app.security.config.OAuthConfiguration
import com.wutsi.blog.app.security.config.SecurityConfiguration
import com.wutsi.blog.app.security.oauth.OAuthUser
import com.wutsi.core.logging.KVLogger
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletRequest


@Controller
@RequestMapping("/login/facebook")
class FacebookLoginController(
        logger: KVLogger,
        objectMapper: ObjectMapper,
        @Qualifier(OAuthConfiguration.FACEBOOK_OAUTH_SERVICE) private val oauth: OAuth20Service
) : AbstractOAuth20LoginController(logger, objectMapper) {
    override fun getOAuthService() = oauth

    override fun getUserUrl() = "https://graph.facebook.com/me"

    override fun toOAuthUser(attrs: Map<String, Any>) = OAuthUser(
                id = attrs["id"].toString(),
                fullName = attrs["name"].toString(),
                email = attrs["email"]?.toString(),
                pictureUrl = "https://graph.facebook.com/" + attrs["id"] + "/picture?type=square",
                provider = SecurityConfiguration.PROVIDER_FACEBOOK
        )

    override fun getError(request: HttpServletRequest) = request.getParameter("error_code")
}