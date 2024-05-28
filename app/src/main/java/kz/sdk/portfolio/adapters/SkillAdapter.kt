package kz.sdk.portfolio.adapters

import android.content.ClipData.Item
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import kz.sdk.portfolio.base.BaseSkillViewHolder
import kz.sdk.portfolio.databinding.ItemSkillBinding
import kz.sdk.portfolio.models.License
import kz.sdk.portfolio.models.Skill

class SkillAdapter:ListAdapter<Skill, BaseSkillViewHolder<*>>(SkillDiffUtils()) {

    var itemClick:((Skill) -> Unit)? = null

    class SkillDiffUtils:DiffUtil.ItemCallback<Skill>(){
        override fun areItemsTheSame(oldItem: Skill, newItem: Skill): Boolean {
            return oldItem.id == newItem.id

        }

        override fun areContentsTheSame(oldItem: Skill, newItem: Skill): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseSkillViewHolder<*> {
        return SkillViewHolder(
            ItemSkillBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: BaseSkillViewHolder<*>, position: Int) {
        holder.bindView(getItem(position))
    }

    inner class SkillViewHolder(binding:ItemSkillBinding):BaseSkillViewHolder<ItemSkillBinding>(binding){


        override fun bindView(item: Skill) {
            with(binding){
                title.text = item.title
                where.text = item.where
            }
            itemView.setOnClickListener {
                itemClick?.invoke(item)
            }
        }
    }

}