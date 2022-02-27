package st235.com.github.sampleapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import st235.com.github.seamcarvingglide.SeamCarvingTransformation

class FeedAdapter(
    private val onItemClickListener: (item: FeedItem) -> Unit
): RecyclerView.Adapter<FeedAdapter.ViewHolder>() {

    private val asyncListDiffer = AsyncListDiffer(this, ItemCallback())

    private val items: List<FeedItem>
        get() {
            return asyncListDiffer.currentList
        }

    fun setItems(items: List<FeedItem>) {
        asyncListDiffer.submitList(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_feed, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.loadImage(item.image)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
            return
        }

        payloads.filterIsInstance<Payload>().forEach { payload ->
            payload.value?.let {
                holder.loadImage(payload.value)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imageView: ImageView = itemView.findViewById(R.id.image_view)

        init {
            itemView.isClickable = true
            itemView.isFocusable = true

            itemView.setOnClickListener {
                val item = items[adapterPosition]
                onItemClickListener(item)
            }
        }

        fun loadImage(@DrawableRes imageRes: Int) {
            Glide.with(itemView.context)
                .load(imageRes)
                .centerCrop()
//                .transform(SeamCarvingTransformation())
                .into(imageView)
        }

    }

    private data class Payload(
        val id: Int?,
        val value: Int?
    )

    private class ItemCallback: DiffUtil.ItemCallback<FeedItem>() {

        override fun areItemsTheSame(oldItem: FeedItem, newItem: FeedItem): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: FeedItem, newItem: FeedItem): Boolean =
            oldItem == newItem

        override fun getChangePayload(oldItem: FeedItem, newItem: FeedItem): Payload {
            return Payload(
                id = newItem.id.takeIf { newItem.id != oldItem.id },
                value = newItem.image.takeIf { newItem.id != oldItem.id }
            )
        }
    }

}
