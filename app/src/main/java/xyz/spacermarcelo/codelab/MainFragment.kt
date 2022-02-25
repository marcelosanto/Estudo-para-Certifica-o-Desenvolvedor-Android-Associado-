package xyz.spacermarcelo.codelab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import xyz.spacermarcelo.codelab.databinding.FragmentMainBinding
import xyz.spacermarcelo.codelab.utils.navTo


class MainFragment : androidx.fragment.app.Fragment(R.layout.fragment_main) {

    private lateinit var binding: FragmentMainBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMainBinding.bind(view)
        binding.codelabToastSnake.setOnClickListener { navTo(R.id.toastSnakeFragment) }

    }

}