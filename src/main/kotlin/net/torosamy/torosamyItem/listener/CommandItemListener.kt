package net.torosamy.torosamyItem.listener

import net.torosamy.torosamyCore.utils.MessageUtil
import net.torosamy.torosamyItem.api.TorosamyItemAPI
import net.torosamy.torosamyItem.utils.ConfigUtil
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class CommandItemListener : Listener {
    @EventHandler
    fun onPlayerAction(event: PlayerInteractEvent) {
        val action: Action = event.getAction()
        val material: Material = event.getMaterial()
        
        if (action == Action.PHYSICAL || material == Material.AIR) {
            return
        }

        val isRightClick: Boolean = (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)
        val isLeftClick: Boolean = (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK)

        val item = event.item ?: return

        if (!TorosamyItemAPI.isTorosamyItem(item)) {
            return
        }

        if(isRightClick) {
            event.isCancelled = true
        }

        val customItem = TorosamyItemAPI.getCustomItem(TorosamyItemAPI.getConfigName(item)) ?: return

        val player: Player = event.getPlayer()
        
        for (command in customItem.commands.values) {
            if (isLeftClick && !command.leftClick) {
                continue
            }
            if (isRightClick && command.leftClick) {
                continue
            }
            
            if (command.sneak && !player.isSneaking) {
                continue
            }
            
            if (!command.permission.isNullOrBlank()) {
                if (!player.hasPermission(command.permission)) {
                    return
                }
            }

            if (!command.updateCooldownTask(player)) {
                val message: String = ConfigUtil.langConfig.commandCooldown
                    .replace("{s}", command.getRemainingTime(player).toString())
                    .replace("{key}", command.key)

                player.sendMessage(MessageUtil.format(message))
                return
            }
  
            command.execute(player)
        }

    }
}