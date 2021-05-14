package it.oechsler.backup

import it.oechsler.backup.actions.BackupAction
import it.oechsler.backup.actions.CleanupAction
import org.bukkit.plugin.java.JavaPlugin

@Suppress("unused")
class Main : JavaPlugin() {

    override fun onLoad() {
        saveDefaultConfig()
    }

    override fun onEnable() {
        val cleanupInHours = config.getLong("cleanup")
        scheduleAction(CleanupAction(this), 60 * 60 * cleanupInHours)

        val backupInHours = config.getLong("backup")
        scheduleAction(BackupAction(this), 60 * 60 * backupInHours)
    }

    override fun onDisable() {
        runAction(BackupAction(this))
    }

    private fun runAction(action: Runnable) {
        server.scheduler.runTask(this, action)
    }

    private fun scheduleAction(action: Runnable, period: Long) {
        server.scheduler.scheduleSyncRepeatingTask(this, action,
            0, 25 * period)
    }

}