package com.wutsi.blog.app.security.oauth

import com.wutsi.blog.app.backend.AuthenticationBackend
import com.wutsi.blog.app.security.SecurityConfiguration
import com.wutsi.blog.client.user.AuthenticateRequest
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest


@Component
class OAuthAuthenticationProvider(
        private val backend: AuthenticationBackend,
        private val request: HttpServletRequest
) : AuthenticationProvider {
    override fun authenticate(auth: Authentication): Authentication {
        try {
            validateState()
            val authentication = auth as OAuthTokenAuthentication
            return authenticate(authentication)
        } finally {
            clearState()
        }
    }

    private fun authenticate(authentication: OAuthTokenAuthentication): Authentication {
        val user = authentication.principal.user
        backend.login(AuthenticateRequest(
                accessToken = authentication.accessToken,
                provider = authentication.principal.user.provider,
                pictureUrl = user.pictureUrl,
                fullName = user.fullName,
                email = user.email,
                providerUserId = user.id
        ))

        authentication.setAuthenticated(true)
        return authentication
    }

    override fun supports(clazz: Class<*>) = OAuthTokenAuthentication::class.java == clazz

    private fun validateState() {
        val state = request.getParameter("state")
        if (state == null || state.isEmpty() || state != request.session.getAttribute(SecurityConfiguration.SESSION_STATE)){
            throw BadCredentialsException("invalid_state")
        }
    }

    private fun clearState() {
        request.session.removeAttribute(SecurityConfiguration.SESSION_STATE)
    }
}
