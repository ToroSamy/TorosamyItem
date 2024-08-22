package net.torosamy.torosamyItem.listener

import net.torosamy.torosamyCore.nbtapi.NBT
import net.torosamy.torosamyItem.manager.ItemManager
import net.torosamy.torosamyItem.utils.ItemUtil
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.inventory.ItemStack


class UpdateItemListener : Listener {
    @EventHandler
    fun onPlayerItemHeld(event: PlayerItemHeldEvent) {
        val oldItem: ItemStack = event.player.inventory.getItem(event.newSlot) ?: return

        var itemKey: String = ""
        var oldHashCode: Int = 0
        //获取物品对应的字段和HashCode 的同时可以判断是否是一个TorosamyItem
        NBT.get(oldItem) { nbt ->
            itemKey = nbt.getString("TorosamyItem")
            oldHashCode = nbt.getInteger("HashCode")
        }
        //如果配置文件中没有找到相应的字段 说明不是一个TorosamyItem
        if (itemKey == "" || !ItemManager.items.containsKey(itemKey)) return
        //获取新的物品
        val newItem = ItemManager.items[itemKey]!!
        //如果未设置或者 设置为禁止
        if(newItem.update == null || newItem.update == false) return
        //如果HashCode没有发生变化
        if(newItem.hashCode == oldHashCode) return

        event.player.inventory.setItem(event.newSlot,ItemUtil.getItem(newItem,event.player))
    }
}