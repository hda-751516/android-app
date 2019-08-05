package co.selim.starter.booking

import androidx.lifecycle.ViewModel
import co.selim.starter.api.BookingService
import co.selim.starter.model.Booking
import io.reactivex.Single
import org.koin.core.KoinComponent
import org.koin.core.inject

class BookingViewModel : ViewModel(), KoinComponent {
    private val bookingService by inject<BookingService>()

    val booking: Single<Booking> = bookingService
        .getCurrentBooking()
        .cache()
}
