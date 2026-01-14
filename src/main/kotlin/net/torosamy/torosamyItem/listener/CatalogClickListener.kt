package net.torosamy.torosamyItem.listener

import net.torosamy.torosamyItem.api.TorosamyItemAPI
import net.torosamy.torosamyItem.pojo.CatalogInventoryHolder
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

class CatalogClickListener: Listener {
    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val inventory = event.view.topInventory
        
        if (!CatalogInventoryHolder.isCatalogInventory(inventory)) {
            return
        }
        
        event.isCancelled = true

        if (event.whoClicked !is Player) {
            return
        }

        val player = event.whoClicked as Player

        val holder = inventory.holder as CatalogInventoryHolder

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