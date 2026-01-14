package net.torosamy.torosamyItem.utils

import net.torosamy.torosamyItem.TorosamyItem
import net.torosamy.torosamyItem.listener.*

object ListenerUtil {
    fun registerListener() {
        TorosamyItem.plugin.server.pluginManager.registerEvents(UpdateItemListener(),TorosamyItem.plugin)
        TorosamyItem.plugin.server.pluginManager.registerEvents(CommandItemListener(),TorosamyItem.plugin)
        TorosamyItem.plugin.server.pluginManager.registerEvents(MoveItemListener(),TorosamyItem.plugin)
        TorosamyItem.plugin.server.pluginManager.registerEvents(CatalogClickListener(),TorosamyItem.plugin)
        TorosamyItem.plugin.server.pluginManager.registerEvents(ItemConsumeListener(),TorosamyItem.plugin)
        TorosamyItem.plugin.server.pluginManager.registerEvents(PlayAnimationListener(),TorosamyItem.plugin)
        TorosamyItem.plugin.server.pluginManager.registerEvents(AttackDefenseListener(),TorosamyItem.plugin)
        TorosamyItem.plugin.server.pluginManager.registerEvents(CrossbowListener(),TorosamyItem.plugin)
    }
}