## Dependency
- TorosamyCore
- PlaceholderAPI
## Function
- quickly configure items and categorize them according to folders
- automatically updating items
- items can be set to consume left or right clicks
- items can be bound with custom commands
- customize which containers are prohibited from containing items
## Usage
1. download [TorosamyCore](https://github.com/ToroSamy/TorosamyCore) and [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) as a dependency plugin
2. put the **dependencies** and this plugin into your plugins folder and start your server
3. Modify the default configuration file generated and restart your server
## Permission
- - **Usage:** /ti reload
  - **Description:** reload this plugin
  - **Permission:** torosamyitem.reload
  <br>
- - **Usage:** /ti give item-name player-name amount
  - **Description:** Provide a specified quantity of items to players
  - **Permission:** torosamyitem.give
  <br>
- - **Usage:** /ti give item-name player-name
  - **Description:** give a item to player
  - **Permission:** torosamyitem.give
  <br>
- - **Usage:** /ti give item-name
  - **Description:** give a item to self
  - **Permission:** torosamyitem.give
  <br>
- - **Usage:** /ti nbt
  - **Description:** display the hashcode and fields of the items in hand
  - **Permission:** torosamyitem.nbt
  <br>
- - **Usage:** /ti nbt item-name
  - **Description:** display the hashcode and fields of the corresponding item in the configuration file
  - **Permission:** torosamyitem.nbt
## Config
### lang.yml
```yml
reload-message: "&b[服务器娘]&a插件 &eTorosamyItem &a重载成功喵~"
package-overflow: "&b[服务器娘]&c玩家 &e%player_name% &c的背包已满 物品丢失"
command-cooldown: "&b[服务器娘]&c错误 您必须还要 &e{s} &c秒才能使用 &e{key}"
```
### config.yml
[more please click here](https://bukkit.windit.net/javadoc/org/bukkit/event/inventory/InventoryType.html)
```yml
black-container:
  - ANVIL
  - BEACON
  - BLAST_FURNACE
  #and more
```
### template custom-item
```yml
CustomName:
  displayName: "&6NAME"
  material: STONE
  lore:
    - '&e text1'
    - '&e text2'
  unbreakable: true
  enchantment:
    - DURABILITY:1
  update: true
  itemFlagList:
  - "HIDE_ATTRIBUTES"
  - "HIDE_UNBREAKABLE"
  - "HIDE_ENCHANTS"
  - "HIDE_POTION_EFFECTS"
  clearAttribute: false
  commands:
    custom1:
      cooldown: 0
      permission: ""
      console: true
      command: 'say hello'
      trigger:
        leftClick: false
        sneak: false
    custom2:
      cooldown: 0
      permission: ""
      console: true
      command: 'say hello %player_name%'
      trigger:
        leftClick: true
        sneak: false
  leftConsume: false
  rightConsume: false
```

## Contact Author
- qq: 1364596766
- website: https://www.torosamy.net

## License

[GPL-3.0 license](./LICENSE)
