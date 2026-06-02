package net.torosamy.torosamyItem.pojo

import net.torosamy.torosamyCore.api.TorosamyCoreAPI
import net.torosamy.torosamyCore.utils.NbtUtil
import net.torosamy.torosamyItem.api.TorosamyItemAPI
import net.torosamy.torosamyItem.pojo.command.sub.Consume
import net.torosamy.torosamyItem.pojo.command.sub.Interact
import net.torosamy.torosamyItem.pojo.command.sub.SlotCommand
import net.torosamy.torosamyItem.pojo.command.sub.Timer
import net.torosamy.torosamyItem.pojo.crop.Crop
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.ItemStack

class CustomItem(config: ConfigurationSection, key: String) {
    private val itemStack: ItemStack
    
    val update: Boolean
    
    val interact: Boolean
    
    val disappear: ArrayList<String> = arrayListOf()
    
    val preventShoot: Boolean
    
    val consume: Consume
    
    val timer: Timer

    val interactGroup = HashMap<String, Interact>()
    
    val attack: SlotCommand
    
    val defense: SlotCommand
    
    val isBlock: Boolean
    
    val crop: Crop
    
    fun getHashCode(): Int {
        return NbtUtil.getInteger(itemStack, TorosamyItemAPI.GET_TOROSAMY_HASH_CODE_KEY())
    }
    
    fun getKey(): String {
        return NbtUtil.getString(itemStack, TorosamyItemAPI.GET_TOROSAMY_ITEM_KEY())
    }
    
    fun getItem(): ItemStack {
        return itemStack.clone()
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

        this.update = config.getBoolean("update", true)

        this.interact = config.getBoolean("interact", false)
        
        this.disappear.addAll(config.getStringList("disappear"))
        
        this.isBlock = config.getBoolean("isBlock", false)
        
        this.crop = Crop(config.getConfigurationSection("crop"))
        
        this.consume = Consume(config.getConfigurationSection("consume"))
        
        this.timer = Timer(config.getConfigurationSection("timer"))

        this.interactGroup.putAll(Interact.generateCommandGroup(config.getConfigurationSection("commands")))
        
        this.attack = SlotCommand(config.getConfigurationSection("attack"))
        
        this.defense = SlotCommand(config.getConfigurationSection("defense"))
    }
}