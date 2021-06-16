package kr.kro.fatcats.bookscanner.util

import androidx.core.view.GravityCompat

class Constants {


    companion object {

        // Server API IP
        const val BASE_DOMAIN                  = "http://seoji.nl.go.kr/"
        // Drawer Direction
        const val DRAWER_TYPE                  = GravityCompat.END        //START,END
        const val DRAWER_TYPE_STRING           = "end"                    //START,END
    }
    class MainText {
        companion object {
            const val TOTAL_TIME_VIEW_TEXT = "이때까지 읽은 총 시간 약: "
        }
    }

    class BookApi {
        companion object {
            const val RESULT_STYLE = "json"
            const val PAGE_NO_DEFAULT = "1"
            const val PAGE_SIZE_DEFAULT = "10"
        }
    }

    // Intent Extras Key
    class Extra {
        companion object {
            const val KEY_TOKEN                = "token"
            const val KEY_REFRESH_TOKEN        = "refresh_token"
        }
    }

    // Retrofit REQUEST Body key
    class Request {
        companion object {
            const val KEY_TOKEN                 = "token"
        }
    }

    // Retrofit RESPONSE Body key
    class Response {
        companion object {
            const val KEY_BODY   = "body"
            const val KEY_HEADER = "header"
        }
    }

    class ViewType {
        companion object {
            const val DEFAULT                   = 0
            const val TITLE                     = 1
        }
    }

    class DialogType {
        companion object {
            const val TIMER                   = "timer"
            const val REGISTER                = "register"
            const val IS_ROOM                 = "isRoom"
        }
    }


}