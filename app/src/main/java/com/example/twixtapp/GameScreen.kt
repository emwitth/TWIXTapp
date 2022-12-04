package com.example.twixtapp

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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

    private lateinit var viewModel: GameViewModel

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = GameScreenBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(requireActivity())[GameViewModel::class.java]

        binding.boardImage.setElemens(
            viewModel.boardArray,
            viewModel.redWalls,
            viewModel.blackWalls,
            viewModel.tempWalls)
        binding.boardImage.invalidate()

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

        binding.winnerTextView.visibility = View.GONE

        resetBoard()

        binding.exitButton.setOnClickListener {
            findNavController().navigate(R.id.action_GameScreen_to_HomeScreen)
        }

        binding.resetBoardButton.setOnClickListener {
            resetBoard()
        }

        binding.placePieceButton.setOnClickListener {
            if(viewModel.hasChosenValidOption()) {
                viewModel.confirmPeg()
                binding.boardImage.setElemens(
                    viewModel.boardArray,
                    viewModel.redWalls,
                    viewModel.blackWalls,
                    viewModel.tempWalls)
                binding.boardImage.invalidate()
                if(viewModel.hasRedWon) {
                    setRedWon()
                }
                else if(viewModel.hasBlackWon) {
                    setBlackWon()
                }
                else {
                    setTurnText()
                }
            }
        }

        setTurnText()

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

    private fun setTurnText() {
        if(viewModel.isRedTurn()) {
            binding.textView.text = "Red Turn"
            binding.textView.setTextColor(Color.parseColor("#FF92322F"))
            binding.textView.setShadowLayer(4f,0f,0f,Color.BLACK)
        }
        else {
            binding.textView.text = "Black Turn"
            binding.textView.setTextColor(Color.BLACK)
            binding.textView.setShadowLayer(4f,0f,0f, Color.parseColor("#FF92322F"))
        }
    }

    private fun setRedWon() {
        binding.winnerTextView.text = "RED WINS!"
        binding.winnerTextView.setTextColor(Color.parseColor("#FF92322F"))
        binding.winnerTextView.setShadowLayer(7f,0f,0f,Color.BLACK)
        binding.textView.visibility = View.GONE
        binding.winnerTextView.visibility = View.VISIBLE
    }

    private fun setBlackWon() {
        binding.winnerTextView.text = "BLACK WINS!"
        binding.winnerTextView.setTextColor(Color.BLACK)
        binding.winnerTextView.setShadowLayer(7f,0f,0f, Color.parseColor("#FF92322F"))
        binding.textView.visibility = View.GONE
        binding.winnerTextView.visibility = View.VISIBLE
    }

    private class ScaleListener constructor(var screen: GameScreen) : SimpleOnScaleGestureListener() {

        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            screen.lastFocusY = detector.focusY
            screen.lastFocusX = detector.focusX
            return true
        }

        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
            screen.mScaleFactor *= scaleGestureDetector.scaleFactor
            screen.mScaleFactor = 0.95f.coerceAtLeast(screen.mScaleFactor.coerceAtMost(5.0f))

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

            // don't return false here or else none of the other
            // gestures will work
            return true
        }

        override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
            if(e != null) {
                val row = screen.binding.boardImage.calcRow(e.y, screen.mScaleFactor)
                val column = screen.binding.boardImage.calcColumn(e.x, screen.mScaleFactor)
                screen.viewModel.touchPoint(row, column)
                screen.binding.boardImage.setElemens(
                    screen.viewModel.boardArray,
                    screen.viewModel.redWalls,
                    screen.viewModel.blackWalls,
                    screen.viewModel.tempWalls)
                screen.binding.boardImage.invalidate()
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