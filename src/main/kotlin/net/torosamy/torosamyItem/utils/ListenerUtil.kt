package net.torosamy.torosamyItem.utils

import net.torosamy.torosamyItem.TorosamyItem
import net.torosamy.torosamyItem.listener.UpdateItemListener

class ListenerUtil {
    companion object{
        fun registerListener() {
            TorosamyItem.plugin.server.pluginManager.registerEvents(UpdateItemListener(),TorosamyItem.plugin)
        }
    }
}