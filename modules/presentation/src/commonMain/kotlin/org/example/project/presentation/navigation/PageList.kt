package org.example.project.presentation.navigation

/**
 * 프로젝트 전체 페이지 정의
 * - 계층적으로 페이지들을 구성
 * - route는 클래스명과 동일하게 작성하여 중복 제거
 */
sealed class Page(val route: String, val description: String) {

    /**
     * 시작 페이지 - 스플래시, 온보딩 등
     */
    sealed class Start(route: String, description: String) : Page("start/$route", description) {
        data object Splash : Start("splash", "스플래시 페이지")
        data object Onboarding : Start("onboarding", "온보딩 페이지")
    }

    /**
     * 메인 페이지들 - 주요 기능 화면
     */
    sealed class Main(route: String, description: String) : Page("main/$route", description) {
        data object Home : Main("home", "홈 페이지")
        data object UserList : Main("user_list", "사용자 목록 페이지")
        data object UserDetail : Main("user_detail", "사용자 상세 페이지")
        data object Profile : Main("profile", "프로필 페이지")
    }

    /**
     * 설정 관련 페이지들
     */
    sealed class Settings(route: String, description: String) :
        Page("settings/$route", description) {
        data object Main : Settings("main", "설정 메인 페이지")
        data object Theme : Settings("theme", "테마 설정 페이지")
        data object Language : Settings("language", "언어 설정 페이지")
    }

    companion object {
        const val START_DESTINATION = "start/splash"
    }
}