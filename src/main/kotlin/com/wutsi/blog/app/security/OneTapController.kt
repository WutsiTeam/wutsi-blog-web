package com.wutsi.blog.app.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.wutsi.blog.app.security.config.SecurityConfiguration
import com.wutsi.blog.app.security.oauth.OAuthUser
import com.wutsi.core.http.Http
import com.wutsi.core.logging.KVLogger
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import java.util.UUID
import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping("/login/onetap")
class OneTapController(
    logger: KVLogger,
    objectMapper: ObjectMapper,
    private val http: Http
) : AbstractLoginController(logger, objectMapper) {

    @GetMapping("/callback")
    @ResponseBody()
    fun callback(request: HttpServletRequest): Map<String, String> {
        val credential = request.getParameter("credential")
        val user = toOAuthUser(credential)
        val url = getSigninUrl(UUID.randomUUID().toString(), user)

        logger.add("RedirectURL", url)
        return mapOf("url" to url)
    }

    private fun toOAuthUser(credential: String): OAuthUser {
        val url = "https://oauth2.googleapis.com/tokeninfo?id_token=$credential"
        val attrs = http.get(url, Map::class.java).body as Map<String, Any>
        logger.add("TokenInfo", attrs)

        return toOAuthUser(attrs)
    }

    override fun toOAuthUser(attrs: Map<String, Any>) = OAuthUser(
        id = attrs["sub"].toString(),
        fullName = attrs["name"].toString(),
        pictureUrl = attrs["picture"].toString(),
        email = attrs["email"].toString(),
        provider = SecurityConfiguration.PROVIDER_GOOGLE
    )
}
