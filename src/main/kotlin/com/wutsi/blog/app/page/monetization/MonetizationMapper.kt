package com.wutsi.blog.app.page.monetization

import com.wutsi.blog.app.common.model.MoneyModel
import com.wutsi.blog.app.page.monetization.model.PlanModel
import com.wutsi.blog.app.page.monetization.model.PlanRateModel
import com.wutsi.blog.app.page.monetization.model.SubscriptionModel
import com.wutsi.subscription.dto.Plan
import com.wutsi.subscription.dto.PlanRate
import com.wutsi.subscription.dto.Subscription
import org.apache.commons.text.StringEscapeUtils
import org.springframework.stereotype.Service

@Service
class MonetizationMapper {
    fun toPlanModel(plan: Plan) = PlanModel(
        id = plan.id,
        partnerId = plan.partnerId,
        name = plan.name,
        description = if (plan.description.isNullOrEmpty()) "" else plan.description,
        descriptionHtml = StringEscapeUtils.escapeHtml4(
            if (plan.description.isNullOrEmpty()) "" else plan.description
        ).replace("\n", "<br>"),
        rate = toPlanRateModel(plan.rate),
        active = plan.active,
        internationalRate = toPlanRateModel(plan.convertedRate)
    )

    fun toPlanRateModel(rate: PlanRate) = PlanRateModel(
        id = rate.id,
        yearly = MoneyModel(rate.yearly, rate.currency),
        monthly = MoneyModel(rate.monthly, rate.currency)
    )

    fun toSubscriptionModel(subscription: Subscription) = SubscriptionModel(
        id = subscription.id,
        plan = toPlanModel(subscription.plan),
        subscriberId = subscription.subscriberId
    )
}
