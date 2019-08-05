package co.selim.starter

import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.jakewharton.threetenabp.AndroidThreeTen
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.Region
import org.altbeacon.beacon.startup.BootstrapNotifier
import org.altbeacon.beacon.startup.RegionBootstrap
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class BookingApp : Application(), BootstrapNotifier {
    private val beaconRegion = Region("751516", null, null, null)
    private var regionBootstrap: RegionBootstrap? = null
    private var beaconManager: BeaconManager? = null

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)

        startKoin {
            modules(bookingAppModule)
            androidContext(this@BookingApp)
        }
        initLogging()

        regionBootstrap = RegionBootstrap(this, beaconRegion)
        beaconManager = BeaconManager.getInstanceForApplication(this)
            .apply {
                backgroundScanPeriod = BEACON_SCAN_DURATION_MILLIS
                backgroundBetweenScanPeriod = BEACON_SCAN_PAUSE_MILLIS
                foregroundScanPeriod = BEACON_SCAN_DURATION_MILLIS
                foregroundBetweenScanPeriod = BEACON_SCAN_PAUSE_MILLIS
            }
    }

    private fun initLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun didDetermineStateForRegion(state: Int, region: Region) = Unit

    override fun didEnterRegion(region: Region) {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val builder = NotificationCompat.Builder(this, "checkin")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Your shuttle is here")
            .setContentText("Tap here to check in.")
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)

        NotificationManagerCompat.from(this)
            .notify(NOTIFICATION_ID, builder.build())
    }

    override fun didExitRegion(region: Region) = NotificationManagerCompat
        .from(this)
        .cancel(NOTIFICATION_ID)
}

private const val NOTIFICATION_ID = 751516
val BEACON_SCAN_DURATION_MILLIS = 1100L
val BEACON_SCAN_PAUSE_MILLIS = 5000L
