package net.torosamy.torosamyItem.listener

import me.clip.placeholderapi.PlaceholderAPI
import net.torosamy.torosamyCore.nbtapi.NBT
import net.torosamy.torosamyCore.utils.MessageUtil
import net.torosamy.torosamyItem.TorosamyItem
import net.torosamy.torosamyItem.manager.ItemManager
import net.torosamy.torosamyItem.scheduler.CooldownTask
import net.torosamy.torosamyItem.utils.ConfigUtil
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class CommandItemListener : Listener {
    @EventHandler
    fun onPlayerAction(event: PlayerInteractEvent) {
        val player: Player = event.getPlayer()
        val action: Action = event.getAction()
        val material: Material = event.getMaterial()
        if (action == Action.PHYSICAL || material == Material.AIR) return

        val isRightClick: Boolean = (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)
        val isLeftClick: Boolean = (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK)

        //若手上没有物品则返回
        val item = event.item ?: return
        //判断物品是否是TorosamyItem
        var itemKey: String = ""
        NBT.get(item) { nbt -> itemKey = nbt.getString("TorosamyItem") }
        //如果配置文件中没有找到相应的字段 说明不是一个TorosamyItem
        if (itemKey == "" || !ItemManager.items.containsKey(itemKey)) return
        //如果是一个TorosamyItem则取消可以交互
        event.isCancelled = true
        //获取内存当中的物品
        val customItem = ItemManager.items[itemKey]!!

        for (command in customItem.commands.values) {
            if (command.leftClick && !isLeftClick) continue
            if (!command.leftClick && !isRightClick) continue
            if (command.sneak && !player.isSneaking) continue
            if (command.permission != "" && !player.hasPermission(command.permission)) continue

            if (command.cooldown > 0) {
                val cooldown = command.playerCooldown[player]?.cooldown
                if(cooldown != null && cooldown > 0) {
                    val message = MessageUtil.text(ConfigUtil.getLangConfig().commandCooldown.replace("{s}", cooldown.toString()).replace("{key}", command.key))
                    player.sendMessage(message)
                    return
                }else {
                    command.playerCooldown[player] = CooldownTask(command.cooldown)
                    command.playerCooldown[player]?.runTaskTimer(TorosamyItem.plugin,0L,20L)
                }
            }
            
            val commandString = MessageUtil.text(PlaceholderAPI.setPlaceholders(player, command.command))
            if (command.isConsole) Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandString)
            else {
                player.isOp = true
                Bukkit.dispatchCommand(player, commandString)
                player.isOp = false
            }
        }

    }
}