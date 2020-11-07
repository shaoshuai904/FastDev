package com.maple.fastdev.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.maple.fastdev.R
import com.maple.fastdev.databinding.BaseRefreshDataListBinding
import com.maple.fastdev.pagestatus.PageChangeAction
import com.maple.fastdev.pagestatus.PageStatusManager
import com.maple.fastdev.utils.setOnSingleClickListener
import com.maple.msdialog.DividerItemDecoration
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener

/**
 * 基础的刷新加载数据列表。
 *
 * @author : shaoshuai27
 * @date ：2020/1/15
 */
abstract class BaseSmartRefreshListFragment : BaseFragment() {
    private lateinit var binding: BaseRefreshDataListBinding
    private lateinit var pageStatusManager: PageStatusManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.base_refresh_data_list, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initUI()
    }

    // 获取适配器
    abstract fun provideAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>
    // 自定义分割线，如果不要分割线，子类重写该方法返回null
    open fun getItemDecoration(): RecyclerView.ItemDecoration? = DividerItemDecoration(mContext, 15f, 1f)
    // 空内容提示文本
    open fun getEmptyText(): CharSequence = "暂无数据"
    // Loading提示文本
    open fun getLoadingText(): CharSequence = "loading..."
    // 刷新逻辑
    open fun onRefreshData(refreshLayout: RefreshLayout? = null) {}
    // 加载逻辑
    open fun onLoadMoreData(refreshLayout: RefreshLayout) {}

    fun getHeaderRoot() = binding.llHeader
    fun getFooterRoot() = binding.llFooter

    private var isPull = false
    fun initUI() {
        binding.apply {
            getItemDecoration()?.let { rvData.addItemDecoration(it) }
            rvData.adapter = provideAdapter()

            pageStatusManager = PageStatusManager(smartRefresh).apply {
                setPageStatusChangeAction(object : PageChangeAction() {
                    override fun onShowLoading(loadingView: View?) {
                        super.onShowLoading(loadingView)
                        val tvText: TextView? = loadingView?.findViewById(R.id.tv_loading)
                        tvText?.text = getLoadingText()
                    }
                })
                getEmptyView()?.let {
                    val tvData = it.findViewById<TextView>(R.id.tv_no_data_show)
                    tvData?.text = getEmptyText()
                    it.setOnSingleClickListener {
                        isPull = false
                        onRefreshData()
                    }
                }
                // 目前项目中没有retry view，以empty view代替了
//                getRetryView()?.let {
//                    it.setOnSingleClickListener {
//                        isPull = false
//                        onRefreshData()
//                    }
//                }
            }
            smartRefresh.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
                override fun onRefresh(refreshLayout: RefreshLayout) {
                    isPull = true
                    onRefreshData(refreshLayout)
                }

                override fun onLoadMore(refreshLayout: RefreshLayout) {
                    onLoadMoreData(refreshLayout)
                }
            })
            enableLoadMore(false) // 默认不可加载更多，当下拉刷新满载时，启用load more...
        }
    }

    fun isNullData(isNull: Boolean) {
        pageStatusManager.showEmptyView(isNull)
    }

    fun enableLoadMore(enableLoadMore: Boolean?) {
        binding.smartRefresh.setEnableLoadMore(enableLoadMore == true)
    }

    fun isRefreshing(it: Boolean?) {
        if (it == true) {
            isNullData(false)
            if (!isPull) {
                showLoading(true)
            }
        } else {
            showLoading(false)
            binding.smartRefresh.finishRefresh()
        }
    }

    fun isLoadMoreing(it: Boolean?) {
        if (it == true) {
            // binding.llLoading.visibility = View.VISIBLE
            // binding.smartRefresh.autoLoadMore()
        } else {
            // binding.llLoading.visibility = View.GONE
            binding.smartRefresh.finishLoadMore()
        }
    }

    fun showLoading(isShow: Boolean) {
        pageStatusManager.showLoadingView(isShow)
    }
}
