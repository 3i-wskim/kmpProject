package org.example.project.presentation.settings

import kotlinx.browser.window

actual class PlatformSettingsStore {
    private val storage = window.localStorage

    actual fun getStringOrNull(key: String): String? = storage.getItem(key)

    actual fun putString(key: String, value: String) {
        storage.setItem(key, value)
    }

    actual fun remove(key: String) {
        storage.removeItem(key)
    }
}