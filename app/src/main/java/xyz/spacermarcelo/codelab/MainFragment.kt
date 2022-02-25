package xyz.spacermarcelo.codelab

import android.os.Bundle
import android.view.View
import xyz.spacermarcelo.codelab.databinding.FragmentMainBinding
import xyz.spacermarcelo.codelab.utils.navTo


class MainFragment : androidx.fragment.app.Fragment(R.layout.fragment_main) {

    private lateinit var binding: FragmentMainBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMainBinding.bind(view)
        binding.codelabToastSnake.setOnClickListener { navTo(R.id.toastSnakeFragment) }
        binding.codelabNotification.setOnClickListener { navTo(R.id.notificationFragment) }

    }

}