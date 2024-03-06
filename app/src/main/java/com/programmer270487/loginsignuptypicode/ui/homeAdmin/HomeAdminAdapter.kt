package com.programmer270487.loginsignuptypicode.ui.homeAdmin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.programmer270487.loginsignuptypicode.data.Role
import com.programmer270487.loginsignuptypicode.databinding.ItemHomeAdminBinding

class HomeAdminAdapter(private val roles: MutableList<Role>) : RecyclerView.Adapter<HomeAdminAdapter.PhotoViewHolder>() {
    fun add(newList: List<Role>) {
        val start = roles.size
        roles.addAll(newList)
        notifyItemRangeInserted(start, newList.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val b = ItemHomeAdminBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(b)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val role = roles[position]
        holder.bind(role)
    }

    override fun getItemCount(): Int {
        return roles.size
    }

    inner class PhotoViewHolder(private val b: ItemHomeAdminBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(role: Role) {
            b.apply {
                id.text = "Id: ${role.id}"
                val adminRole = role.role ?: false
                if (adminRole) {
                    this.adminRole.text = "Role: ✔️"
                } else {
                    this.adminRole.text = "Role: x"
                }
                username.text = "Username: " + role.username ?: "Username: Not available"
                email.text = "Email: " + role.email ?: "Email: Not available"
            }
        }
    }
}
