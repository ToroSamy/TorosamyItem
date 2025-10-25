package net.torosamy.torosamyItem.listener

import net.torosamy.torosamyItem.api.TorosamyItemAPI
import net.torosamy.torosamyItem.utils.ConfigUtil
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryPickupItemEvent
import org.bukkit.event.inventory.InventoryType

class MoveItemListener : Listener {
    @EventHandler
    fun onClickCurrent(event: InventoryClickEvent) {
        val item = event.currentItem ?: return
        
        if(item.type == Material.AIR) {
            return
        }
        
        if (!TorosamyItemAPI.isTorosamyItem(item)) {
            return
        }
        
        val inventoryType = event.inventory.type
        for (blackContainer in ConfigUtil.mainConfig.blackContainer) {
            if (blackContainer != "CRAFTING" && inventoryType == InventoryType.valueOf(blackContainer)) { 
                event.isCancelled = true 
            }
        }
    }

    @EventHandler
    fun onClick(event: InventoryClickEvent) {
        val item = event.cursor ?: return
        
        if(item.type == Material.AIR) {
            return
        }
        
        if (!TorosamyItemAPI.isTorosamyItem(item)) {
            return
        }
        
        for (blackContainer in ConfigUtil.mainConfig.blackContainer) {
            if (blackContainer == "CRAFTING" && event.slotType == InventoryType.SlotType.CRAFTING) { 
                event.isCancelled = true 
            }
        }
    }

    @EventHandler
    fun onHopperGetItem(event: InventoryPickupItemEvent) {
        val itemStack = event.item.itemStack
        if (!TorosamyItemAPI.isTorosamyItem(itemStack)) {
            return
        }
        for (blackContainer in ConfigUtil.mainConfig.blackContainer) {
            if (blackContainer == "HOPPER") { 
                event.isCancelled = true
            }
        }
    }
}