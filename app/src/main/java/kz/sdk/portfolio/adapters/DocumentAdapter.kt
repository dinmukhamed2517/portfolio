package kz.sdk.portfolio.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import kz.sdk.portfolio.R
import kz.sdk.portfolio.base.BaseDocumentViewHolder
import kz.sdk.portfolio.base.BaseRewardViewHolder
import kz.sdk.portfolio.databinding.ItemDocumentBinding
import kz.sdk.portfolio.databinding.ItemRewardBinding
import kz.sdk.portfolio.models.Document
import kz.sdk.portfolio.models.Reward

class DocumentAdapter:ListAdapter<Document, BaseDocumentViewHolder<*>>(DocumentDiffUtils()) {

    class DocumentDiffUtils:DiffUtil.ItemCallback<Document>(){
        override fun areItemsTheSame(oldItem: Document, newItem: Document): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Document, newItem: Document): Boolean {
            return oldItem == newItem
        }

    }
    var itemClick:((Document) -> Unit)? = null

    inner class DocumentViewHolder(binding: ItemDocumentBinding):
        BaseDocumentViewHolder<ItemDocumentBinding>(binding){


        override fun bindView(item: Document) {
            with(binding){
                title.text = item.title
                Glide.with(itemView.context)
                    .load(item.img)
                    .placeholder(R.drawable.placeholder_event)
                    .into(img)
            }
            itemView.setOnClickListener {
                itemClick?.invoke(item)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseDocumentViewHolder<*> {
        return DocumentViewHolder(
            ItemDocumentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: BaseDocumentViewHolder<*>, position: Int) {
        holder.bindView(getItem(position))
    }
}