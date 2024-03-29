package com.wutsi.blog.app.security.oauth

import com.wutsi.blog.app.backend.AuthenticationBackend
import com.wutsi.blog.app.common.service.RequestContext
import com.wutsi.blog.client.user.AuthenticateRequest
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class OAuthAuthenticationProvider(
    private val backend: AuthenticationBackend,
    private val requestContext: RequestContext
) : AuthenticationProvider {
    override fun authenticate(auth: Authentication): Authentication {
        val authentication = auth as OAuthTokenAuthentication
        return authenticate(authentication)
    }

    private fun authenticate(authentication: OAuthTokenAuthentication): Authentication {
        val user = authentication.principal.user
        backend.login(
            AuthenticateRequest(
                accessToken = authentication.accessToken,
                provider = authentication.principal.user.provider,
                pictureUrl = user.pictureUrl,
                fullName = user.fullName,
                email = user.email,
                providerUserId = user.id,
                language = LocaleContextHolder.getLocale().language,
                siteId = requestContext.siteId()
            )
        )

        authentication.isAuthenticated = true
        requestContext.request.getSession(true).maxInactiveInterval = 84600 // 1d
        return authentication
    }

    override fun supports(clazz: Class<*>) = OAuthTokenAuthentication::class.java == clazz
}
