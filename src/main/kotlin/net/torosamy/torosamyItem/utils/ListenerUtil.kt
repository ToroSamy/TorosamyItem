package net.torosamy.torosamyItem.utils

import net.torosamy.torosamyItem.TorosamyItem
import net.torosamy.torosamyItem.listener.*

class ListenerUtil {
    companion object{
        fun registerListener() {
            TorosamyItem.plugin.server.pluginManager.registerEvents(UpdateItemListener(),TorosamyItem.plugin)
            TorosamyItem.plugin.server.pluginManager.registerEvents(CommandItemListener(),TorosamyItem.plugin)
            TorosamyItem.plugin.server.pluginManager.registerEvents(ConsumeItemListener(),TorosamyItem.plugin)
            TorosamyItem.plugin.server.pluginManager.registerEvents(MoveItemListener(),TorosamyItem.plugin)
        }
    }
}