package com.maple.fastdev.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.maple.fastdev.R
import com.maple.fastdev.databinding.ItemInfoKeyValueBinding
import com.maple.fastdev.databinding.ItemInfoNoDataBinding
import com.maple.fastdev.databinding.ItemInfoTitleBinding
import com.maple.msdialog.adapter.BaseQuickAdapter

/**
 * 基本信息 item适配器
 *
 * @author : shaoshuai27
 * @date ：2020/1/6
 */
open class KeyValueAdapter(val mContext: Context) : BaseQuickAdapter<ItemInfoBean, RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return getItem(position).getType().id
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ItemType.Title.id -> TitleHolder(DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_info_title, parent, false))
            ItemType.KeyValue.id -> ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_info_key_value, parent, false))
            ItemType.NoData.id -> NoDataHolder(DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_info_no_data, parent, false))
            ItemType.Line.id -> LineHolder(View(mContext))
            else -> customizedViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (item.getType()) {
            ItemType.Title -> (holder as TitleHolder).bind(item as TitleBean)
            ItemType.KeyValue -> (holder as ItemHolder).bind(item as KeyValueBean)
            ItemType.NoData -> (holder as NoDataHolder).bind(item as NoDataBean)
            ItemType.Line -> (holder as LineHolder).bind(item as LineBean)
            ItemType.Customized -> customizedBindViewHolder(holder, position)
        }
    }


    // 标题
    inner class TitleHolder(val binding: ItemInfoTitleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TitleBean) {
            // bindViewClickListener(this)
            binding.tvTitle.text = item.title
        }
    }

    // Key-Value
    inner class ItemHolder(val binding: ItemInfoKeyValueBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: KeyValueBean) {
            // bindViewClickListener(this)
            binding.tvName.text = item.name
            binding.tvValue.text = item.value
        }
    }

    // No Data
    inner class NoDataHolder(val binding: ItemInfoNoDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: NoDataBean) {
            // bindViewClickListener(this)
            binding.tvMessage.text = item.message
        }
    }

    // 分割线
    inner class LineHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: LineBean) {
            view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, item.height)
        }
    }

    // Customized 定制款，对于需要填充自定义Item View的，重写以下两个方法。
    open fun customizedViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return LineHolder(View(mContext))
    }

    open fun customizedBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {}
}
