package net.torosamy.torosamyItem.commands

import net.torosamy.torosamyCore.utils.MessageUtil
import net.torosamy.torosamyItem.utils.ConfigUtil
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.ArmorMeta
import org.incendo.cloud.annotations.Argument
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.CommandDescription
import org.incendo.cloud.annotations.Permission

class RemoveCommands {
    @Command(value = "ti remove trim <player>")
    @Permission("torosamyitem.remove.trim")
    @CommandDescription("删除装备的纹饰")
    fun removeTrim(sender: CommandSender, @Argument("player") player: Player) {
        if (!player.isOnline) {
            sender.sendMessage(MessageUtil.format(ConfigUtil.langConfig.playerNotFound))
            return
        }

        val item = player.inventory.itemInMainHand

        val meta = item.itemMeta

        if (meta == null) {
            sender.sendMessage(MessageUtil.format(ConfigUtil.langConfig.playerNotFound))
            return
        }

        if (meta !is ArmorMeta) {
            return
        }
        
        meta.trim = null
        item.setItemMeta(meta)
    }
}