package com.geetgobindingh.githubrepoapp.ui.base

import androidx.lifecycle.ViewModel
import com.geetgobindingh.githubrepoapp.util.rx.scheduler.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import java.lang.ref.WeakReference

abstract class BaseViewModel<V : IView> constructor(
    iView: IView,
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable
) : ViewModel(), IViewModel<V> {

    //region Member variables
    protected val TAG = "BaseViewModel"
    private var iViewRef: WeakReference<V> = WeakReference(iView as V)
    protected var mSchedulerProvider: SchedulerProvider = schedulerProvider
    protected var mCompositeDisposable: CompositeDisposable = compositeDisposable
    //endregion

    //region Public API methods
    override val view: V?
        get() = iViewRef.get()

    override val isViewAttached: Boolean get() = view != null

    override fun onCleared() {
        mCompositeDisposable.dispose()
        super.onCleared()
    }
    //endregion
}