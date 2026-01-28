package com.flick.app.ui.screens.show.core.video

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import timber.log.Timber

/**
 * Monitors network conditions to enable adaptive video buffering.
 * Returns network quality levels that can be used to adjust ExoPlayer buffer sizes.
 *
 * Based on TikTok's dynamic buffer adjustment optimization.
 */
class NetworkMonitor(private val context: Context) {

    /**
     * Network quality levels for adaptive buffering.
     */
    enum class NetworkQuality {
        /** No network or very poor connection (<150 Kbps) */
        POOR,
        /** Slow network like 3G (150-550 Kbps) */
        MODERATE,
        /** Good network like 4G/LTE (550 Kbps - 2 Mbps) */
        GOOD,
        /** Excellent network like WiFi/5G (>2 Mbps) */
        EXCELLENT
    }

    private val connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    /**
     * Gets the current network quality based on connection type and bandwidth.
     *
     * @return NetworkQuality indicating current network conditions
     */
    fun getNetworkQuality(): NetworkQuality {
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)

        if (capabilities == null) {
            Timber.d("NetworkMonitor: No network capabilities, returning POOR")
            return NetworkQuality.POOR
        }

        val quality = when {
            // Check for WiFi first (usually best)
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                getWifiQuality(capabilities)
            }
            // Cellular network
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                getCellularQuality(capabilities)
            }
            // Ethernet (usually excellent)
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                NetworkQuality.EXCELLENT
            }
            else -> NetworkQuality.MODERATE
        }

        Timber.d("NetworkMonitor: Network quality = $quality")
        return quality
    }

    private fun getWifiQuality(capabilities: NetworkCapabilities): NetworkQuality {
        // Use bandwidth estimation if available (API 21+)
        val downstreamBandwidth = capabilities.linkDownstreamBandwidthKbps

        return when {
            downstreamBandwidth >= 10000 -> NetworkQuality.EXCELLENT  // 10+ Mbps
            downstreamBandwidth >= 2000 -> NetworkQuality.GOOD        // 2-10 Mbps
            downstreamBandwidth >= 500 -> NetworkQuality.MODERATE     // 500 Kbps - 2 Mbps
            else -> NetworkQuality.POOR
        }
    }

    private fun getCellularQuality(capabilities: NetworkCapabilities): NetworkQuality {
        val downstreamBandwidth = capabilities.linkDownstreamBandwidthKbps

        return when {
            downstreamBandwidth >= 5000 -> NetworkQuality.EXCELLENT   // 5G or fast LTE
            downstreamBandwidth >= 2000 -> NetworkQuality.GOOD        // Good LTE
            downstreamBandwidth >= 500 -> NetworkQuality.MODERATE     // 3G/slow LTE
            else -> NetworkQuality.POOR                                // 2G/Edge
        }
    }

    /**
     * Checks if network is available.
     *
     * @return true if network is available
     */
    fun isNetworkAvailable(): Boolean {
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

    /**
     * Checks if the network is metered (e.g., cellular data with limits).
     * Useful for deciding whether to reduce video quality/prefetch.
     *
     * @return true if network is metered
     */
    fun isMeteredConnection(): Boolean {
        return connectivityManager.isActiveNetworkMetered
    }

    companion object {
        /**
         * Buffer configuration based on network quality.
         * These values are tuned for short-form video content like TikTok.
         */
        object BufferConfig {
            /**
             * Get minimum buffer duration in milliseconds based on network quality.
             * Faster networks can start with smaller buffers for quicker initial playback.
             */
            fun getMinBufferMs(quality: NetworkQuality): Int = when (quality) {
                NetworkQuality.POOR -> 30_000      // 30s - buffer more on slow networks
                NetworkQuality.MODERATE -> 20_000  // 20s
                NetworkQuality.GOOD -> 15_000      // 15s
                NetworkQuality.EXCELLENT -> 10_000 // 10s - quick start on fast networks
            }

            /**
             * Get maximum buffer duration in milliseconds based on network quality.
             */
            fun getMaxBufferMs(quality: NetworkQuality): Int = when (quality) {
                NetworkQuality.POOR -> 60_000      // 60s
                NetworkQuality.MODERATE -> 45_000  // 45s
                NetworkQuality.GOOD -> 35_000      // 35s
                NetworkQuality.EXCELLENT -> 25_000 // 25s
            }

            /**
             * Buffer size required before playback can start.
             */
            const val BUFFER_FOR_PLAYBACK_MS = 2_500

            /**
             * Buffer size required to resume after rebuffering.
             */
            const val BUFFER_FOR_REBUFFER_MS = 5_000
        }
    }
}
