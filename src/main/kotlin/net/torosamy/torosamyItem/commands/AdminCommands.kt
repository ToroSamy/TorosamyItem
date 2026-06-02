package net.torosamy.torosamyItem.commands

import net.torosamy.torosamyCore.api.TorosamyCoreAPI
import net.torosamy.torosamyCore.utils.MessageUtil
import net.torosamy.torosamyItem.api.TorosamyItemAPI
import net.torosamy.torosamyItem.utils.ConfigUtil
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.incendo.cloud.annotations.*
import org.incendo.cloud.annotations.suggestion.Suggestions
import org.incendo.cloud.context.CommandContext
import org.incendo.cloud.context.CommandInput


class AdminCommands {
    @Command("ti reload [resourced]")
    @Permission("torosamyitem.reload")
    @CommandDescription("重载TorosamyItem配置文件")
    fun reloadConfig(sender: CommandSender, @Argument("resourced")@Default("false") resourced: Boolean) {
        ConfigUtil.reloadConfig()
        TorosamyItemAPI.loadItems()
        sender.sendMessage(MessageUtil.format(ConfigUtil.langConfig.reloadMessage))
        
        if (!resourced) {
            return
        }

        for (player in Bukkit.getOnlinePlayers()) {
            TorosamyItemAPI.updatePlayerResourcePack(player)
        }
        
    }

    @Command("ti drop <item> <player>")
    @Permission("torosamyitem.give")
    @CommandDescription("给予玩家自定义物品")
    fun dropItem(sender: CommandSender, @Argument("player") player: Player, @Argument(value = "item", suggestions = "item") itemName: String) {
        val customItem = TorosamyItemAPI.getCustomItem(itemName)

        if (customItem == null) {
            sender.sendMessage(MessageUtil.format(player, ConfigUtil.langConfig.customNotFound))
            return
        }
        val item = customItem.getItem()

        player.world.dropItemNaturally(player.location, item)
    }

    @Command("ti set-hand <item> <player>")
    @Permission("torosamyitem.give")
    @CommandDescription("给予玩家自定义物品")
    fun setPlayerHandItem(sender: CommandSender, @Argument("player") player: Player, @Argument(value = "item", suggestions = "item") itemName: String) {
        val customItem = TorosamyItemAPI.getCustomItem(itemName)

        if (customItem == null) {
            sender.sendMessage(MessageUtil.format(player, ConfigUtil.langConfig.customNotFound))
            return
        }
        val item = customItem.getItem()
        
        player.inventory.setItemInMainHand(item)
    }

    @Command("ti give <item> <player>")
    @Permission("torosamyitem.give")
    @CommandDescription("给予玩家自定义物品")
    fun givePlayerItem(sender: CommandSender, @Argument("player") player: Player, @Argument(value = "item", suggestions = "item") itemName: String) {
        giveMoreItem(sender, player, itemName, 1, true)
    }

    @Command("ti give <item> <player> <amount> [messaged]")
    @Permission("torosamyitem.give")
    @CommandDescription("给予指定数量的自定义物品")
    fun giveMoreItem(sender: CommandSender, @Argument("player") player: Player, @Argument(value = "item", suggestions = "item") itemName: String, @Argument("amount") amount: Int, @Argument("messaged")@Default("true") messaged: Boolean) {
        if (player.inventory.firstEmpty() == -1) {
            sender.sendMessage(MessageUtil.format(player, ConfigUtil.langConfig.packageOverflow))

            if (player.name != sender.name) {
                player.sendMessage(MessageUtil.format(player, ConfigUtil.langConfig.packageOverflow))
            }
        }
        val customItem = TorosamyItemAPI.getCustomItem(itemName)

        if (customItem == null) {
            sender.sendMessage(MessageUtil.format(player, ConfigUtil.langConfig.customNotFound))
            return
        }
        val item = customItem.getItem()
        item.amount = amount
        player.inventory.addItem(item)
        
        if (!messaged) {
            return
        }
        
        val message: String = MessageUtil.format(player, ConfigUtil.langConfig.giveSuccessful.replace("%item%", itemName))
        
        sender.sendMessage(message)
    }


    @Command("ti give <item>")
    @Permission("torosamyitem.give")
    @CommandDescription("给予自己自定义物品")
    fun giveSelfItem(sender: Player, @Argument(value = "item", suggestions = "item") itemName: String) {
        giveMoreItem(sender, sender, itemName, 1, true)
    }
    
    

    @Command(value = "ti animation <player>")
    @Permission("torosamyitem.animation")
    @CommandDescription("播放玩家的装备动画")
    fun animation(sender: CommandSender, @Argument("player") player: Player) {
        val equipment = player.equipment ?: return
        
        if (TorosamyItemAPI.canPlayAnimation(equipment.itemInMainHand) ||
            TorosamyItemAPI.canPlayAnimation(equipment.itemInOffHand)) {
            TorosamyCoreAPI.playAnimation(player)
        }
    }
    
    
    @Suggestions("item")
    fun suggestBoxes(context: CommandContext<CommandSender>, input: CommandInput): List<String> {
        return TorosamyItemAPI.getItemNames();
    }
}