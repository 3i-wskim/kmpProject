package org.example.project.presentation.settings

import platform.Foundation.NSUserDefaults

actual class PlatformSettingsStore {
    private val defaults = NSUserDefaults.standardUserDefaults

    actual fun getStringOrNull(key: String): String? = defaults.stringForKey(key)

    actual fun putString(key: String, value: String) {
        defaults.setObject(value, forKey = key)
    }

    actual fun remove(key: String) {
        defaults.removeObjectForKey(key)
    }
}

