package com.example.twixtapp

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.twixtapp.databinding.GameScreenBinding


/**
 * A simple [Fragment] subclass as the game screen
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

        // when screen is created, need to set the boardImage view to the correct data
        binding.boardImage.setElemens(
            viewModel.boardArray,
            viewModel.redWalls,
            viewModel.blackWalls,
            viewModel.tempWalls)
        binding.boardImage.invalidate()

        // instantiate gesture detectors
        scaleGestureDetector = ScaleGestureDetector(this.context, ScaleListener(this))
        mGestureDetector = GestureDetector(this.context, MyGestureListener(this))


        // setup gesture detectors
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

        // don't want to show unless won
        binding.winnerTextView.visibility = View.GONE

        // set to initial position, slightly smaller than full screen width
        resetBoardPosn()

        // go home on click of button
        binding.exitButton.setOnClickListener {
            findNavController().navigate(R.id.action_GameScreen_to_HomeScreen)
        }

        // reset board position on click of button
        binding.resetBoardButton.setOnClickListener {
            resetBoardPosn()
        }

        // confirm placement of the piece if a valid option has been chosen
        binding.placePieceButton.setOnClickListener {
            if(viewModel.hasChosenValidOption) {
                viewModel.confirmPeg()
                // set view to new data from view model
                binding.boardImage.setElemens(
                    viewModel.boardArray,
                    viewModel.redWalls,
                    viewModel.blackWalls,
                    viewModel.tempWalls)
                binding.boardImage.invalidate()
                checkWin()
            }
        }

        checkWin()
    }

    /**
     * destroys the view
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * resets the boards position to slightly smaller than screen width
     */
    fun resetBoardPosn() {
        mScaleFactor = 0.95f
        binding.boardImage.scaleX = mScaleFactor
        binding.boardImage.scaleY = mScaleFactor

        binding.boardImage.translationX = 0f
        binding.boardImage.translationY = 0f
    }

    /**
     * sets the text view to either red or black depending on who's turn it is
     * including which styles it needs for that player
     */
    private fun setTurnText() {
        if(viewModel.isRedTurn) {
            binding.turnTextView.text = "Red Turn"
            binding.turnTextView.setTextColor(Color.parseColor("#FF92322F"))
            binding.turnTextView.setShadowLayer(4f,0f,0f,Color.BLACK)
        }
        else {
            binding.turnTextView.text = "Black Turn"
            binding.turnTextView.setTextColor(Color.BLACK)
            binding.turnTextView.setShadowLayer(4f,0f,0f, Color.parseColor("#FF92322F"))
        }
    }

    /**
     * initiates the win check in the view model and acts on the result
     */
    private fun checkWin() {
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

    /**
     * makes visible the text view and sets it's style/contents
     */
    private fun setRedWon() {
        binding.winnerTextView.text = "RED WINS!"
        binding.winnerTextView.setTextColor(Color.parseColor("#FF92322F"))
        binding.winnerTextView.setShadowLayer(7f,0f,0f,Color.BLACK)
        binding.turnTextView.visibility = View.GONE
        binding.winnerTextView.visibility = View.VISIBLE
    }

    /**
     * makes visible the text view and sets it's style/contents
     */
    private fun setBlackWon() {
        binding.winnerTextView.text = "BLACK WINS!"
        binding.winnerTextView.setTextColor(Color.BLACK)
        binding.winnerTextView.setShadowLayer(7f,0f,0f, Color.parseColor("#FF92322F"))
        binding.turnTextView.visibility = View.GONE
        binding.winnerTextView.visibility = View.VISIBLE
    }

    /**
     * listener for scaling gesture. Needed to zoom into board
     */
    private class ScaleListener constructor(var screen: GameScreen) : SimpleOnScaleGestureListener() {

        /**
         * sets the last focus, otherwise carries over from last scale gesture
         */
        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            screen.lastFocusY = detector.focusY
            screen.lastFocusX = detector.focusX
            return true
        }

        /**
         * scales the board view according to the gesture, moves view as well according to focus movement
         */
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

    /**
     * listener for non-scaling gestures
     */
    private class MyGestureListener(var screen: GameScreen) : SimpleOnGestureListener() {
        override fun onDown(event: MotionEvent?): Boolean {

            // don't return false here or else none of the other
            // gestures will work
            return true
        }

        /**
         * single tap places peg and walls pre-image
         */
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

        /**
         * double tap resets the boards position
         */
        override fun onDoubleTap(e: MotionEvent?): Boolean {
            screen.resetBoardPosn()
            return true
        }

        /**
         * scroll pans the boards position (translation)
         */
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