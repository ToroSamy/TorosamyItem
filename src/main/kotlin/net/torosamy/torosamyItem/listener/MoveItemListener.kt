package net.torosamy.torosamyItem.listener

import net.torosamy.torosamyCore.nbtapi.NBT
import net.torosamy.torosamyItem.manager.ItemManager
import net.torosamy.torosamyItem.utils.ConfigUtil
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryMoveItemEvent
import org.bukkit.event.inventory.InventoryPickupItemEvent
import org.bukkit.event.inventory.InventoryType

class MoveItemListener : Listener {
    @EventHandler
    fun onClickCurrent(event: InventoryClickEvent) {
        //处理被点击的物品
        val item = event.currentItem ?: return
        if(item.type == Material.AIR) return
        var itemKey: String = ""
        //判断是否是一个TorosamyItem
        NBT.get(item) { nbt -> itemKey = nbt.getString("TorosamyItem") }
        //如果配置文件中没有找到相应的字段 说明不是一个TorosamyItem
        if (itemKey == "" || !ItemManager.items.containsKey(itemKey)) { return }
        val inventoryType = event.inventory.type
        for (blackContainer in ConfigUtil.getMainConfig().blackContainer) {
            if(blackContainer != "CRAFTING" && inventoryType == InventoryType.valueOf(blackContainer)) { event.isCancelled = true }
        }
    }

    @EventHandler
    fun onClick(event: InventoryClickEvent) {
        //处理被光标拿起的物品
        val item = event.cursor
        if(item.type == Material.AIR) return
        var itemKey: String = ""
        //判断是否是一个TorosamyItem
        NBT.get(item) { nbt -> itemKey = nbt.getString("TorosamyItem") }
        //如果配置文件中没有找到相应的字段 说明不是一个TorosamyItem
        if (itemKey == "" || !ItemManager.items.containsKey(itemKey)) { return }
        for (blackContainer in ConfigUtil.getMainConfig().blackContainer) {
            if(blackContainer == "CRAFTING" && event.slotType == InventoryType.SlotType.CRAFTING) { event.isCancelled = true }
        }
    }

    @EventHandler
    fun onHopperGetItem(event: InventoryPickupItemEvent) {
        val itemStack = event.item.itemStack
        var itemKey: String = ""
        //判断是否是一个TorosamyItem
        NBT.get(itemStack) { nbt -> itemKey = nbt.getString("TorosamyItem") }
        //如果配置文件中没有找到相应的字段 说明不是一个TorosamyItem
        if (itemKey == "" || !ItemManager.items.containsKey(itemKey)) { return }
        for (blackContainer in ConfigUtil.getMainConfig().blackContainer) {
            if(blackContainer == "HOPPER") { event.isCancelled = true }
        }
    }
}