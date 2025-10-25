package net.torosamy.torosamyItem.api

import net.torosamy.torosamyCore.api.TorosamyCoreAPI
import net.torosamy.torosamyCore.config.ConfigFile
import net.torosamy.torosamyCore.inventory.InventoryBlockerHolder
import net.torosamy.torosamyCore.utils.MessageUtil
import net.torosamy.torosamyCore.utils.NbtUtil
import net.torosamy.torosamyItem.TorosamyItem
import net.torosamy.torosamyItem.pojo.CustomItem
import net.torosamy.torosamyItem.pojo.ItemCommand
import net.torosamy.torosamyItem.utils.ConfigUtil
import org.bukkit.Bukkit
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class TorosamyItemAPI {
    companion object {
        private val items = HashMap<String, CustomItem>()
        
        private val CATALOG_DEFAULT_ITEM: ItemStack = loadCatalogDefaultItem()

        private val catalogs: HashMap<Int, Inventory> = HashMap()

        private const val TOROSAMY_ITEM_KEY: String = "TorosamyItem"
        
        private const val TOROSAMY_HASH_CODE_KEY: String = "HashCode"
        
        fun GET_TOROSAMY_ITEM_KEY(): String {
            return TOROSAMY_ITEM_KEY
        }

        fun GET_TOROSAMY_HASH_CODE_KEY(): String {
            return TOROSAMY_HASH_CODE_KEY
        }
        
        fun getCustomItem(itemKey: String) : CustomItem?{
            return items[itemKey]
        }
        
        fun getHashCode(itemStack: ItemStack): Int {
            return NbtUtil.getInteger(itemStack, TOROSAMY_HASH_CODE_KEY);
        }

        fun getItemKey(itemStack: ItemStack): String {
            return NbtUtil.getString(itemStack, TOROSAMY_ITEM_KEY);
        }

        fun getConfigName(itemStack: ItemStack): String {
            return NbtUtil.getString(itemStack, TOROSAMY_ITEM_KEY);
        }

        fun isTorosamyItem(itemStack: ItemStack?): Boolean {
            val itemKey: String? = NbtUtil.getString(itemStack, TOROSAMY_ITEM_KEY)
            return itemKey != null
        }
        
        public fun getCatalog(page: Int): Inventory? {
            return catalogs[page]
        }

        public fun loadCatalogs() {
            catalogs.clear()
            for (it in items.values) {
                if (ConfigUtil.mainConfig.catalogDefaultItem.slots.contains(it.catalogSlot)) {
                    continue
                }


                if (it.catalogSlot < 0) {
                    continue
                }

                if (it.catalogPage < 0) {
                    continue
                }

                val inventory = catalogs.get(it.catalogPage) ?: generateEmptyCatalog()

                val itemStack = it.itemStack

                if (itemStack.type.isItem) {
                    inventory.setItem(it.catalogSlot, itemStack.clone())
                }

                catalogs[it.catalogPage] = inventory
            }
        }

        private fun generateEmptyCatalog(): Inventory {
            val displayInv = Bukkit.createInventory(InventoryBlockerHolder.INVENTORY_BLOCKER_HOLDER, 54, MessageUtil.format(ConfigUtil.langConfig.catalogTitle))

            ConfigUtil.mainConfig.catalogDefaultItem.slots.forEach{
                if (CATALOG_DEFAULT_ITEM.type.isItem) {
                    displayInv.setItem(it, CATALOG_DEFAULT_ITEM.clone())
                }
            }
            return displayInv
        }

        private fun loadCatalogDefaultItem(): ItemStack {
            val config = ConfigFile(TorosamyItem.plugin, "config.yml").config

            return TorosamyCoreAPI.generateItem(config.getConfigurationSection("catalog-default-item"))
        }
        
        fun loadItems(){
            items.clear()

            TorosamyCoreAPI.getConfigs(TorosamyItem.plugin, listOf("Item")).values.forEach{
                for (itemName in it.getKeys(false)) {
                    val itemConfig = it.getConfigurationSection(itemName) ?: continue

                    val customItem = CustomItem(itemConfig, itemName)

                    items[itemName] = customItem
                }
            }
            
            Bukkit.getConsoleSender().sendMessage(MessageUtil.format(ConfigUtil.langConfig.loadItemsMessage.replace("%amount%", items.size.toString())))
        }
    }
}