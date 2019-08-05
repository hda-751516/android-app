package co.selim.starter.ui

import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable

abstract class AutoDisposingFragment : Fragment() {
    protected val disposables = CompositeDisposable()

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }
}
