package com.wutsi.blog.app.component.comment.service

import com.wutsi.blog.app.common.service.Moment
import com.wutsi.blog.app.common.service.RequestContext
import com.wutsi.blog.app.component.comment.model.CommentCountModel
import com.wutsi.blog.app.component.comment.model.CommentModel
import com.wutsi.blog.app.page.settings.model.UserModel
import com.wutsi.blog.client.comment.CommentCountDto
import com.wutsi.blog.client.comment.CommentDto
import com.wutsi.core.util.NumberUtils
import org.springframework.stereotype.Service

@Service
class CommentMapper(
    private val moment: Moment,
    private val requestContext: RequestContext
) {
    fun toCommentModel(obj: CommentDto, users: Map<Long, UserModel>) = CommentModel(
        id = obj.id,
        text = obj.text,
        modificationDateTime = moment.format(obj.modificationDateTime),
        user = users[obj.userId]
    )

    fun toCommentCountModel(obj: CommentCountDto) = CommentCountModel(
        storyId = obj.storyId,
        value = obj.value,
        valueText = if (obj.value > 0) NumberUtils.toHumanReadable(obj.value) else "",
        text = text(obj)
    )

    private fun text(obj: CommentCountDto): String {
        if (obj.value <= 0L) {
            return ""
        } else if (obj.value == 1L) {
            return requestContext.getMessage("label.1_comment")
        } else {
            return requestContext.getMessage("label.n_comments", args = arrayOf(NumberUtils.toHumanReadable(obj.value)))
        }
    }
}
