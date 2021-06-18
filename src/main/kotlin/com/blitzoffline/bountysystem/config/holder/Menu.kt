package com.blitzoffline.bountysystem.config.holder

import me.mattstudios.config.SettingsHolder
import me.mattstudios.config.annotations.Path
import me.mattstudios.config.properties.Property

object Menu : SettingsHolder {

    @Path("menu.title")
    val TITLE = Property.create("&cBounties")

    @Path("menu.items.filler.material")
    val FILLER_MATERIAL = Property.create("BLACK_STAINED_GLASS_PANE")

    @Path("menu.items.filler.name")
    val FILLER_NAME = Property.create(" ")

    @Path("menu.items.filler.lore")
    val FILLER_LORE = Property.create(listOf<String>())

    @Path("menu.items.next-page.material")
    val NEXT_PAGE_MATERIAL = Property.create("PAPER")

    @Path("menu.items.next-page.name")
    val NEXT_PAGE_NAME = Property.create("&6Next")

    @Path("menu.items.next-page.lore")
    val NEXT_PAGE_LORE = Property.create(listOf<String>())

    @Path("menu.items.previous-page.material")
    val PREVIOUS_PAGE_MATERIAL = Property.create("PAPER")

    @Path("menu.items.previous-page.name")
    val PREVIOUS_PAGE_NAME = Property.create("&6Previous")

    @Path("menu.items.previous-page.lore")
    val PREVIOUS_PAGE_LORE = Property.create(listOf<String>())

    @Path("menu.items.bounty.name")
    val BOUNTY_NAME = Property.create("&6Previous")

    @Path("menu.items.bounty.lore")
    val BOUNTY_LORE = Property.create(listOf<String>())

}