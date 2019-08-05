package co.selim.starter.api

import co.selim.starter.model.Booking
import io.reactivex.Single
import retrofit2.http.GET

interface BookingApi {
    @GET("booking")
    fun getCurrentBooking(): Single<Booking>
}
