package com.wutsi.blog.app.mapper

import com.wutsi.blog.app.model.UserModel
import com.wutsi.blog.app.service.ImageKitService
import com.wutsi.blog.client.user.UserDto
import com.wutsi.blog.client.user.UserSummaryDto
import org.springframework.stereotype.Service

@Service
class UserMapper(private val imageKit: ImageKitService) {
    fun toUserModel(user: UserDto) = UserModel(
            id = user.id,
            name = user.name,
            biography = user.biography,
            fullName = user.fullName,
            pictureUrl = pictureUrl(user.pictureUrl),
            websiteUrl = user.websiteUrl,
            email = user.email,
            loginCount = user.loginCount,
            slug = slug(user),
            facebookUrl = facebookUrl(user),
            linkedinUrl = linkedinUrl(user),
            twitterUrl = twitterUrl(user),
            youtubeUrl = youtubeUrl(user),
            superUser = user.superUser,
            readAllLanguages = user.readAllLanguages,
            language = user.language,
            facebookId = user.facebookId,
            twitterId = user.twitterId,
            linkedinId = user.linkedinId,
            youtubeId = user.youtubeId
    )

    fun slug(user: UserDto) = "/@/${user.name}"

    fun slug(user: UserSummaryDto) = "/@/${user.name}"

    private fun facebookUrl(user: UserDto): String? {
        return if (user.facebookId == null) null else "https://www.facebook.com/${user.facebookId}"
    }

    private fun twitterUrl(user: UserDto): String? {
        return if (user.twitterId == null) null else "https://www.twitter.com/${user.twitterId}"
    }

    private fun linkedinUrl(user: UserDto): String? {
        return if (user.linkedinId == null) null else "https://www.linkedin.com/in/${user.linkedinId}"
    }

    private fun youtubeUrl(user: UserDto): String? {
        return if (user.youtubeId == null) null else "https://www.youtube.com/user/${user.youtubeId}"
    }

    fun toUserModel(user: UserSummaryDto) = UserModel(
            id = user.id,
            name = user.name,
            fullName = user.fullName,
            pictureUrl = pictureUrl(user.pictureUrl),
            slug = slug(user)
    )

    private fun pictureUrl(url: String?) = if (url == null) null else imageKit.transform(url, "128px")
}
