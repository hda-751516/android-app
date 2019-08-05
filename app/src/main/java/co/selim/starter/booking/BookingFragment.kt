package co.selim.starter.booking

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.selim.starter.R
import co.selim.starter.ui.AutoDisposingFragment
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.booking_fragment.*
import net.glxn.qrgen.android.QRCode
import org.koin.androidx.viewmodel.ext.android.viewModel

class BookingFragment : AutoDisposingFragment() {
    private val viewModel by viewModel<BookingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.booking_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        disposables += viewModel.booking
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { booking ->
                startTextView.text = booking.start
                pickUpTimeTextView.text = booking.pickUpTime
                destinationTextView.text = booking.destination
                dropOffTimeTextView.text = booking.dropOffTime
            }

        disposables += viewModel.booking
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.bookingCode.toString() }
            .flatMap { it.toQrCode() }
            .subscribe { bitmap ->
                qrCodeImageView.setImageBitmap(bitmap)
            }
    }

    private fun String.toQrCode(): Single<Bitmap> = Single
        .fromCallable {
            QRCode.from(this)
                .withSize(1024, 1024)
                .bitmap()
        }
}
