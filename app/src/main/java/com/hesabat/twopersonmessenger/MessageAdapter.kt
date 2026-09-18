package com.hesabat.twopersonmessenger

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.hesabat.twopersonmessenger.databinding.ItemMessageBinding

class MessageAdapter(private val myId: Int, private val onLong: (Message) -> Unit) : RecyclerView.Adapter<MessageAdapter.VH>() {
    val items = mutableListOf<Message>()
    class VH(val b: ItemMessageBinding) : RecyclerView.ViewHolder(b.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    override fun getItemCount() = items.size
    override fun onBindViewHolder(h: VH, pos: Int) {
        val m = items[pos]
        val mine = m.sender_id == myId
        h.b.text.text = m.text ?: ""
        val st = if (!mine) "" else when { m.read_at != null -> "  ✓✓"; m.delivered_at != null -> "  ✓✓"; else -> "  ✓" }
        h.b.meta.text = (m.created_at ?: "") + st
        h.b.bubble.background = GradientDrawable().apply {
            cornerRadius = 18f
            setColor(Color.parseColor(if (mine) "#005C4B" else "#202C33"))
        }
        val lp = h.b.bubble.layoutParams as FrameLayout.LayoutParams
        lp.gravity = if (mine) Gravity.END else Gravity.START
        h.b.bubble.layoutParams = lp
        h.b.bubble.setOnLongClickListener { onLong(m); true }
    }
}
