package net.torosamy.torosamyItem

import net.torosamy.torosamyCore.utils.MessageUtil
import net.torosamy.torosamyItem.api.TorosamyItemAPI
import net.torosamy.torosamyItem.utils.CommandUtil
import net.torosamy.torosamyItem.utils.ConfigUtil
import net.torosamy.torosamyItem.utils.ListenerUtil
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class TorosamyItem : JavaPlugin() {
    companion object{lateinit var plugin: TorosamyItem}
    override fun onEnable() {
        plugin = this
        ConfigUtil.initConfig()
        ConfigUtil.reloadConfig()
        
        CommandUtil.registerCommand()

        TorosamyItemAPI.loadItems()
        TorosamyItemAPI.loadCatalogs()
        ListenerUtil.registerListener()
        Bukkit.getConsoleSender().sendMessage(MessageUtil.format("&a[服务器娘]&a插件 &eTorosamyItem &a成功开启喵~"))
        Bukkit.getConsoleSender().sendMessage(MessageUtil.format("&a[服务器娘]&a作者 &eTorosamy|yweiyang"))
    }

    override fun onDisable() {
        ConfigUtil.saveConfig()
        
        Bukkit.getConsoleSender().sendMessage(MessageUtil.format("&a[服务器娘]&c插件 &eTorosamyItem &c成功关闭喵~"))
        Bukkit.getConsoleSender().sendMessage(MessageUtil.format("&a[服务器娘]&c作者 &eTorosamy|yweiyang"))
    }
}
