package com.example.testingthings.ui.tictactoe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.testingthings.R


class GameHeaderFragment : Fragment() {
    private val viewModel: TicTacToeViewModel by activityViewModels()

    private lateinit var playerOneTextEditable: TextView
    private lateinit var playerTwoTextEditable: TextView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_game_header, container, false)
        playerOneTextEditable = v.findViewById(R.id.text_view_p1)
        playerTwoTextEditable = v.findViewById(R.id.text_view_p2)
        val resetButton: Button = v.findViewById(R.id.button_reset)
        //set the text to what has been passed from the login screen to the main fragment activity to the viewmodel
        playerOneTextEditable.text = viewModel.player1.playerName
        playerTwoTextEditable.text = viewModel.player2.playerName

        savedInstanceState?.let {
            playerOneTextEditable.text = "${viewModel.player1.playerName}: " + "${viewModel.player1.playerStepCount}"
            playerTwoTextEditable.text = "${viewModel.player2.playerName}: " + "${viewModel.player2.playerStepCount}"

        }

        resetButton.setOnClickListener {
            viewModel.resetGame()
        }

        return v
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.playerOneTurn.observe(viewLifecycleOwner, Observer {
            playerOneTextEditable.text = "${viewModel.player1.playerName}: " + "${viewModel.player1.playerStepCount}"
        })
        viewModel.playerTwoTurn.observe(viewLifecycleOwner, Observer {
            playerTwoTextEditable.text = "${viewModel.player2.playerName}: " + "${viewModel.player2.playerStepCount}"
        })
        viewModel.playerOneWon.observe(viewLifecycleOwner, Observer {
            playerOneTextEditable.text = "${viewModel.player1.playerName}: Winner!"
            playerTwoTextEditable.text = "${viewModel.player2.playerName}"
        })
        viewModel.playerTwoWon.observe(viewLifecycleOwner, Observer {
            playerOneTextEditable.text = "${viewModel.player1.playerName}"
            playerTwoTextEditable.text = "${viewModel.player2.playerName}: Winner!"
        })
        viewModel.draw.observe(viewLifecycleOwner, Observer {
            playerOneTextEditable.text = "${viewModel.player1.playerName}: Draw"
            playerTwoTextEditable.text = "${viewModel.player2.playerName}: Draw"
        })
        viewModel.gameReset.observe(viewLifecycleOwner, Observer {
            playerOneTextEditable.text = "${viewModel.player1.playerName}"
            playerTwoTextEditable.text = "${viewModel.player2.playerName}"
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.invalidate()
    }

        companion object {
            @JvmStatic
            fun newInstance() = GameHeaderFragment()
        }
}