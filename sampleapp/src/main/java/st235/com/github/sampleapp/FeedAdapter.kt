package st235.com.github.sampleapp

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
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
        holder.bind(item)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
            return
        }

        payloads.filterIsInstance<Payload>().forEach { payload ->
            payload.description?.let {
                holder.descriptionTextView.text = payload.description
            }

            payload.author?.let {
                holder.profileTitleTextView.text = payload.author.name
                holder.loadProfileImage(payload.author.avatar)
            }

            payload.likes?.let {
                holder.likesCountTextView.text = payload.likes.count
            }

            payload.tags?.let {
                holder.loadTags(payload.tags)
            }

            payload.value?.let {
                holder.loadImage(payload.value)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imageView: ImageView = itemView.findViewById(R.id.image_view)
        val profileImageView: CircularImageView = itemView.findViewById(R.id.profile_image_view)
        val profileTitleTextView: TextView = itemView.findViewById(R.id.profile_title)
        val likesIconImageView: ImageView = itemView.findViewById(R.id.likes_icon)
        val likesCountTextView: TextView = itemView.findViewById(R.id.likes_count)
        val descriptionTextView: TextView = itemView.findViewById(R.id.description)
        val tagLayout: ChipGroup = itemView.findViewById(R.id.tag_layout)

        val context: Context
        get() {
            return itemView.context
        }

        init {
            itemView.isClickable = true
            itemView.isFocusable = true

            itemView.setOnClickListener {
                val item = items[adapterPosition]
                onItemClickListener(item)
            }
        }

        fun bind(feedItem: FeedItem) {
            profileTitleTextView.text = feedItem.author.name
            likesCountTextView.text = feedItem.likes.count
            descriptionTextView.text = feedItem.description

            loadProfileImage(feedItem.author.avatar)
            loadImage(feedItem.image)

            loadTags(feedItem.tags)
        }

        fun loadProfileImage(@DrawableRes imageRes: Int) {
            Glide.with(itemView.context)
                .load(imageRes)
                .centerCrop()
                .into(object : CustomViewTarget<CircularImageView, Drawable>(profileImageView) {
                    override fun onLoadFailed(errorDrawable: Drawable?) {
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable>?
                    ) {
                        profileImageView.setDrawable(resource)
                    }

                    override fun onResourceCleared(placeholder: Drawable?) {
                    }
                })
        }

        fun loadImage(@DrawableRes imageRes: Int) {
            Glide.with(itemView.context)
                .load(imageRes)
                .transform(SeamCarvingTransformation(sampling = 2))
                .into(imageView)
        }

        fun loadTags(tags: List<String>) {
            tagLayout.removeAllViews()

            for (tag in tags) {
                val chip = Chip(context)
                chip.text  = tag

                tagLayout.addView(chip)
            }
        }

    }

    private data class Payload(
        val id: Int?,
        val description: String?,
        val author: Author?,
        val likes: Likes?,
        val tags: List<String>?,
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
                description = newItem.description.takeIf { newItem.description != oldItem.description },
                author = newItem.author.takeIf { newItem.author != oldItem.author },
                likes = newItem.likes.takeIf { newItem.likes != oldItem.likes },
                tags = newItem.tags.takeIf { newItem.tags != oldItem.tags },
                value = newItem.image.takeIf { newItem.id != oldItem.id }
            )
        }
    }

}
