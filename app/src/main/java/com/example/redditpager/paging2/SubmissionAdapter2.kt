package com.example.redditpager.paging2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.redditpager.R
import com.example.redditpager.databinding.ItemSeparatorBinding
import com.example.redditpager.databinding.ItemSubmissionBinding
import com.example.redditpager.paging3.PagerViewModel
import timber.log.Timber


@ExperimentalPagingApi
class SubmissionAdapter2: PagedListAdapter<PagerViewModel.ListItem, RecyclerView.ViewHolder>(UIMODEL_COMPARATOR) {

    companion object {
        private val UIMODEL_COMPARATOR = object : DiffUtil.ItemCallback<PagerViewModel.ListItem>() {
            override fun areItemsTheSame(oldItem: PagerViewModel.ListItem, newItem: PagerViewModel.ListItem): Boolean {
                return true
            }

            override fun areContentsTheSame(oldItem: PagerViewModel.ListItem, newItem: PagerViewModel.ListItem): Boolean =
                oldItem == newItem
        }
    }


    inner class SubViewHolder(binding: ItemSubmissionBinding) : RecyclerView.ViewHolder(binding.root) {
        val imageView = binding.imageView
        val labelView = binding.label

        fun bind(title: String, url: String?){
            labelView.text = title

            if (url != null){
                Glide.with(imageView.context).load(url).into(imageView)
            }else{
                Timber.i("${title} doesn't have image")
                Glide.with(imageView.context).load(R.drawable.ic_cloud_close).into(imageView)
            }

        }
    }

    inner class SeparatorViewHolder(binding: ItemSeparatorBinding) : RecyclerView.ViewHolder(binding.root) {
        val seperatorText = binding.separatorDescription
        fun bind(letter: String){
            seperatorText.text = letter
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is PagerViewModel.ListItem.Sub -> R.layout.item_submission
            else -> R.layout.item_separator
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == R.layout.item_submission) {
            return SubViewHolder(ItemSubmissionBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            return SeparatorViewHolder(ItemSeparatorBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item: PagerViewModel.ListItem? = getItem(position)
        when (item){
            is PagerViewModel.ListItem.Sub -> {
                (holder as SubViewHolder).bind(item.title, item.url)
            }
            is PagerViewModel.ListItem.Separator -> {
                (holder as SeparatorViewHolder).bind(item.upvote)
            }
        }
    }
}