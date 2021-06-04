package kr.kro.fatcats.bookscanner.util

import androidx.core.view.GravityCompat

class Constants {


    companion object {
        // Server API IP
        const val BASE_DOMAIN                  = "https://api.base.com"
        // Drawer Direction
        const val DRAWER_TYPE                  = GravityCompat.END        //START,END
        const val DRAWER_TYPE_STRING           = "end"                    //START,END
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


}