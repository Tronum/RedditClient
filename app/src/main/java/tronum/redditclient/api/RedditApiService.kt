package tronum.redditclient.api

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface RedditApiService{
    @GET("/top.json")
    fun getTop(
        @Query("limit") limit: Int = 10,
        @Query("after") after: String = ""
    ): Call<RedditModel.TopResponse>

    companion object {
        private const val HOST = "https://www.reddit.com"

        fun create(): RedditApiService {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(HOST)
                .build()
            return retrofit.create(RedditApiService::class.java)
        }
    }
}
