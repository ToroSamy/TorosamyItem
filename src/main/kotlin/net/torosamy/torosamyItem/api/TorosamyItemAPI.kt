package net.torosamy.torosamyItem.api

import io.papermc.paper.datacomponent.DataComponentTypes
import net.kyori.adventure.resource.ResourcePackInfo
import net.kyori.adventure.resource.ResourcePackRequest
import net.kyori.adventure.text.Component
import net.torosamy.torosamyCore.api.TorosamyCoreAPI
import net.torosamy.torosamyCore.config.ConfigFile
import net.torosamy.torosamyCore.utils.MessageUtil
import net.torosamy.torosamyCore.utils.NbtUtil
import net.torosamy.torosamyItem.TorosamyItem
import net.torosamy.torosamyItem.pojo.catalog.CatalogInventory
import net.torosamy.torosamyItem.pojo.catalog.CatalogMenuHolder
import net.torosamy.torosamyItem.pojo.CustomItem
import net.torosamy.torosamyItem.utils.ConfigUtil
import org.bukkit.*
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Interaction
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import java.net.URI
import java.util.*
import kotlin.collections.HashMap

object TorosamyItemAPI {
    private val items = HashMap<String, CustomItem>()

    private var CATALOG_DEFAULT_ITEM: ItemStack = loadCatalogDefaultItem()

    private val catalogs: HashMap<String, CatalogInventory> = HashMap()

    private const val TOROSAMY_ITEM_KEY: String = "TorosamyItem"

    private const val TOROSAMY_HASH_CODE_KEY: String = "HashCode"
    
    private const val TOROSAMY_BLOCK_KEY: String = "TorosamyFurniture"

    public val EQUIPMENT_SLOTS = listOf(
        EquipmentSlot.HEAD, 
        EquipmentSlot.CHEST,
        EquipmentSlot.LEGS, 
        EquipmentSlot.FEET, 
        EquipmentSlot.HAND, 
        EquipmentSlot.OFF_HAND
    )
    
//    public fun updateBindEnable() {
//        BIND_ENABLE = TorosamyItem.plugin.server.pluginManager.isPluginEnabled("TorosamyBind")
//    }
//    
//    public fun bindEnable(): Boolean {
//        return BIND_ENABLE;
//    }
    
    fun GET_TOROSAMY_ITEM_KEY(): String {
        return TOROSAMY_ITEM_KEY
    }
    
    fun GET_TOROSAMY_BLOCK_KEY(): String {
        return TOROSAMY_BLOCK_KEY
    }

    fun GET_TOROSAMY_HASH_CODE_KEY(): String {
        return TOROSAMY_HASH_CODE_KEY
    }

    fun getCustomItem(itemKey: String) : CustomItem?{
        return items[itemKey]
    }

    fun getCatalogNames(): List<String> {
        return catalogs.keys.toList()
    }
    
    fun updatePlayerResourcePack(player: Player) {
        val packInfo = ResourcePackInfo.resourcePackInfo(
            UUID.fromString(ConfigUtil.mainConfig.resourcePackUuid),
            URI.create(ConfigUtil.mainConfig.resourcePackUrl),
            ConfigUtil.mainConfig.resourcePackSha
        )

        val request = ResourcePackRequest.resourcePackRequest()
            .packs(packInfo)
            .required(true)
            .prompt(Component.text(ConfigUtil.langConfig.resourcePack))
            .build()

        player.sendResourcePacks(request)
    }


    fun getItemNames(): List<String> {
        return items.keys.toList()
    }

    fun getHashCode(itemStack: ItemStack): Int {
        return NbtUtil.getInteger(itemStack, TOROSAMY_HASH_CODE_KEY);
    }

    fun canPlayAnimation(item: ItemStack): Boolean {
        if (!isTorosamyItem(item)) {
            return false
        }
        val customItem = getCustomItem(getItemKey(item)) ?: return false

        return customItem.getItem().hasData(DataComponentTypes.DEATH_PROTECTION)
    }
    
    fun getCustomItem(itemStack: ItemStack): CustomItem? {
        if (!isTorosamyItem(itemStack)) {
            return null
        }
        
        return getCustomItem(getItemKey(itemStack))
    }

    fun getItemKey(itemStack: ItemStack): String {
        return NbtUtil.getString(itemStack, TOROSAMY_ITEM_KEY);
    }
    
    fun isTorosamyItem(itemStack: ItemStack?): Boolean {
        val itemKey: String? = NbtUtil.getString(itemStack, TOROSAMY_ITEM_KEY)
        return itemKey != null
    }
    
    public fun getCatalog(catalogName: String): CatalogInventory? {
        return catalogs[catalogName]
    }


    private fun generateEmptyCatalog(catalogName: String): CatalogInventory {
        val displayInv = Bukkit.createInventory(CatalogMenuHolder(catalogName), 54, MessageUtil.format(ConfigUtil.langConfig.catalogTitle))

        for (i in 0 until 54) {
            if (CATALOG_DEFAULT_ITEM.type.isItem) {
                displayInv.setItem(i, CATALOG_DEFAULT_ITEM.clone())
            }
        }

        val catalogInventory = CatalogInventory(displayInv)

        val leftCommands = ConfigUtil.mainConfig.catalogDefaultItem.leftCommands
        val rightCommands = ConfigUtil.mainConfig.catalogDefaultItem.rightCommands

        for (i in 0 until 54) {
            catalogInventory.addLeftCommands(i, leftCommands)
            catalogInventory.addRightCommands(i, rightCommands)
        }

        return catalogInventory
    }

    private fun loadCatalogDefaultItem(): ItemStack {
        val config = ConfigFile(TorosamyItem.plugin, "config.yml").config

        return TorosamyCoreAPI.generateItem(config.getConfigurationSection("catalog-default-item"))
    }
    
    fun reloadCatalog(catalogConfig: ConfigurationSection, customItem: CustomItem) {
        val catalogName = catalogConfig.getString("name", null) ?: return
        
        val slot = catalogConfig.getInt("slot", -1)

        if (slot < 0) {
            return
        }

        val denyCommands = HashMap<String, List<String>>()

        val section = catalogConfig.getConfigurationSection("denyCommands")

        section?.getKeys(false)?.forEach { name->
            denyCommands[name] = section.getStringList(name)
        }


        val inventory = catalogs[catalogName] ?: generateEmptyCatalog(catalogName)

        val itemStack = customItem.getItem()
        
        if (itemStack.type.isItem) {
            inventory.addItem(
                itemStack,
                slot,
                catalogConfig.getStringList("rightCommands"),
                catalogConfig.getStringList("leftCommands"),
                denyCommands)
        }

        catalogs[catalogName] = inventory
    }

    fun loadItems(){
        items.clear()
        catalogs.clear()
        this.CATALOG_DEFAULT_ITEM = loadCatalogDefaultItem()
        TorosamyCoreAPI.getConfigs(TorosamyItem.plugin, listOf("Item")).values.forEach{
            for (itemName in it.getKeys(false)) {
                val itemConfig = it.getConfigurationSection(itemName) ?: continue

                val customItem = CustomItem(itemConfig, itemName)

                items[itemName] = customItem
                
                val catalogConfig = itemConfig.getConfigurationSection("catalog") ?: continue
                
                reloadCatalog(catalogConfig, customItem)
            }
        }

        Bukkit.getConsoleSender().sendMessage(MessageUtil.format(ConfigUtil.langConfig.loadItemsMessage.replace("%amount%", items.size.toString())))
    }
}