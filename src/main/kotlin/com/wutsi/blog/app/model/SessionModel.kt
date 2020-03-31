package com.wutsi.blog.app.model

import java.util.Date

data class SessionModel(
        val accessToken: String = "",
        val refreshToken: String? = null,
        val accountId: Long = -1,
        val userId: Long = -1,
        val loginDateTime: Date = Date(),
        var logoutDateTime: Date? = null
)
