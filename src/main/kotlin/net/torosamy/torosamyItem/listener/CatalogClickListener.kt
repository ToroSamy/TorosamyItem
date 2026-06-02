package net.torosamy.torosamyItem.listener

import net.torosamy.torosamyItem.api.TorosamyItemAPI
import net.torosamy.torosamyItem.pojo.catalog.CatalogMenuHolder
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

class CatalogClickListener: Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onInventoryClick(event: InventoryClickEvent) {
        val inventory = event.view.topInventory
        
        if (!CatalogMenuHolder.isCatalogInventory(inventory)) {
            return
        }
        
        event.isCancelled = true

        if (event.whoClicked !is Player) {
            return
        }

        val player = event.whoClicked as Player

        val holder = inventory.holder as CatalogMenuHolder

        val catalogInventory = TorosamyItemAPI.getCatalog(holder.catalogName) ?: return

        if (event.isLeftClick) {
            catalogInventory.runLeftCommands(event.slot, player)
            return
        }
 
        if (event.isRightClick) {
            catalogInventory.runRightCommands(event.slot, player)
            return
        }
    }
}