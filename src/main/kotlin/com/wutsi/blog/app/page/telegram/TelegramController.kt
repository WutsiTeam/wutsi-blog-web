package com.wutsi.blog.app.page.telegram

import com.wutsi.blog.app.common.controller.AbstractPageController
import com.wutsi.blog.app.common.service.RequestContext
import com.wutsi.blog.app.page.telegram.model.TelegramForm
import com.wutsi.blog.app.page.telegram.service.TelegramService
import com.wutsi.blog.app.util.PageName
import com.wutsi.blog.client.channel.ChannelType
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.net.URLEncoder

@Controller
@RequestMapping()
@ConditionalOnProperty(value = ["wutsi.toggles.channel-telegram"], havingValue = "true")
class TelegramController(
    private val telegramService: TelegramService,
    requestContext: RequestContext,

    @Value("\${wutsi.telegram.bot.title}") private val botTitle: String,
    @Value("\${wutsi.telegram.help-url}") private val helpUrl: String
) : AbstractPageController(requestContext) {
    override fun pageName() = PageName.TELEGRAM

    @GetMapping("/telegram")
    fun index(
        @RequestParam(required = false) error: String? = null,
        @RequestParam(required = false, defaultValue = "") title: String = "",
        @RequestParam(required = false, defaultValue = "") username: String = "",
        @RequestParam(required = false, defaultValue = "group") type: String = "group",
        model: Model
    ): String {
        if (error != null) {
            val key = "error.$error.$type"
            val message = requestContext.getMessage(key = key, args = arrayOf(botTitle))
            model.addAttribute("error", message)
        }
        model.addAttribute("helpUrl", helpUrl)
        model.addAttribute("botTitle", botTitle)
        model.addAttribute("chatTitle", title)
        model.addAttribute("chatType", type)
        model.addAttribute("username", username)
        return "page/telegram/index"
    }

    @PostMapping("/telegram")
    fun submit(@ModelAttribute form: TelegramForm, model: Model): String {
        try {
            val chat = telegramService.connect(form)

            return "redirect:/me/settings/channel/create?" +
                "id=${chat.id}" +
                "&accessToken=-" +
                "&accessTokenSecret=-" +
                "&name=" + URLEncoder.encode(chat.title, "utf-8") +
                "&type=" + ChannelType.telegram
        } catch (ex: Exception) {
            return "redirect:/telegram?error=${ex.message}&" +
                "&type=${form.chatType}" +
                "&title=" + URLEncoder.encode(form.chatTitle, "utf-8") +
                "&username=" + URLEncoder.encode(form.username, "utf-8")
        }
    }
}
