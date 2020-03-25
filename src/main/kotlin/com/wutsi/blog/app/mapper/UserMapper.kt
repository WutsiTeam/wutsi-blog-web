package com.wutsi.blog.app.mapper

import com.wutsi.blog.app.model.UserModel
import com.wutsi.blog.client.user.UserDto
import com.wutsi.blog.client.user.UserSummaryDto
import org.springframework.stereotype.Service

@Service
class UserMapper {
    fun toUserModel(user: UserDto) = UserModel(
            id = user.id,
            fullName = user.fullName,
            pictureUrl = user.pictureUrl,
            email = user.email
    )

    fun toUserModel(user: UserSummaryDto) = UserModel(
            id = user.id,
            fullName = user.fullName,
            pictureUrl = user.pictureUrl
    )
}
