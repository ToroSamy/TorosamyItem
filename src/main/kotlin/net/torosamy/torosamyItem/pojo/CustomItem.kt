package net.torosamy.torosamyItem.pojo

class CustomItem {
    //物品基础配置
    var displayName: String? = null
    var material: String? = null
    var lore: List<String>? = null
    var unbreakable: Boolean? = null
    var enchantList: List<String>? = null
    var itemFlagList: List<String>? = null
    var clearAttribute: Boolean? = null
    var color: String? = null

    //物品指令 <key,command>
    var commands = HashMap<String, ItemCommand>()

    //物品更新
    var update: Boolean? = null
    var key: String? = null
    var configString: String? = null
    var hashCode: Int? = null

    //物品消耗
    var leftConsume: Boolean = false
    var rightConsume: Boolean = false
}