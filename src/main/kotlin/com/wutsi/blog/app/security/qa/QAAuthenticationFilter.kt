package com.wutsi.blog.app.security.qa

import com.wutsi.blog.app.security.config.SecurityConfiguration
import com.wutsi.blog.app.security.oauth.OAuthPrincipal
import com.wutsi.blog.app.security.oauth.OAuthTokenAuthentication
import com.wutsi.blog.app.security.oauth.OAuthUser
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import java.util.UUID
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * AuthenticationFilter for QA testing only
 */
class QAAuthenticationFilter(
    pattern: String
) : AbstractAuthenticationProcessingFilter(AntPathRequestMatcher(pattern)) {
    @Throws(AuthenticationException::class)
    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        val accessToken = generateAccessToken()
        val auth = createAuthentication(accessToken)
        return authenticationManager.authenticate(auth)
    }

    private fun generateAccessToken() = UUID.randomUUID().toString()

    private fun createAuthentication(accessToken: String) = OAuthTokenAuthentication(
        principal = OAuthPrincipal(
            accessToken = accessToken,
            user = OAuthUser(
                id = accessToken,
                provider = SecurityConfiguration.PROVIDER_QA,
                email = "qa@wutsi.com",
                fullName = "QA User",
                pictureUrl = "https://cdn.pixabay.com/photo/2013/07/13/10/07/man-156584_1280.png"
            )
        ),
        accessToken = accessToken
    )
}
