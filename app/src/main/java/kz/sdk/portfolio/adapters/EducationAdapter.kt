package kz.sdk.portfolio.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import kz.sdk.portfolio.base.BaseEducationViewHolder
import kz.sdk.portfolio.databinding.ItemEducationBinding
import kz.sdk.portfolio.models.Education

class EducationAdapter: ListAdapter<Education, BaseEducationViewHolder<*>>(EducationDiffUtils()) {


    var itemClick:((Education) -> Unit)? = null
    class EducationDiffUtils:DiffUtil.ItemCallback<Education>(){
        override fun areItemsTheSame(oldItem: Education, newItem: Education): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Education, newItem: Education): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseEducationViewHolder<*> {
        return EducationViewHolder(
            ItemEducationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: BaseEducationViewHolder<*>, position: Int) {
        holder.bindView(getItem(position))
    }
    inner class EducationViewHolder(binding:ItemEducationBinding):BaseEducationViewHolder<ItemEducationBinding>(binding){
        override fun bindView(item: Education) {
            with(binding){
                title.text  = item.title
                degree.text = item.degree
                specialty.text = item.specialty
                period.text = item.period
                grade.text  = "${item.grade}"
            }
            itemView.setOnClickListener {
                itemClick?.invoke(item)
            }
        }

    }
}