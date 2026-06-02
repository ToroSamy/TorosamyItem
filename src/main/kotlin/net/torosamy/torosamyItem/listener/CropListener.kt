package net.torosamy.torosamyItem.listener

import net.torosamy.torosamyItem.TorosamyItem
import net.torosamy.torosamyItem.api.TorosamyItemAPI
import net.torosamy.torosamyItem.pojo.CustomItem
import org.bukkit.*
import org.bukkit.block.BlockFace
import org.bukkit.entity.Interaction
import org.bukkit.entity.ItemDisplay
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.world.ChunkLoadEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.persistence.PersistentDataType
import java.util.*

class CropListener : Listener {

    private val TOROSAMY_CROP_TYPE = NamespacedKey(TorosamyItem.plugin, "TorosamyCropType")
    private val TOROSAMY_CROP_STAGE = NamespacedKey(TorosamyItem.plugin, "TorosamyCropStage")
    private val TOROSAMY_CROP_UUID = NamespacedKey(TorosamyItem.plugin, "TorosamyCropUUID")
    private val TOROSAMY_CROP_NEXT_GROW = NamespacedKey(TorosamyItem.plugin, "TorosamyCropNextGrow")

    init {
        startProximityGrowthTicker(TorosamyItem.plugin)
    }
    
    @EventHandler
    fun onPlantCrop(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_BLOCK || event.hand != EquipmentSlot.HAND) {
            return
        }

        val itemInHand = event.item ?: return

        if (!itemInHand.hasItemMeta() || !itemInHand.itemMeta.hasCustomModelData()) {
            return
        }

        val customItem = TorosamyItemAPI.getCustomItem(itemInHand) ?: return

        if (!customItem.crop.isEnable()) {
            return
        }

        val clickedBlock = event.clickedBlock ?: return

        if (clickedBlock.type != Material.FARMLAND || event.blockFace != BlockFace.UP) return

        val cropBlock = clickedBlock.getRelative(BlockFace.UP)

        if (!cropBlock.type.isAir) return

        val spawnLoc = cropBlock.location.add(0.5, 0.0, 0.5)

        val display = cropBlock.world.spawn(spawnLoc, ItemDisplay::class.java) { entity ->
            entity.setItemStack(customItem.crop.getSeedStage())
            entity.itemDisplayTransform = ItemDisplay.ItemDisplayTransform.FIXED
        }

        cropBlock.world.spawn(spawnLoc, Interaction::class.java) { interaction ->
            interaction.interactionWidth = 0.8f
            interaction.interactionHeight = 1.0f
            interaction.isResponsive = true

            val pdc = interaction.persistentDataContainer
            pdc.set(TOROSAMY_CROP_TYPE, PersistentDataType.STRING, TorosamyItemAPI.getItemKey(customItem.getItem()))
            pdc.set(TOROSAMY_CROP_STAGE, PersistentDataType.INTEGER, 0)
            pdc.set(TOROSAMY_CROP_UUID, PersistentDataType.STRING, display.uniqueId.toString())
            pdc.set(TOROSAMY_CROP_NEXT_GROW, PersistentDataType.LONG, System.currentTimeMillis() + customItem.crop.getGrowTimeMs())
        }


        cropBlock.world.playSound(cropBlock.location, Sound.ITEM_CROP_PLANT, 1.0f, 1.0f)

        event.isCancelled = true

        if (event.player.gameMode != GameMode.CREATIVE) {
            itemInHand.amount -= 1
        }
    }

    @EventHandler
    fun onChunkLoad(event: ChunkLoadEvent) {
        for (entity in event.chunk.entities) {
            if (entity is Interaction && entity.persistentDataContainer.has(TOROSAMY_CROP_TYPE, PersistentDataType.STRING)) {
                tryNaturalGrowth(entity)
            }
        }
    }

    @EventHandler
    fun onInteractCropEntity(event: PlayerInteractEntityEvent) {
        if (event.hand != EquipmentSlot.HAND) return
        val interaction = event.rightClicked as? Interaction ?: return

        tryNaturalGrowth(interaction)
        
        val pdc = interaction.persistentDataContainer
        val itemKey = pdc.get(TOROSAMY_CROP_TYPE, PersistentDataType.STRING) ?: return
        val stage = pdc.get(TOROSAMY_CROP_STAGE, PersistentDataType.INTEGER) ?: return
        val customItem = TorosamyItemAPI.getCustomItem(itemKey) ?: return

        if (customItem.crop.harvestable(stage)) {
            return
        }

        //催熟
        val itemInHand = event.player.inventory.getItem(event.hand)

        if (itemInHand.type != Material.BONE_MEAL) {
            return
        }

        if (event.player.gameMode != GameMode.CREATIVE) {
            itemInHand.amount -= 1
        }

        val uuid = pdc.get(TOROSAMY_CROP_UUID, PersistentDataType.STRING)

        if (uuid != null) {
            val displayEntity = interaction.world.getEntity(UUID.fromString(uuid)) as? ItemDisplay ?: return
            displayEntity.setItemStack(customItem.crop.getNextStage(stage))
        }

        pdc.set(TOROSAMY_CROP_STAGE, PersistentDataType.INTEGER, stage + 1)
        pdc.set(TOROSAMY_CROP_TYPE, PersistentDataType.STRING, itemKey)

        val loc = interaction.location.clone().add(0.0, 0.5, 0.0)

 
        interaction.location.world.spawnParticle(Particle.HAPPY_VILLAGER, loc, 15, 0.4, 0.4, 0.4)
        interaction.location.world.playSound(loc, Sound.ITEM_BONE_MEAL_USE, 1.0f, 1.0f)
        interaction.location.world.playSound(loc, Sound.BLOCK_CROP_BREAK, 1.0f, 1.5f)

        event.isCancelled = true
    }

    /**
     * 3. 左键挖掘逻辑
     */
    @EventHandler
    fun onHitCropEntity(event: EntityDamageByEntityEvent) {
        val interaction = event.entity as? Interaction ?: return
        val player = event.damager as? Player ?: return

        val itemKey = interaction.persistentDataContainer.get(TOROSAMY_CROP_TYPE, PersistentDataType.STRING) ?: return
        val stage = interaction.persistentDataContainer.get(TOROSAMY_CROP_STAGE, PersistentDataType.INTEGER) ?: return
        val customItem = TorosamyItemAPI.getCustomItem(itemKey) ?: return
        
        removeCrop(interaction, customItem)

        //收获
        if (customItem.crop.harvestable(stage)) {
            dropCrop(player, interaction.location, customItem, true)
            event.isCancelled = true
            return
        }

        dropCrop(player, interaction.location, customItem, false)
        event.isCancelled = true
    }


    @EventHandler
    fun onBreakFarmland(event: BlockBreakEvent) {
        val block = event.block
        if (block.type != Material.FARMLAND) return

        val cropLoc = block.getRelative(BlockFace.UP).location.add(0.5, 0.5, 0.5)
        val nearbyCrops = block.world.getNearbyEntities(cropLoc, 0.4, 0.4, 0.4) { it is Interaction }

        for (entity in nearbyCrops) {
            val interaction = entity as Interaction
            val itemKey = interaction.persistentDataContainer.get(TOROSAMY_CROP_TYPE, PersistentDataType.STRING) ?: continue
            val customItem = TorosamyItemAPI.getCustomItem(itemKey) ?: continue

            removeCrop(interaction, customItem)
            dropCrop(event.player, cropLoc, customItem, false)
        }
    }


    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        val blockLoc = event.block.location.add(0.5, 0.5, 0.5)
        val nearbyCrops = event.block.world.getNearbyEntities(blockLoc, 0.4, 0.4, 0.4) { it is Interaction }

        for (entity in nearbyCrops) {
            if ((entity as Interaction).persistentDataContainer.has(TOROSAMY_CROP_TYPE, PersistentDataType.STRING)) {
                event.isCancelled = true
                return
            }
        }
    }


    private fun removeCrop(interaction: Interaction, customItem: CustomItem) {
        val pdc = interaction.persistentDataContainer
        val loc = interaction.location

        val uuid = pdc.get(TOROSAMY_CROP_UUID, PersistentDataType.STRING)
        if (uuid != null) {
            interaction.world.getEntity(UUID.fromString(uuid))?.remove()
        }
        interaction.remove()
        
        loc.world.spawnParticle(Particle.ITEM, loc.clone().add(0.0, 0.3, 0.0), 20, 0.2, 0.2, 0.2, 0.05, customItem.getItem())
        loc.world.spawnParticle(Particle.CLOUD, loc.clone().add(0.0, 0.2, 0.0), 5, 0.2, 0.2, 0.2, 0.01)
        loc.world.playSound(loc, Sound.BLOCK_CROP_BREAK, 1.0f, 1.0f)
    }

    private fun dropCrop(player: Player, location: Location, customItem: CustomItem, harvestable: Boolean) {
        val item = customItem.getItem()
        
        item.amount = customItem.crop.getRandomSeedAmount()

        location.world.dropItemNaturally(location, item)

        if (!harvestable) {
            return
        }
        
        location.world.playSound(location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5f, 1.2f)
        customItem.crop.harvest(player, location)
    }

    private fun tryNaturalGrowth(interaction: Interaction) {
        val pdc = interaction.persistentDataContainer
        val itemKey = pdc.get(TOROSAMY_CROP_TYPE, PersistentDataType.STRING) ?: return
        val stage = pdc.get(TOROSAMY_CROP_STAGE, PersistentDataType.INTEGER) ?: return

        val customItem = TorosamyItemAPI.getCustomItem(itemKey) ?: return
        if (customItem.crop.harvestable(stage)) return

        val nextGrowTime = pdc.get(TOROSAMY_CROP_NEXT_GROW, PersistentDataType.LONG) ?: return
        val now = System.currentTimeMillis()

        // 时间还没到
        if (now < nextGrowTime) return

        // 计算跨越几个阶段
        val growDelayMs = customItem.crop.getGrowTimeMs()
        val overdueMs = now - nextGrowTime
        val stagesToGrow = 1 + (overdueMs / growDelayMs).toInt()

        var newStage = stage + stagesToGrow
        val maxStage = customItem.crop.getMaxStageIndex()

        if (newStage > maxStage) {
            newStage = maxStage
        }

        // 更新模型显示
        val uuid = pdc.get(TOROSAMY_CROP_UUID, PersistentDataType.STRING)
        if (uuid != null) {
            val displayEntity = interaction.world.getEntity(UUID.fromString(uuid)) as? ItemDisplay
            displayEntity?.setItemStack(customItem.crop.getStageItem(newStage))
        }
        
        pdc.set(TOROSAMY_CROP_STAGE, PersistentDataType.INTEGER, newStage)

        if (!customItem.crop.harvestable(newStage)) {
            // 如果还没完全成熟，计算下一个阶段的精准时间戳
            val remainingMs = overdueMs % growDelayMs
            pdc.set(TOROSAMY_CROP_NEXT_GROW, PersistentDataType.LONG, now + growDelayMs - remainingMs)
        }


        val loc = interaction.location.clone().add(0.0, 0.5, 0.0)
        interaction.world.spawnParticle(Particle.HAPPY_VILLAGER, loc, 5, 0.3, 0.3, 0.3)
    }


    private fun startProximityGrowthTicker(plugin: TorosamyItem) {
        Bukkit.getScheduler().runTaskTimer(plugin, Runnable {
            for (player in Bukkit.getOnlinePlayers()) {
                val nearbyEntities = player.getNearbyEntities(32.0, 32.0, 32.0)
                for (entity in nearbyEntities) {
                    if (entity is Interaction && entity.persistentDataContainer.has(TOROSAMY_CROP_TYPE, PersistentDataType.STRING)) {
                        tryNaturalGrowth(entity)
                    }
                }
            }
        }, 100L, 100L)
    }
}