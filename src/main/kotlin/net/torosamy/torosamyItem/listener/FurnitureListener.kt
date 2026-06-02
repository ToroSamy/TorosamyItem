package net.torosamy.torosamyItem.listener

import net.torosamy.torosamyItem.api.TorosamyItemAPI
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.ItemFrame
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot


class FurnitureListener : Listener {
    @EventHandler
    fun onPlaceCustomModel(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_BLOCK || event.hand != EquipmentSlot.HAND) {
            return
        }
        
        val itemInHand = event.item ?: return
        
        if (!itemInHand.hasItemMeta() || !itemInHand.itemMeta.hasCustomModelData()) {
            return
        }

        val customItem = TorosamyItemAPI.getCustomItem(itemInHand) ?: return
        
        if (!customItem.isBlock) {
            return
        }
        
        val clickedBlock = event.clickedBlock ?: return

        if (clickedBlock.type == Material.BARRIER) {
            event.isCancelled = true
            return
        }
        
        val face = event.blockFace

        val targetBlock = clickedBlock.getRelative(face)

        if (!targetBlock.type.isAir && targetBlock.type != Material.WATER) {
            return
        }

        clickedBlock.world.spawn(clickedBlock.getRelative(face).location, ItemFrame::class.java) { entity ->
            entity.setFacingDirection(face, true)
            entity.setItem(itemInHand.clone())
            entity.isVisible = false
            entity.isFixed = true
            entity.addScoreboardTag(TorosamyItemAPI.GET_TOROSAMY_BLOCK_KEY())
        }

        //展示框所在的方块


        targetBlock.setType(Material.BARRIER, false)

        if (event.player.gameMode != GameMode.CREATIVE) {
            itemInHand.amount -= 1
        }

        event.isCancelled = true 
    }
    
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onInteractFurniture(event: PlayerInteractEvent) {
        if (event.action != Action.LEFT_CLICK_BLOCK) return

        val block = event.clickedBlock ?: return
        
        if (block.type != Material.BARRIER) return

        val breakEvent = BlockBreakEvent(block, event.player)

        Bukkit.getPluginManager().callEvent(breakEvent)
        
        if (breakEvent.isCancelled) {
            event.isCancelled = true
            return
        }
        
        val furnitureFrame = block.world.getNearbyEntities(block.location.add(0.5, 0.5, 0.5), 0.5, 0.5, 0.5).firstOrNull {
            it is ItemFrame && it.scoreboardTags.contains(TorosamyItemAPI.GET_TOROSAMY_BLOCK_KEY())
        } as? ItemFrame ?: return

        if (event.player.gameMode != GameMode.CREATIVE) {
            block.world.dropItemNaturally(block.location, furnitureFrame.item)
        }

        furnitureFrame.remove()

        block.type = Material.AIR
    }
}