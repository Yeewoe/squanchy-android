package net.squanchy.remoteconfig

import com.google.firebase.remoteconfig.FirebaseRemoteConfig

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class RemoteConfig(
    private val firebaseRemoteConfig: FirebaseRemoteConfig,
    private val debugMode: Boolean
) {

    private val cacheExpiryInSeconds: Long
        get() = if (debugMode) EXPIRY_IMMEDIATELY else EXPIRY_ONE_HOUR

    fun getString(key: String): String = firebaseRemoteConfig.getString(key)

    fun getBoolean(key: String) = firebaseRemoteConfig.getBoolean(key)

    fun getUpdatedBoolean(key: String): Single<Boolean> =
        getConfigValue { firebaseRemoteConfig.getBoolean(key) }

    private fun <T> getConfigValue(action: () -> T): Single<T> {
        return fetchAndActivate(cacheExpiryInSeconds)
            .andThen(Single.fromCallable { action() })
    }

    fun fetchNow(): Completable {
        return fetchAndActivate(EXPIRY_IMMEDIATELY)
            .subscribeOn(Schedulers.io())
    }

    private fun fetchAndActivate(cacheExpiryInSeconds: Long): Completable {
        return Completable.create { emitter ->
            firebaseRemoteConfig.fetch(cacheExpiryInSeconds)
                .addOnCompleteListener {
                    firebaseRemoteConfig.activateFetched()
                    emitter.onComplete()
                }
                .addOnFailureListener { exception ->
                    if (emitter.isDisposed) {
                        return@addOnFailureListener
                    }
                    emitter.onError(exception)
                }
        }
    }

    companion object {

        @Suppress("ObjectPropertyNaming") // It is a de-facto constant but we can't use const
        private val EXPIRY_IMMEDIATELY = TimeUnit.HOURS.toSeconds(0)

        @Suppress("ObjectPropertyNaming") // It is a de-facto constant but we can't use const
        private val EXPIRY_ONE_HOUR = TimeUnit.HOURS.toSeconds(1)
    }
}
