package kz.sdk.portfolio.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kz.sdk.portfolio.models.Education
import kz.sdk.portfolio.models.Event
import kz.sdk.portfolio.models.License
import kz.sdk.portfolio.models.Reward
import kz.sdk.portfolio.models.Skill

abstract class BaseViewHolder<VB : ViewBinding, T>(protected open val binding: VB) :
    RecyclerView.ViewHolder(binding.root) {
    abstract fun bindView(item: T)
}

abstract class BaseEventViewHolder<VB : ViewBinding>(override val binding: VB) :
    BaseViewHolder<VB, Event>(binding)

abstract class BaseEducationViewHolder<VB : ViewBinding>(override val binding: VB) :
    BaseViewHolder<VB, Education>(binding)

abstract class BaseLicenseViewHolder<VB : ViewBinding>(override val binding: VB) :
    BaseViewHolder<VB, License>(binding)

abstract class BaseSkillViewHolder<VB : ViewBinding>(override val binding: VB) :
    BaseViewHolder<VB, Skill>(binding)

abstract class BaseRewardViewHolder<VB : ViewBinding>(override val binding: VB) :
    BaseViewHolder<VB, Reward>(binding)

