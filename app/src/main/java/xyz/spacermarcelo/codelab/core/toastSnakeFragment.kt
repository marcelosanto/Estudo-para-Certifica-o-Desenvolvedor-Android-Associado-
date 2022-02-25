package xyz.spacermarcelo.codelab.core

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import xyz.spacermarcelo.codelab.R
import xyz.spacermarcelo.codelab.databinding.FragmentToastSnakeBinding
import xyz.spacermarcelo.codelab.utils.toast

class ToastSnakeFragment : androidx.fragment.app.Fragment(R.layout.fragment_toast_snake) {

    private lateinit var binding: FragmentToastSnakeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

        binding = FragmentToastSnakeBinding.bind(view)

        binding.toast.setOnClickListener {
            val msg = "Minha Mensagem para você!"
            Toast
                .makeText(requireContext(), msg, Toast.LENGTH_SHORT)
                .show()
        }

        binding.snake.setOnClickListener {
            Snackbar.make(view, "Oi Snake", Snackbar.LENGTH_SHORT).show()
        }

        binding.blank.setOnClickListener {
            Snackbar
                .make(view, "Snake with Action", Snackbar.LENGTH_INDEFINITE)
                .setAction("OKAY") { toast("I am a snake!") }
                .show()
        }

    }

}