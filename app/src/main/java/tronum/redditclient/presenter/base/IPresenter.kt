package tronum.redditclient.presenter.base

interface IPresenter {
    fun onCreate()
    fun onStart()
    fun onStop()
    fun onDestroy()
}
