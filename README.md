# 正在制作当中
## Dependency
- TorosamyCore
- PlaceholderAPI
## Usage
1. download [TorosamyCore](https://github.com/ToroSamy/TorosamyCore) and [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) as a dependency plugin
2. put the **dependencies** and this plugin into your plugins folder and start your server
3. Modify the default configuration file generated and restart your server
## Permission
- - **Usage:** /tsi reload
  - **Description:** reload this plugin
  - **Permission:** torosamyitem.reload
  <br>
- - **Usage:** /tsi give item-name player-name
  - **Description:** give a item to player
  - **Permission:** torosamyitem.give
  <br>
- - **Usage:** /tsi give item-name
  - **Description:** give a item to self
  - **Permission:** torosamyitem.give
  <br>
- - **Usage:** /tsi nbt
  - **Description:** display the hashcode and fields of the items in hand
  - **Permission:** torosamyitem.nbt
    <br>
- - **Usage:** /tsi nbt item-name
  - **Description:** display the hashcode and fields of the corresponding item in the configuration file
  - **Permission:** torosamyitem.nbt
## Config
### lang.yml
```yml
reload-message: "&b[服务器娘]&a插件 &eTorosamyItem &a重载成功喵~"
package-overflow: "&b[服务器娘]&c玩家 &e%player_name% &c的背包已满 物品丢失"
```

## Function
- item manger
- automatically updating items
## FuturePlans
- bind owner
- consume item
- trigger command
- can be placed in a container
## Contact Author
- qq: 1364596766
- website: https://www.torosamy.net

## License

[MIT](./LICENSE)
