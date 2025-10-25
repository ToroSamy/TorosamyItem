package net.torosamy.torosamyItem.commands

import net.torosamy.torosamyCore.utils.MessageUtil
import net.torosamy.torosamyItem.api.TorosamyItemAPI
import net.torosamy.torosamyItem.utils.ConfigUtil
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.incendo.cloud.annotations.Argument
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.CommandDescription
import org.incendo.cloud.annotations.Permission

class PlayerCommands {
    @Command("catalog <page>", requiredSender = Player::class)
    @Permission("torosamyitem.view")
    @CommandDescription("打开图鉴")
    fun catalogPage(sender: CommandSender, @Argument("page") page: Int) {
        val player = sender as Player

        if (page < 1) {
            player.sendMessage(MessageUtil.format(ConfigUtil.langConfig.pageError))
            return
        }

        val inventory = TorosamyItemAPI.getCatalog(page)
        
        if (inventory == null) {
            player.sendMessage(MessageUtil.format(ConfigUtil.langConfig.pageError))
            return
        }
        
        player.openInventory(inventory)
    }
}