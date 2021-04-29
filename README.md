# BountySystem
###### Do you hate someone? Place a bounty on their head and pay the first person to kill them.

### Features:
* [PlaceholderAPI](https://www.spigotmc.org/resources/6245/) support.
* [WorldGuard](https://dev.bukkit.org/projects/worldguard/) support.
* Lots of placeholders. soon™️
* Hex support (&#aaFF00).
* 99% Customizable.

! This plugin should be used on [PaperSpigot](https://papermc.io/downloads)

### User Commands:
Command | Permission | Description
--------|------------|------------
/bounty | bountysystem.open | Open a menu in which all existing bounties will be listed.
/bounty place \<target> \<amount> | bountysystem.place | Place a bounty on someone.
/bounty add \<bountyID> \<amount> | bountysystem.add | Increase the reward for one of the bounties you placed.
/bounty cancel \<bountyID> | bountysystem.cancel | Cancel a bounty that you've placed.

### Admin Commands:
Command | Permission | Description
--------|------------|------------
/bountyadmin cancel \<bountyID> | bountysystem.admin.cancel | Cancel bounties placed by other users.
/bountyadmin bypass \<player> | bountysystem.admin.bypass | Give someone the bountysystem.bypass permission.
/bountyadmin reload | bountysystem.admin.reload | Reload the plugin configuration.

### Permissions:
Permission | Description
-----------|------------
bountysystem.bypass | Bounties can't be placed on people with this permission.

### Placeholders:
Placeholder | Description
------------|------------
%bountysystem_target_<id>% | Provides the name of the person the bounty is placed on.
%bountysystem_payer_<id>% | Provides the name of the person that placed the bounty.
%bountysystem_amount_<id>% | Provides the amount the payer placed on the target's head.
%bountysystem_ids_<player>% | Provides a list of IDs for every bounty this player has placed.