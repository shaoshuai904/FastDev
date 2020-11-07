package com.maple.fastdev.base

import java.io.Serializable


interface ItemInfoBean {
    fun getType(): ItemType
}

enum class ItemType(val id: Int) {
    Title(0),
    KeyValue(1),
    NoData(2),
    Line(3),
    Customized(5)// 特殊定制款
}

data class TitleBean(
        var title: String
) : ItemInfoBean, Serializable {
    override fun getType() = ItemType.Title
}

data class KeyValueBean(
        var name: String,
        var value: String
) : ItemInfoBean, Serializable {
    override fun getType() = ItemType.KeyValue
}

data class NoDataBean(
        var message: String
) : ItemInfoBean, Serializable {
    override fun getType() = ItemType.NoData
}

data class LineBean(
        var height: Int // px
) : ItemInfoBean, Serializable {
    override fun getType() = ItemType.Line
}