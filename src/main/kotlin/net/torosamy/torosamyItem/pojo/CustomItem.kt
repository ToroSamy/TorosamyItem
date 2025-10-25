package net.torosamy.torosamyItem.pojo

import net.torosamy.torosamyCore.api.TorosamyCoreAPI
import net.torosamy.torosamyCore.utils.NbtUtil
import net.torosamy.torosamyItem.api.TorosamyItemAPI
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.ItemStack

class CustomItem(config: ConfigurationSection, key: String) {
    val itemStack: ItemStack

    val catalogPage: Int
    val catalogSlot: Int
    val commands = HashMap<String, ItemCommand>()
    
    val update: Boolean
    
    val leftConsume: Boolean
    val rightConsume: Boolean
    
    fun getHashCode(): Int {
        return NbtUtil.getInteger(itemStack, TorosamyItemAPI.GET_TOROSAMY_HASH_CODE_KEY())
    }
    
    fun getKey(): String {
        return NbtUtil.getString(itemStack, TorosamyItemAPI.GET_TOROSAMY_ITEM_KEY())
    }
    
    init {
        this.itemStack = TorosamyCoreAPI.generateItem(config)
        NbtUtil.setInteger(
            itemStack, 
            TorosamyItemAPI.GET_TOROSAMY_HASH_CODE_KEY(),
            TorosamyCoreAPI.saveString(config).hashCode()
        )
        NbtUtil.setString(
            itemStack, 
            TorosamyItemAPI.GET_TOROSAMY_ITEM_KEY(),
            key
        )
//        this.hashCode = TorosamyCoreAPI.saveString(config).hashCode()
//
//        this.key = key

        this.leftConsume = config.getBoolean("leftConsume", false)

        this.rightConsume = config.getBoolean("rightConsume", false)

        this.update = config.getBoolean("update", false)

        this.commands.putAll(ItemCommand.generateCommandGroup(config.getConfigurationSection("commands")))

        val location = config.getString("catalogLocation", "-1:-1")!!

        val split = location.split(":")
        if (split.size > 1) {
            this.catalogPage = split[0].toIntOrNull() ?: -1
            this.catalogSlot = split[1].toIntOrNull() ?: -1
        }else {
            this.catalogPage = -1
            this.catalogSlot = -1
        }
    }
}