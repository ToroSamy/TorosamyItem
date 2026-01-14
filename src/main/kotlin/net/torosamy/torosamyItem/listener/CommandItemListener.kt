package net.torosamy.torosamyItem.listener

import net.torosamy.torosamyCore.utils.MessageUtil
import net.torosamy.torosamyItem.api.TorosamyItemAPI
import net.torosamy.torosamyItem.utils.ConfigUtil
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot

class CommandItemListener : Listener {
    @EventHandler
    fun onPlayerAction(event: PlayerInteractEvent) {
        val action = event.action

        if (action == Action.PHYSICAL) {
            return
        }
        val item = event.item ?: return

        if (item.type == Material.AIR) {
            return
        }
        if (!TorosamyItemAPI.isTorosamyItem(item)) return

        val customItem = TorosamyItemAPI.getCustomItem(TorosamyItemAPI.getItemKey(item)) ?: return
        
        val isRightClick = (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)
        
        val isLeftClick = (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK)

        if (isRightClick && !customItem.interact) {
            event.isCancelled = true
        }
        
        val player = event.player

        for (command in customItem.interactGroup.values) {
            if (isLeftClick && !command.leftClick) continue
            if (isRightClick && command.leftClick) continue
            
            if (command.onlyMainHand && event.hand != EquipmentSlot.HAND) {
                continue
            }
            
            if (command.sneak && !player.isSneaking) {
                continue
            }

            val remainTime = command.updateCooldown(player)

            if (remainTime == 0L) {
                command.execute(player)
                continue
            }

            val message = ConfigUtil.langConfig.commandCooldown
                .replace("{s}", remainTime.toString())
                .replace("{key}", command.key)

            player.sendMessage(MessageUtil.format(message))
        }
    }
}