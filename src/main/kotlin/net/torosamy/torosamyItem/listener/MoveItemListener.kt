package net.torosamy.torosamyItem.listener

import net.torosamy.torosamyItem.manager.ItemManager
import net.torosamy.torosamyItem.utils.ConfigUtil
import net.torosamy.torosamyItem.utils.ItemUtil
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryPickupItemEvent
import org.bukkit.event.inventory.InventoryType

class MoveItemListener : Listener {
    @EventHandler
    fun onClickCurrent(event: InventoryClickEvent) {
        //处理被点击的物品
        val item = event.currentItem ?: return
        if(item.type == Material.AIR) {
            return
        }
        //判断物品是否是TorosamyItem
        if (!ItemUtil.isTorosamyItem(item)) {
            return
        }
        val inventoryType = event.inventory.type
        for (blackContainer in ConfigUtil.mainConfig.blackContainer) {
            if(blackContainer != "CRAFTING" && inventoryType == InventoryType.valueOf(blackContainer)) { event.isCancelled = true }
        }
    }

    @EventHandler
    fun onClick(event: InventoryClickEvent) {
        //处理被光标拿起的物品
        val item = event.cursor
        if(item?.type == Material.AIR) return
        //判断物品是否是TorosamyItem
        if (!ItemUtil.isTorosamyItem(item)) {
            return
        }
        for (blackContainer in ConfigUtil.mainConfig.blackContainer) {
            if(blackContainer == "CRAFTING" && event.slotType == InventoryType.SlotType.CRAFTING) { event.isCancelled = true }
        }
    }

    @EventHandler
    fun onHopperGetItem(event: InventoryPickupItemEvent) {
        val itemStack = event.item.itemStack
        if (!ItemUtil.isTorosamyItem(itemStack)) {
            return
        }
        for (blackContainer in ConfigUtil.mainConfig.blackContainer) {
            if(blackContainer == "HOPPER") { event.isCancelled = true }
        }
    }
}