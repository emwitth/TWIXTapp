package com.example.twixtapp

import android.annotation.SuppressLint
import android.graphics.PointF
import android.os.Bundle
import android.util.Log
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
    private var mScaleFactor = 1.0F

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

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
        private val viewportFocus = PointF()
        private var lastFocusX = 0F;
        private var lastFocusY = 0F;

        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            lastFocusY = detector.focusY
            lastFocusX = detector.focusX
            return true
        }

        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
            screen.mScaleFactor *= scaleGestureDetector.scaleFactor
            screen.mScaleFactor = 0.95f.coerceAtLeast(screen.mScaleFactor.coerceAtMost(10.0f))

            screen.binding.boardImage.scaleX = screen.mScaleFactor
            screen.binding.boardImage.scaleY = screen.mScaleFactor

            screen.binding.boardImage.translationX += scaleGestureDetector.focusX - lastFocusX
            screen.binding.boardImage.translationY += scaleGestureDetector.focusY - lastFocusY

            lastFocusX = scaleGestureDetector.focusX
            lastFocusY = scaleGestureDetector.focusY

            return true
        }
    }
}