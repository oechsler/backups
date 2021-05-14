package it.oechsler.backup.actions

import org.bukkit.ChatColor
import org.bukkit.Server
import org.bukkit.plugin.Plugin

class CleanupAction(private val plugin: Plugin) : Runnable {

    private val server = plugin.server
    private val prefix = "${ChatColor.DARK_GRAY}[${ChatColor.YELLOW}Backup${ChatColor.DARK_GRAY}]"

    override fun run() {
        val worldContainer = server.worldContainer

        val backupContainerName = plugin.config.getString("container") ?: "backup"
        val backupContainer = worldContainer.resolve(backupContainerName)
        if (!backupContainer.exists())
            return

        val backups = backupContainer.listFiles()
            ?.asList()?.filterNotNull() ?: return

        val keepBackupCount = plugin.config.getInt("keep")
        val deletableBackups = backups.reversed().drop(keepBackupCount)
        deletableBackups.forEach {
            it.delete()
        }

        if (deletableBackups.isNotEmpty())
            server.broadcastMessage("$prefix ${ChatColor.GRAY}Cleaned up ${ChatColor.YELLOW}" +
                    "${deletableBackups.count()}${ChatColor.GRAY} old backups.")
    }

}