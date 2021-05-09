package io.arindam.socketx.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.arindam.socketx.R
import io.arindam.socketx.data.local.Repository
import io.arindam.socketx.data.model.CityAQI
import io.arindam.socketx.ui.chart.ChartFragment
import io.arindam.socketx.ui.home.HomeFragment
import io.arindam.socketx.ui.home.HomeViewModel
import io.arindam.socketx.util.ViewModelFactory
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI

/**
 * Created by Arindam Karmakar on 9/5/21.
 */

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "SocketX"
        const val WEB_SOCKET_URL = "ws://city-ws.herokuapp.com/"
        const val DATABASE_NAME = "city-aqi-db"
    }

    private val homeFragment = HomeFragment.newInstance()
    private val chartFragment = ChartFragment.newInstance()
    private var activeFragment: Fragment? = null

    private lateinit var viewModel: HomeViewModel
    private lateinit var webSocketClient: WebSocketClient

    // region Lifecycle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = initViewModel()
        loadFragments()
    }

    override fun onResume() {
        super.onResume()
        connect()
    }

    override fun onPause() {
        super.onPause()
        disconnect()
    }

    override fun onBackPressed() {
        if (chartFragment.isVisible) closeChart()
        else super.onBackPressed()
    }

    // endregion Lifecycle

    private fun initViewModel(): HomeViewModel {
        val model: HomeViewModel by viewModels { ViewModelFactory(Repository.getInstance(this)) }
        return model
    }

    // region Fragments

    private fun loadFragments() {
        supportFragmentManager.findFragmentByTag(ChartFragment.TAG) ?: supportFragmentManager
            .beginTransaction()
            .add(R.id.container_fragment, chartFragment, ChartFragment.TAG)
            .hide(chartFragment)
            .commitAllowingStateLoss()

        supportFragmentManager.findFragmentByTag(HomeFragment.TAG) ?: supportFragmentManager
            .beginTransaction()
            .add(R.id.container_fragment, homeFragment, HomeFragment.TAG)
            .commitAllowingStateLoss()

        activeFragment = supportFragmentManager.findFragmentByTag(HomeFragment.TAG) ?: homeFragment
    }

    private fun showFragment(fragment: Fragment) {
        activeFragment?.run {
            supportFragmentManager.beginTransaction().hide(this).show(fragment).commit()
            activeFragment = fragment
        }
    }

    // endregion Fragments

    // region WebSocket Connection

    private fun connect() {
        initWebSocket()
        webSocketClient.connect()
    }

    private fun disconnect() {
        webSocketClient.close()
    }

    private fun initWebSocket() {
        val cityWiseAQIUri = URI(WEB_SOCKET_URL)
        createWebSocketClient(cityWiseAQIUri)
    }

    private fun createWebSocketClient(cityWiseAQIUri: URI?) {
        webSocketClient = object : WebSocketClient(cityWiseAQIUri) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                Log.d(TAG, "onOpen() Called")
            }
            override fun onMessage(message: String?) {
                Log.i(TAG, "onMessage: $message")
                setUpCityWiseAQI(message)
            }
            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                Log.d(TAG, "onClose: code -> $code, reason -> $reason, remote -> $remote")
            }
            override fun onError(ex: Exception?) {
                Log.e(TAG, "onError: ${ex?.message}")
            }
        }
    }

    // endregion WebSocket Connection

    private fun setUpCityWiseAQI(message: String?) {
        message?.let {
            val list = parseMessage(it)
            viewModel.updateData(list)
        }
    }

    private fun parseMessage(message: String): List<CityAQI> {
        val moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(List::class.java, CityAQI::class.java)
        val adapter: JsonAdapter<List<CityAQI>> = moshi.adapter(type)
        return adapter.fromJson(message) as List<CityAQI>
    }

    fun openChart(cityName: String) {
        showFragment(chartFragment)
        chartFragment.updateCity(cityName)
    }

    private fun closeChart() {
        showFragment(homeFragment)
        chartFragment.removeObserver()
    }
}
