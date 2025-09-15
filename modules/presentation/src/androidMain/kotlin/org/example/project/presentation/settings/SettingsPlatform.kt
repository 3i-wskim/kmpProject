package org.example.project.presentation.settings

import android.content.Context
import android.content.Context.MODE_PRIVATE

// Android 전역 Context 보관용 (앱 시작 시 초기화 필요)
object PresentationContextHolder {
    @Volatile
    private var _appContext: Context? = null

    fun init(context: Context) {
        _appContext = context.applicationContext
    }

    fun context(): Context = requireNotNull(_appContext) {
        "PresentationContextHolder.init(context) 가 호출되지 않았습니다. MainActivity.onCreate에서 초기화해주세요."
    }
}

actual class PlatformSettingsStore {
    private val prefs = PresentationContextHolder
        .context()
        .getSharedPreferences("app_prefs", MODE_PRIVATE)

    actual fun getStringOrNull(key: String): String? = prefs.getString(key, null)

    actual fun putString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    actual fun remove(key: String) {
        prefs.edit().remove(key).apply()
    }
}