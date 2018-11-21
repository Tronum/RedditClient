package tronum.redditclient.api

import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET



interface  RedditApiService{
    @GET("top.json")
    fun getTop(): Observable<RedditModel.TopResponse>

    companion object {
        val HOST = "https://www.reddit.com"

        fun create(): RedditApiService {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(
                    RxJava2CallAdapterFactory.create())
                .addConverterFactory(
                    GsonConverterFactory.create())
                .baseUrl(HOST)
                .build()
            return retrofit.create(RedditApiService::class.java)
        }
    }
}
