package co.selim.starter.api

import org.koin.core.KoinComponent
import org.koin.core.inject

class BookingService : KoinComponent {
    private val bookingApi by inject<BookingApi>()

    fun getCurrentBooking() = bookingApi.getCurrentBooking()
}
