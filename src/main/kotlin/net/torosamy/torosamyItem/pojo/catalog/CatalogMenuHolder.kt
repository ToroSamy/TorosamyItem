package net.torosamy.torosamyItem.pojo.catalog

import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

class CatalogMenuHolder(val catalogName: String) : InventoryHolder {
    override fun getInventory(): Inventory {
        throw UnsupportedOperationException("This InventoryHolder is only used as a marker.")
    }

    companion object {
        fun isCatalogInventory(inventory: Inventory): Boolean {
            return inventory.holder is CatalogMenuHolder
        }
    }
}