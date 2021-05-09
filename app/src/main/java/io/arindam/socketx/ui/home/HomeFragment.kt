package io.arindam.socketx.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import io.arindam.socketx.R
import io.arindam.socketx.data.local.Repository
import io.arindam.socketx.listener.CityAQIListener
import io.arindam.socketx.ui.MainActivity
import io.arindam.socketx.util.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * Created by Arindam Karmakar on 9/5/21.
 */

class HomeFragment private constructor() : Fragment(), CityAQIListener {

    companion object {
        const val TAG = "HomeFragment"
        fun newInstance(): HomeFragment = HomeFragment()
    }

    private var viewModel: HomeViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = initViewModel()
        viewModel?.getLatestList()?.observe(this as LifecycleOwner, { cityList ->
            if (cityList.isNotEmpty()) recycler_view.adapter = CityAQIAdapter(cityList, this)
        })
    }

    private fun initViewModel(): HomeViewModel {
        val model: HomeViewModel by viewModels { ViewModelFactory(Repository.getInstance(requireContext())) }
        return model
    }

    override fun onClicked(cityName: String) {
        (activity as MainActivity).openChart(cityName)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel?.getLatestList()?.removeObservers(this)
    }
}
