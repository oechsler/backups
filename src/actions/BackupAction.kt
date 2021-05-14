package it.oechsler.backup.actions

import net.lingala.zip4j.ZipFile
import org.bukkit.ChatColor
import org.bukkit.Server
import org.bukkit.plugin.Plugin
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class BackupAction(private val plugin: Plugin) : Runnable {

    private val server: Server = plugin.server
    private val prefix = "${ChatColor.DARK_GRAY}[${ChatColor.YELLOW}Backup${ChatColor.DARK_GRAY}]"

    override fun run() {
        server.broadcastMessage("$prefix ${ChatColor.GRAY}Backing up the server (may cause lag for a moment)")

        val worldContainer = server.worldContainer
        val worldNames = server.worlds.map { it.name }

        val wordFolders = worldNames.map {
            val worldPath = worldContainer.resolve(it)
            if (worldPath.exists()) {
                return@map worldPath
            }
            return@map null
        }
        .filterNotNull()

        val backupContainerName = plugin.config.getString("container") ?: "backup"
        val backupContainer = worldContainer.resolve(backupContainerName)
        if (!backupContainer.exists())
            backupContainer.mkdir()

        val currentTime = LocalDateTime.now()
        val timeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
        val timeStamp = timeFormatter.format(currentTime)

        val backupNamePrefix = plugin.config.getString("prefix") ?: "backup-"
        val backupName = "${backupNamePrefix}${timeStamp}"
        val backup = ZipFile("${backupContainer.absolutePath}/${backupName}.zip")
        wordFolders.forEach {
            backup.addFolder(it)
        }

        server.broadcastMessage("$prefix ${ChatColor.GRAY}" +
                "Saved backup to \"${ChatColor.YELLOW}${backupName}.zip${ChatColor.GRAY}\"")
    }

}