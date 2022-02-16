package st235.com.github.seamcarving.presentation.editor.options.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import st235.com.github.seamcarving.R

class EditorOptionsAdapter(
    private val onItemClickListener: (option: EditorOption) -> Unit
): RecyclerView.Adapter<EditorOptionsAdapter.ViewHolder>() {

    private val asyncDiffer = AsyncListDiffer(this, OptionsItemCallback())

    private val items: List<EditorOption>
    get() = asyncDiffer.currentList

    fun setItems(items: List<EditorOption>) {
        asyncDiffer.submitList(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_editor_option, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
           return onBindViewHolder(holder, position)
        }

        payloads.filterIsInstance<Payload>().forEach { payload ->
            payload.icon?.let {
                holder.iconView.setImageResource(payload.icon)
            }

            payload.text?.let {
                holder.textView.setText(payload.text)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val cardView: MaterialCardView = itemView.findViewById(R.id.card_view)
        val textView: TextView = itemView.findViewById(R.id.text_view)
        val iconView: ImageView = itemView.findViewById(R.id.icon_view)

        init {
            cardView.isClickable = true
            cardView.isFocusable = true
            cardView.setOnClickListener {
                onItemClickListener(items[adapterPosition])
            }
        }

        fun bind(option: EditorOption) {
            iconView.setImageResource(option.icon)
            textView.setText(option.text)
        }

    }

    private data class Payload(
        val icon: Int?,
        val text: Int?
    )

    private class OptionsItemCallback: DiffUtil.ItemCallback<EditorOption>() {

        override fun areItemsTheSame(oldItem: EditorOption, newItem: EditorOption): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: EditorOption, newItem: EditorOption): Boolean {
            return oldItem == newItem
        }

        override fun getChangePayload(oldItem: EditorOption, newItem: EditorOption): Payload {
            return Payload(
                icon = newItem.icon.takeIf { newItem.icon != oldItem.icon },
                text = newItem.text.takeIf { newItem.text != oldItem.text }
            )
        }
    }

}