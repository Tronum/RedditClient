package tronum.redditclient.data

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import tronum.redditclient.model.State

data class Listing(
    val data: LiveData<PagedList<PostData>>,
    val networkState: LiveData<State>,
    val refreshState: LiveData<State>,
    val refresh: () -> Unit,
    val retry: () -> Unit)