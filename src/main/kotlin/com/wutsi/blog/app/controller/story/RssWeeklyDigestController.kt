package com.wutsi.blog.app.controller.story

import com.wutsi.blog.app.service.rss.RssWeeklyDigestView
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class RssWeeklyDigestController(
        private val view: RssWeeklyDigestView
){
    @GetMapping("/rss/digest/weekly")
    fun index(): RssWeeklyDigestView {
        return view
    }
}