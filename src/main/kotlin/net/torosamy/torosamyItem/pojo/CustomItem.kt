package net.torosamy.torosamyItem.pojo

import net.torosamy.torosamyCore.api.TorosamyCoreAPI
import net.torosamy.torosamyCore.inventory.InventoryBlockerHolder
import net.torosamy.torosamyCore.utils.MessageUtil
import net.torosamy.torosamyCore.utils.NbtUtil
import net.torosamy.torosamyItem.api.TorosamyItemAPI
import net.torosamy.torosamyItem.pojo.command.sub.Consume
import net.torosamy.torosamyItem.pojo.command.sub.Interact
import net.torosamy.torosamyItem.pojo.command.sub.SlotCommand
import net.torosamy.torosamyItem.pojo.command.sub.Timer
import net.torosamy.torosamyItem.utils.ConfigUtil
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class CustomItem(config: ConfigurationSection, key: String) {
    val itemStack: ItemStack
    
    val update: Boolean
    
    val interact: Boolean
    
    val disappear: ArrayList<String> = arrayListOf()
    
    val preventShoot: Boolean
    
    val consume: Consume
    
    val timer: Timer

    val interactGroup = HashMap<String, Interact>()
    
    val attack: SlotCommand
    
    val defense: SlotCommand
    
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
        this.preventShoot = config.getBoolean("preventShoot", this.itemStack.type == Material.CROSSBOW)

        this.update = config.getBoolean("update", false)

        this.interact = config.getBoolean("interact", false)
        
        this.disappear.addAll(config.getStringList("disappear"))
        
        this.consume = Consume(config.getConfigurationSection("consume"))
        
        this.timer = Timer(config.getConfigurationSection("timer"))

        this.interactGroup.putAll(Interact.generateCommandGroup(config.getConfigurationSection("commands")))
        
        this.attack = SlotCommand(config.getConfigurationSection("attack"))
        
        this.defense = SlotCommand(config.getConfigurationSection("defense"))
    }
}