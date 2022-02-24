package xyz.spacermarcelo.codelab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import xyz.spacermarcelo.codelab.databinding.FragmentMainBinding


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)
    }

}