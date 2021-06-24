package com.wutsi.blog.app.page.mail

import com.wutsi.blog.app.page.track.model.PushTrackForm
import com.wutsi.blog.app.page.track.service.TrackService
import com.wutsi.blog.app.util.PageName
import org.apache.commons.io.IOUtils
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.time.Clock
import java.util.UUID
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
@RequestMapping("/story/pixel")
@Deprecated("Replaced by MailTrackingController")
class StoryMailPixelController(
    private val trackService: TrackService,
    private val clock: Clock
) {
    companion object {
        val REFERER = "https://pixel.mail.wutsi.com"
    }

    @GetMapping("/{storyId}.png", produces = ["image/png"])
    fun pixel(
        @PathVariable storyId: Long,
        @RequestHeader("User-Agent", required = false) userAgent: String? = null,
        @RequestParam("u", required = false) userId: Long? = null,
        @RequestParam("d", required = false) durationMinutes: Int? = null,
        @RequestParam("c", required = false) campaign: String? = null,
        request: HttpServletRequest,
        response: HttpServletResponse
    ) {
        try {
            val png = StoryMailPixelController::class.java.getResourceAsStream("/pixel/img.png")
            response.contentType = "image/png"
            response.addHeader("Cache-Control", "no-cache, max-age=0")
            response.addHeader("Pragma", "no-cache")
            IOUtils.copy(png, response.outputStream)
        } finally {
            val url = request.requestURL.toString() + (request.queryString?.let { "?$it" } ?: "")
            track(storyId, userAgent, userId, durationMinutes, url)
        }
    }

    private fun track(
        storyId: Long,
        userAgent: String?,
        userId: Long?,
        durationMinutes: Int?,
        url: String
    ) {
        val hitId = UUID.randomUUID().toString()
        val deviceId = UUID.randomUUID().toString()
        val time = clock.millis()

        trackService.push(
            PushTrackForm(
                time = time,
                hid = hitId,
                duid = deviceId,
                pid = storyId.toString(),
                uid = userId?.toString(),
                page = PageName.READ,
                event = "readstart",
                ua = userAgent,
                referer = REFERER,
                url = url
            )
        )

        if (durationMinutes != null) {
            trackService.push(
                PushTrackForm(
                    time = (time + durationMinutes * 60 * 1000),
                    hid = hitId,
                    duid = deviceId,
                    pid = storyId.toString(),
                    uid = userId.toString(),
                    page = PageName.READ,
                    event = "scroll",
                    value = "100",
                    ua = userAgent,
                    referer = REFERER,
                    url = url
                )
            )
        }
    }
}