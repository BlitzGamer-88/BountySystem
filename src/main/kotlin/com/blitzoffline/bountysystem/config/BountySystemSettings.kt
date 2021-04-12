package com.blitzoffline.bountysystem.config

import ch.jalu.configme.SettingsManagerImpl
import ch.jalu.configme.configurationdata.ConfigurationDataBuilder
import ch.jalu.configme.migration.PlainMigrationService
import ch.jalu.configme.resource.YamlFileResource
import com.blitzoffline.bountysystem.config.sections.Settings
import java.io.File

class BountySystemSettings(file: File) : SettingsManagerImpl(
    YamlFileResource(file),
    ConfigurationDataBuilder.createConfiguration(Settings::class.java),
    PlainMigrationService()
)
