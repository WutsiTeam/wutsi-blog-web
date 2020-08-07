package com.wutsi.blog.app.common.model

data class PageModel (
        val name: String = "",
        val title: String = "",
        val description: String = "",
        val type: String = "website",
        val url: String? = null,
        val imageUrl: String? = null,
        val modifiedTime: String? = null,
        val publishedTime: String? = null,
        val author: String? = null,
        val robots: String = "all",
        val tags: List<String> = emptyList(),
        val baseUrl: String,
        val assetUrl: String,
        val twitterUserId: String? = null,
        val googleAnalyticsCode: String,
        val facebookPixelCode: String,
        val canonicalUrl: String? = null,
        val googleClientId: String? = null,
        val showGoogleOneTap: Boolean = false,
        val language: String,
        val schemas: String?,
        val firebaseConfig: FirebaseConfigModel,
        val showNotificationOptIn: Boolean = false
)

