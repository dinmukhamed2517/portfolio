package kz.sdk.portfolio.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import kz.sdk.portfolio.R
import kz.sdk.portfolio.base.BaseLicenseViewHolder
import kz.sdk.portfolio.databinding.ItemLicenseBinding
import kz.sdk.portfolio.models.Education
import kz.sdk.portfolio.models.License

class LicenseAdapter:ListAdapter<License, BaseLicenseViewHolder<*>>(LicenseDiffUtils()) {

    var itemClick:((License) -> Unit)? = null


    class LicenseDiffUtils:DiffUtil.ItemCallback<License>(){
        override fun areItemsTheSame(oldItem: License, newItem: License): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: License, newItem: License): Boolean {
            return oldItem ==newItem
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseLicenseViewHolder<*> {
        return LicenseViewHolder(
            ItemLicenseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: BaseLicenseViewHolder<*>, position: Int) {
        holder.bindView(getItem(position))
    }
    inner class LicenseViewHolder(binding:ItemLicenseBinding):BaseLicenseViewHolder<ItemLicenseBinding>(binding){
        override fun bindView(item: License) {
            with(binding){
                title.text = item.title
                Glide.with(itemView.context)
                    .load(item.img)
                    .placeholder(R.drawable.placeholder_event)
                    .into(img)

                description.text = item.description
                issued.text = item.issues
            }
            itemView.setOnClickListener {
                itemClick?.invoke(item)
            }
        }

    }

}