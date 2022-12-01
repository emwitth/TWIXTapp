package com.example.twixtapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.GestureDetector.SimpleOnGestureListener
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
    private var mGestureDetector: GestureDetector? = null
    private var mScaleFactor = 1.0F
    private var lastFocusX = 0f
    private var lastFocusY = 0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = GameScreenBinding.inflate(inflater, container, false)

        scaleGestureDetector = ScaleGestureDetector(this.context, ScaleListener(this))
        mGestureDetector = GestureDetector(this.context, MyGestureListener(this))


        binding.root.setOnTouchListener(View.OnTouchListener { _, event ->
            if(scaleGestureDetector?.isInProgress == false) {
                mGestureDetector?.onTouchEvent(event)
            }
            scaleGestureDetector?.onTouchEvent(event)
            return@OnTouchListener true
        })

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        resetBoard()

        binding.exitButton.setOnClickListener {
            findNavController().navigate(R.id.action_GameScreen_to_HomeScreen)
        }

        binding.resetBoardButton.setOnClickListener {
            resetBoard()
        }

        binding.placePieceButton.setOnClickListener {
            binding.boardImage.confirmPeg()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun resetBoard() {
        mScaleFactor = 0.95f
        binding.boardImage.scaleX = mScaleFactor
        binding.boardImage.scaleY = mScaleFactor

        binding.boardImage.translationX = 0f
        binding.boardImage.translationY = 0f
    }

    private class ScaleListener constructor(var screen: GameScreen) : SimpleOnScaleGestureListener() {

        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            screen.lastFocusY = detector.focusY
            screen.lastFocusX = detector.focusX
            return true
        }

        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
            screen.mScaleFactor *= scaleGestureDetector.scaleFactor
            screen.mScaleFactor = 0.95f.coerceAtLeast(screen.mScaleFactor.coerceAtMost(10.0f))

            screen.binding.boardImage.scaleX = screen.mScaleFactor
            screen.binding.boardImage.scaleY = screen.mScaleFactor

            screen.binding.boardImage.translationX += scaleGestureDetector.focusX - screen.lastFocusX
            screen.binding.boardImage.translationY += scaleGestureDetector.focusY - screen.lastFocusY

            screen.lastFocusX = scaleGestureDetector.focusX
            screen.lastFocusY = scaleGestureDetector.focusY

            return true
        }
    }

    private class MyGestureListener(var screen: GameScreen) : SimpleOnGestureListener() {
        override fun onDown(event: MotionEvent?): Boolean {
            Log.d("rootbeer", "onDown: ")

            // don't return false here or else none of the other
            // gestures will work
            return true
        }

        override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
            if(e != null) {
                screen.binding.boardImage.touchPoint(e.x, e.y, screen.mScaleFactor)
            }
            return true
        }

        override fun onDoubleTap(e: MotionEvent?): Boolean {
            screen.resetBoard()
            return true
        }

        override fun onScroll(
            e1: MotionEvent?, e2: MotionEvent?,
            distanceX: Float, distanceY: Float
        ): Boolean {
            if(e1 != null && e2!= null) {
                screen.binding.boardImage.translationX -= distanceX
                screen.binding.boardImage.translationY -= distanceY
            }
            return true
        }

    }
}