package kr.kro.fatcats.bookscanner.api

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class ResponseInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            val response = chain.proceed(chain.request())
            return response.newBuilder()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .build()
        }catch (e : Exception){
            Log.e("ResponseInterceptor","$e")
            return chain.proceed(chain.request())
        }

    }
}