package com.example.testingthings.ui.tictactoe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.view.children
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.testingthings.R

val Button.ismyButtonClickable: Boolean
    get() = this.text.isEmpty()

class TicTacToeFragment : Fragment() {

    private val viewModel: TicTacToeViewModel by activityViewModels()

    private var linearLayoutOne: LinearLayout? = null
    private var linearLayoutTwo: LinearLayout? = null
    private var linearLayoutThree: LinearLayout? = null

    companion object {
        val TAG = TicTacToeFragment::class.java.simpleName
        @JvmStatic
        fun newInstance() = TicTacToeFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val v = inflater.inflate(R.layout.fragment_tic_tac_toe,container,false)
        linearLayoutOne = v.findViewById(R.id.row1)
        linearLayoutTwo = v.findViewById(R.id.row2)
        linearLayoutThree = v.findViewById(R.id.row3)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.gameReset.observe(viewLifecycleOwner, Observer {
            restartGame()
        })
        viewModel.stepsUpdated.observe(viewLifecycleOwner, Observer {
            it.forEachIndexed { index, element ->
                when (index) {
                    in 0..2 -> (linearLayoutOne?.children?.elementAt(index) as? Button)?.text = viewModel.playerSymbolForPlayerIndex(element)
                    in 3..5 -> (linearLayoutTwo?.children?.elementAt(index -3) as? Button)?.text = viewModel.playerSymbolForPlayerIndex(element)
                    in 6..8 -> (linearLayoutThree?.children?.elementAt(index -6) as? Button)?.text = viewModel.playerSymbolForPlayerIndex(element)
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.invalidate()
    }

    fun restartGame(){
        linearLayoutOne?.let {
            it.children.forEachIndexed { index, button ->
                (button as? Button)?.setOnClickListener {
                    buttonClicked(it as Button, index)
                }
                (button as? Button)?.text = ""
            }
        }
        linearLayoutTwo?.let {
            it.children.forEachIndexed { index, button ->
                (button as? Button)?.setOnClickListener {
                    buttonClicked(it as Button, index + 3)
                }
                (button as? Button)?.text = ""
            }
        }
        linearLayoutThree?.let {
            it.children.forEachIndexed { index, button ->
                (button as? Button)?.setOnClickListener {
                    buttonClicked(it as Button, index + 6)
                }
                (button as? Button)?.text = ""
            }
        }
    }

    private fun buttonClicked(view: Button, index: Int){
        if (view.ismyButtonClickable) {
            viewModel.buttonClicked(index)
        }
    }



}