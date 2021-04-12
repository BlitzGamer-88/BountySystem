package com.blitzoffline.bountysystem.config.holder

import me.mattstudios.config.SettingsHolder
import me.mattstudios.config.annotations.Path

object GUI : SettingsHolder {

    @Path("menu.title")
    val TITLE = "&cBounties"
    @Path("menu.size")
    val SIZE = 27

    // ITEMS
    @Path("menu.items.filler.material")
    val FILLER_MATERIAL = "BLACK_STAINED_GLASS_PANE"
    @Path("menu.items.filler.name")
    val FILLER_NAME = " "
    @Path("menu.items.filler.lore")
    val FILLER_LORE = listOf<String>()

    @Path("menu.items.next-page.material")
    val NEXT_PAGE_MATERIAL = "PAPER"
    @Path("menu.items.next-page.name")
    val NEXT_PAGE_NAME = "&6Next"
    @Path("menu.items.next-page.lore")
    val NEXT_PAGE_LORE = listOf<String>()

    @Path("menu.items.previous-page.material")
    val PREVIOUS_PAGE_MATERIAL = "PAPER"
    @Path("menu.items.previous-page.name")
    val PREVIOUS_PAGE_NAME = "&6Previous"
    @Path("menu.items.previous-page.lore")
    val PREVIOUS_PAGE_LORE = listOf<String>()

    @Path("menu.items.bounty.material")
    val BOUNTY_MATERIAL = "PAPER"
    @Path("menu.items.bounty.name")
    val BOUNTY_NAME = "&6Previous"
    @Path("menu.items.bounty.lore")
    val BOUNTY_LORE = listOf<String>()
}