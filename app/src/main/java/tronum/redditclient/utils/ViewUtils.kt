package tronum.redditclient.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import tronum.redditclient.R

fun ImageView.setImage(url: String) {
    Glide
        .with(context)
        .load(url)
        .apply(
            RequestOptions()
                .placeholder(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        )
        .into(this)
}
