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
import org.incendo.cloud.annotations.suggestion.Suggestions
import org.incendo.cloud.context.CommandContext
import org.incendo.cloud.context.CommandInput

class PlayerCommands {
    @Command("ti catalog <catalog>")
    @Permission("torosamyitem.catalog")
    @CommandDescription("打开图鉴")
    fun catalogPage(player: Player, @Argument(value = "catalog", suggestions = "catalog") catalogName: String) {
        val catalogInventory = TorosamyItemAPI.getCatalog(catalogName)

        if (catalogInventory == null) {
            player.sendMessage(MessageUtil.format(ConfigUtil.langConfig.pageError))
            return
        }
        
        catalogInventory.open(player)
    }
    
    @Suggestions("catalog")
    fun catalogSuggest(context: CommandContext<CommandSender>, input: CommandInput): List<String> {
        return TorosamyItemAPI.getCatalogNames()
    }

    @Suggestions("item")
    fun itemSuggest(context: CommandContext<CommandSender>, input: CommandInput): List<String> {
        return TorosamyItemAPI.getItemNames();
    }
}