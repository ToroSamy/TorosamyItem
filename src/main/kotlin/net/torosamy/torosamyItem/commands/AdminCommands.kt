package net.torosamy.torosamyItem.commands

import me.clip.placeholderapi.PlaceholderAPI
import net.torosamy.torosamyCore.utils.MessageUtil
import net.torosamy.torosamyItem.manager.ItemManager
import net.torosamy.torosamyItem.utils.ConfigUtil
import net.torosamy.torosamyItem.utils.ItemUtil
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.incendo.cloud.annotations.Argument
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.CommandDescription
import org.incendo.cloud.annotations.Permission

class AdminCommands {
    @Command("tsi reload")
    @Permission("torosamyitem.reload")
    @CommandDescription("重载TorosamyItem配置文件")
    fun reloadConfig(sender: CommandSender) {
        ConfigUtil.reloadConfig()
        ItemManager.loadItem()
        sender.sendMessage(MessageUtil.text(ConfigUtil.getLangConfig().reloadMessage))
    }
    @Command("tsi give <itemName> <player>")
    @Permission("torosamyitem.give")
    @CommandDescription("给予玩家自定义物品")
    fun givePlayerItem(sender: CommandSender, @Argument("player") player: Player, @Argument("itemName") itemName: String) {
        if (player.inventory.firstEmpty() == -1) {
            player.sendMessage(MessageUtil.text(PlaceholderAPI.setPlaceholders(player, ConfigUtil.getLangConfig().packageOverflow)))
            sender.sendMessage(MessageUtil.text(PlaceholderAPI.setPlaceholders(player, ConfigUtil.getLangConfig().packageOverflow)))
        }
        ItemManager.items[itemName]?.let { ItemUtil.getItem(it,player) }?.let { player.inventory.addItem(it) }
    }
    @Command("tsi give <itemName>", requiredSender = Player::class)
    @Permission("torosamyitem.give")
    @CommandDescription("给予自己自定义物品")
    fun giveSelfItem(sender: CommandSender, @Argument("itemName") itemName: String) {
        val player = sender as Player
        if (player.inventory.firstEmpty() == -1) {
            player.sendMessage(MessageUtil.text(PlaceholderAPI.setPlaceholders(player, ConfigUtil.getLangConfig().packageOverflow)))
        }
        ItemManager.items[itemName]?.let { ItemUtil.getItem(it,player) }?.let { player.inventory.addItem(it) }
    }
}