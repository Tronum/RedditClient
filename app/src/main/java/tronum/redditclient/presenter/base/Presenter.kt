package tronum.redditclient.presenter.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import tronum.redditclient.view.base.IView

open class Presenter<V : IView>(var view: V?) : IPresenter {
    private var lifeCycleSubscriptions = CompositeDisposable()

    override fun onCreate() {}

    override fun onStart() {
        lifeCycleSubscriptions = CompositeDisposable()
    }

    override fun onStop() {
        lifeCycleSubscriptions.dispose()
    }

    override fun onDestroy() {}

    protected fun subscribe(vararg blocks: Disposable) {
        lifeCycleSubscriptions.addAll(*blocks)
    }
}