package net.torosamy.torosamyItem.commands

import me.clip.placeholderapi.PlaceholderAPI
import net.torosamy.torosamyCore.nbtapi.NBT
import net.torosamy.torosamyCore.utils.MessageUtil
import net.torosamy.torosamyItem.TorosamyItem
import net.torosamy.torosamyItem.manager.ItemManager
import net.torosamy.torosamyItem.utils.ConfigUtil
import net.torosamy.torosamyItem.utils.ItemUtil
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.incendo.cloud.annotations.Argument
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.CommandDescription
import org.incendo.cloud.annotations.Permission

class AdminCommands {
    @Command("ti reload")
    @Permission("torosamyitem.reload")
    @CommandDescription("重载TorosamyItem配置文件")
    fun reloadConfig(sender: CommandSender) {
        ConfigUtil.reloadConfig()
        ItemManager.loadItem()
        sender.sendMessage(MessageUtil.text(ConfigUtil.getLangConfig().reloadMessage))
    }
    @Command("ti give <itemName> <player>")
    @Permission("torosamyitem.give")
    @CommandDescription("给予玩家自定义物品")
    fun givePlayerItem(sender: CommandSender, @Argument("player") player: Player, @Argument("itemName") itemName: String) {
        if (player.inventory.firstEmpty() == -1) {
            player.sendMessage(MessageUtil.text(PlaceholderAPI.setPlaceholders(player, ConfigUtil.getLangConfig().packageOverflow)))
            if(sender is Player) sender.sendMessage(MessageUtil.text(PlaceholderAPI.setPlaceholders(player, ConfigUtil.getLangConfig().packageOverflow)))
            TorosamyItem.plugin.server.consoleSender.sendMessage(MessageUtil.text(PlaceholderAPI.setPlaceholders(player, ConfigUtil.getLangConfig().packageOverflow)))
        }
        ItemManager.items[itemName]?.let { ItemUtil.getItem(it,player) }?.let { player.inventory.addItem(it) }
    }

    @Command("ti give <itemName> <player> <amount>")
    @Permission("torosamyitem.give")
    @CommandDescription("给予指定数量的自定义物品")
    fun giveMoreItem(sender: CommandSender, @Argument("player") player: Player, @Argument("itemName") itemName: String, @Argument("amount") amount: Int) {
        if (player.inventory.firstEmpty() == -1) {
            player.sendMessage(MessageUtil.text(PlaceholderAPI.setPlaceholders(player, ConfigUtil.getLangConfig().packageOverflow)))
            if(sender is Player) sender.sendMessage(MessageUtil.text(PlaceholderAPI.setPlaceholders(player, ConfigUtil.getLangConfig().packageOverflow)))
            TorosamyItem.plugin.server.consoleSender.sendMessage(MessageUtil.text(PlaceholderAPI.setPlaceholders(player, ConfigUtil.getLangConfig().packageOverflow)))
        }
        ItemManager.items[itemName]?.let { val item = ItemUtil.getItem(it, player)
            item.amount = amount
            player.inventory.addItem(item)
        }
    }


    @Command("ti give <itemName>", requiredSender = Player::class)
    @Permission("torosamyitem.give")
    @CommandDescription("给予自己自定义物品")
    fun giveSelfItem(sender: CommandSender, @Argument("itemName") itemName: String) {
        val player = sender as Player
        if (player.inventory.firstEmpty() == -1) {
            player.sendMessage(MessageUtil.text(PlaceholderAPI.setPlaceholders(player, ConfigUtil.getLangConfig().packageOverflow)))
        }
        ItemManager.items[itemName]?.let { ItemUtil.getItem(it,player) }?.let { player.inventory.addItem(it) }
    }

    @Command("ti nbt <itemName>")
    @Permission("torosamyitem.nbt")
    @CommandDescription("显示CustomItem的NBT")
    fun showItemNBT(sender: CommandSender, @Argument("itemName") itemName: String) {
        ItemManager.items[itemName]?.let {
            sender.sendMessage("HashCode: " + it.hashCode)
            sender.sendMessage("TorosamyItem: "+ it.key)
        }
    }

    @Command("ti nbt", requiredSender = Player::class)
    @Permission("torosamyitem.nbt")
    @CommandDescription("显示手上物品的NBT")
    fun showHandNBT(sender: CommandSender) {
        val player = sender as Player
        val itemInMainHand: ItemStack = player.inventory.itemInMainHand
        if (itemInMainHand.type == Material.AIR) return
        NBT.get(itemInMainHand) { nbt->
            sender.sendMessage("HashCode: " + nbt.getInteger("HashCode"))
            sender.sendMessage("TorosamyItem: "+ nbt.getString("TorosamyItem"))
        }
    }

}