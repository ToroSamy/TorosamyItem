package net.torosamy.torosamyItem.pojo.crop

import net.torosamy.torosamyCore.api.TorosamyCoreAPI
import net.torosamy.torosamyItem.api.TorosamyItemAPI
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.World
import org.bukkit.block.BlockFace
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Interaction
import org.bukkit.entity.ItemDisplay
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import java.util.*

class CropStage {
    private val item: ItemStack
    
    private val harvestable: Boolean
    
    public constructor(config: ConfigurationSection) {
        this.item = TorosamyCoreAPI.generateItem(config)
        
        this.harvestable = config.getBoolean("harvestable", false)
    }
    
    public fun getItemStack(): ItemStack {
        return this.item
    }
    
    public fun harvestable(): Boolean {
        return harvestable;
    }
}