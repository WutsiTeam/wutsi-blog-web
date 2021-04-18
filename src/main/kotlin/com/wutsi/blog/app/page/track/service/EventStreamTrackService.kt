package com.wutsi.blog.app.page.track.service

import com.wutsi.blog.app.common.service.RequestContext
import com.wutsi.blog.app.page.track.model.PushTrackForm
import com.wutsi.blog.app.util.CookieHelper
import com.wutsi.blog.app.util.CookieName
import com.wutsi.stream.EventStream
import com.wutsi.tracking.dto.PushTrackRequest
import com.wutsi.tracking.event.TrackSubmittedEventPayload
import com.wutsi.tracking.event.TrackingEventType
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service

@Service
@ConditionalOnProperty(
    value = ["wutsi.toggles.event-stream-tracking"],
    havingValue = "true"
)
class EventStreamTrackService(
    private val eventStream: EventStream,
    private val requestContext: RequestContext
) : TrackService {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(EventStreamTrackService::class.java)
    }

    override fun push(form: PushTrackForm): String {
        LOGGER.info("push($form)")
        eventStream.publish(
            type = TrackingEventType.TRACK_SUBMITTED.urn,
            payload = createPayload(form)
        )
        return ""
    }

    private fun createPayload(form: PushTrackForm) = TrackSubmittedEventPayload(
        request = createRequest(form)
    )

    fun createRequest(form: PushTrackForm) = PushTrackRequest(
        time = System.currentTimeMillis(),
        value = form.value,
        long = form.long,
        lat = form.lat,
        page = form.page,
        ip = form.ip,
        event = form.event,
        pid = form.pid,
        uid = requestContext.currentUser()?.id?.toString(),
        referer = CookieHelper.get(CookieName.REFERER, requestContext.request),
        ua = requestContext.request.getHeader("User-Agent"),
        duid = CookieHelper.get(CookieName.DEVICE_UID, requestContext.request),
        hid = form.hid,
        url = form.url
    )
}
