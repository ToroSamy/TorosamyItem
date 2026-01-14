package net.torosamy.torosamyItem.pojo.command

import net.torosamy.torosamyCore.api.TorosamyCoreAPI
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player

class SubCommand {
    private val commands: ArrayList<String> = arrayListOf()

    private val denyCommands: HashMap<String, List<String>> = hashMapOf()
    
    private val holders: HashMap<String, String> = hashMapOf()
    
    public constructor(config: ConfigurationSection?) {
        if (config == null) {
            return
        }
        val section = config.getConfigurationSection("denyCommands")

        section?.getKeys(false)?.forEach{
            this.denyCommands[it] = section.getStringList(it)
        }

        this.commands.addAll(config.getStringList("commands"))
    }

    public fun runCommands(player: Player, holders: HashMap<String, String>) {
        TorosamyCoreAPI.runCommands(player, this.commands, this.denyCommands, holders)
    }
    
    public fun runCommands(player: Player) {
        TorosamyCoreAPI.runCommands(player, this.commands, this.denyCommands, holders)
    }

    public fun copyHolder(): HashMap<String, String> {
        return HashMap(holders)
    }
}