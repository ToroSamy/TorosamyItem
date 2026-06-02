package net.torosamy.torosamyItem.pojo.crop

import net.torosamy.torosamyCore.api.TorosamyCoreAPI
import net.torosamy.torosamyItem.api.TorosamyItemAPI
import net.torosamy.torosamyItem.pojo.command.SubCommand
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class Crop {
    private val stages: ArrayList<ItemStack> = arrayListOf()
    
    private val subCommand: SubCommand
    
    private val fruitItemKey: String?
    
    private val minFruitAmount: Int
    
    private val maxFruitAmount: Int
    
    private val minSeedAmount: Int

    private val maxSeedAmount: Int
    
    private val growTime: Int

    public constructor(config: ConfigurationSection?) {
        subCommand = SubCommand(config)
        
        fruitItemKey = config?.getString("fruitItemKey", null)

        minFruitAmount = config?.getInt("minFruitAmount", 1) ?: 1
        
        maxFruitAmount = config?.getInt("maxFruitAmount", 1) ?: 1
        
        growTime = config?.getInt("growTime", 60) ?: 60

        minSeedAmount = config?.getInt("minSeedAmount", 0) ?: 0

        maxSeedAmount = config?.getInt("maxSeedAmount", 1) ?: 1
        
        val stageSection = config?.getConfigurationSection("stages") ?: return 

        for (stageName in stageSection.getKeys(false)) {
            val stageConfig = stageSection.getConfigurationSection(stageName) ?: continue

            stages.add(TorosamyCoreAPI.generateItem(stageConfig))
        }
    }
    
    public fun getGrowTimeMs(): Long {
        return growTime * 1000L
    }

    public fun getMaxStageIndex(): Int {
        return stages.size - 1
    }

    public fun getStageItem(stageIndex: Int): ItemStack {
        if (!isEnable() || stageIndex < 0 || stageIndex > getMaxStageIndex()) {
            return ItemStack(Material.AIR)
        }
        return stages[stageIndex]
    }

    public fun isEnable(): Boolean {
        return this.stages.isNotEmpty()
    }
    
    public fun getSeedStage(): ItemStack {
        if (!isEnable()) {
            return ItemStack(Material.AIR)
        }
        
        return this.stages[0]
    }
    
    public fun getNextStage(stage: Int): ItemStack {
        if (!isEnable() || stages.size <= stage + 1) {
            return ItemStack(Material.AIR)
        }

        return this.stages[stage + 1]
    }
    
    public fun harvestable(stageIndex: Int): Boolean {
        return stageIndex >= this.stages.size - 1
    }

    fun harvest(player: Player, location: Location) {
        fruitItemKey ?: return
        
        val customItem = TorosamyItemAPI.getCustomItem(fruitItemKey) ?: return

        val item = customItem.getItem()
        
        item.amount = getRandomFruitAmount()

        location.world.dropItemNaturally(location, item)
        
        subCommand.runCommands(player)
    }
    
    public fun getRandomFruitAmount() : Int {
        if (minFruitAmount <= maxFruitAmount) {
            return (minFruitAmount..maxFruitAmount).random()
        }
        
        return minFruitAmount
    }

    public fun getRandomSeedAmount() : Int {
        if (minSeedAmount <= maxSeedAmount) {
            return (minSeedAmount..maxSeedAmount).random()
        }

        return minSeedAmount
    }
}