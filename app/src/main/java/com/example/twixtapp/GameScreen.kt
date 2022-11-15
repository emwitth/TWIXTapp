package com.example.twixtapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.twixtapp.databinding.GameScreenBinding


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class GameScreen : Fragment() {

    private var _binding: GameScreenBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var scaleGestureDetector: ScaleGestureDetector? = null
    private var mScaleFactor = 1.0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = GameScreenBinding.inflate(inflater, container, false)

        binding.root.setOnTouchListener(View.OnTouchListener { _, event ->
            scaleGestureDetector?.onTouchEvent(event)
            return@OnTouchListener true
        })

        scaleGestureDetector = ScaleGestureDetector(this.context, ScaleListener(this))

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_GameScreen_to_HomeScreen)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private class ScaleListener constructor(var screen: GameScreen) : SimpleOnScaleGestureListener() {

        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
            screen.mScaleFactor *= scaleGestureDetector.scaleFactor
            screen.mScaleFactor = Math.max(0.1f, Math.min(screen.mScaleFactor, 10.0f))
            screen.binding.boardImage.setScaleX(screen.mScaleFactor)
            screen.binding.boardImage.setScaleY(screen.mScaleFactor)
            return true
        }
    }
}