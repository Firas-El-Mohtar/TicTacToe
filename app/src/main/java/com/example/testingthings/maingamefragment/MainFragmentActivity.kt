package com.example.testingthings.maingamefragment

import android.app.ActivityManager
import android.content.*
import android.os.*
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import com.example.testingthings.R
import com.example.testingthings.history.HistoryFragment
import com.example.testingthings.login.LoginActivity
import com.example.testingthings.player.Player
import com.example.testingthings.services.GameService
import com.example.testingthings.settings.MySettingsFragment
import com.example.testingthings.ui.tictactoe.GameHeaderFragment
import com.example.testingthings.ui.tictactoe.TicTacToeFragment
import com.example.testingthings.ui.tictactoe.TicTacToeViewModel
import com.example.testingthings.ui.tictactoe.TicTacToeViewModelFactory
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import java.lang.ref.WeakReference

class MainFragmentActivity : AppCompatActivity(){

    lateinit var toggle: ActionBarDrawerToggle

    //kindof starting the viewmodel by viewModels<give the view model name> {place inside the factory player 1 and player 2 into respective objects}
    private val viewModel by viewModels<TicTacToeViewModel> { TicTacToeViewModelFactory(player1 = playerOne, player2 = playerTwo, prefs) }
    private val TAG = MainFragmentActivity::class.qualifiedName

    //five player 1 and player 2 their respective attributes and definitions
    lateinit var prefs: SharedPreferences
    lateinit var playerOne: Player
    lateinit var playerTwo: Player


    private var gameHeaderFragment: GameHeaderFragment? = null
    private var ticTacToeFragment: TicTacToeFragment? = null

    /** Messenger for communicating with the service.  */
    private var mServiceMessenger: Messenger? = null
    private val mActivityMessenger: Messenger = Messenger(
            ActivityHandler(ActivityWithMessenger@this)
    )
    /** Flag indicating whether we have called bind on the service.  */
    private var bound: Boolean = false

    /**
     * Class for interacting with the main interface of the service.
     */
    private val mConnection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // This is called when the connection with the service has been
            // established, giving us the object we can use to
            // interact with the service.  We are communicating with the
            // service using a Messenger, so here we get a client-side
            // representation of that from the raw IBinder object.
            mServiceMessenger = Messenger(service)
            bound = true
        }

        override fun onServiceDisconnected(className: ComponentName) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            mServiceMessenger = null
            bound = false
        }
    }

    internal class ActivityHandler(activity: MainFragmentActivity?) : Handler(Looper.getMainLooper()) {
        private val mActivity: WeakReference<MainFragmentActivity>
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                GameService.MSG_GET_TIMESTAMP -> {
                    Log.d(
                            TicTacToeFragment.TAG,
                            msg.data.getString(
                                    "timestamp"
                            ) ?: "")
                    val timestamp = msg.data.getLong("timestampLong")
                    mActivity.get()?.viewModel?.saveMatchHistory(timestamp)
                }
            }
        }

        init {
            mActivity = WeakReference<MainFragmentActivity>(activity)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        prefs = getPreferences(Context.MODE_PRIVATE)
        val playerOneName = intent.getStringExtra("displayname")
        playerOne = Player(1, "X", playerOneName.toString())
        playerTwo = Player(2, "O", "Bot")

//        intent.getStringExtra("displayname")?.let {
//            val playerOneComplete = Player(1, "X", it)
//            val playerTwoComplete = Player(2, "O", "Bot")
//        }
        if (savedInstanceState == null) {
            if (viewModel != null) {
                Log.d(TAG, viewModel.player1.playerName)
                Log.d(TAG, viewModel.player2.playerName)
                gameHeaderFragment = GameHeaderFragment.newInstance()
                ticTacToeFragment = TicTacToeFragment.newInstance()
                //when we want to call a function use the viewmodel with the function that needs to be called
                viewModel.resetGame()
                supportFragmentManager.beginTransaction()
                        .replace(R.id.container_header, gameHeaderFragment!!)
                        .replace(R.id.container_game, ticTacToeFragment!!)
                        .commitNow()
            }
        }

        viewModel.playerOneWon.observe(this@MainFragmentActivity, Observer {
            getTimeStamp()
        })
        viewModel.playerTwoWon.observe(this@MainFragmentActivity, Observer {
            getTimeStamp()
        })
        viewModel.draw.observe(this@MainFragmentActivity, Observer {
            getTimeStamp()
        })
        viewModel.gameReset.observe(this@MainFragmentActivity, Observer {
            resetTimeStamp()
        })
        viewModel.totalTurnsChanged.observe(this@MainFragmentActivity, Observer {
            if (it == 1)
                resetTimeStamp()
        })

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerlayout1)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        var navView: NavigationView = findViewById(R.id.navView1)
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.Login_screen -> {
                    Toast.makeText(applicationContext, "Clicked Item 1", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
                R.id.Settings -> {
                    supportFragmentManager.beginTransaction()
                            .remove(gameHeaderFragment!!)
                            .remove(ticTacToeFragment!!)
                            .replace(R.id.mainFrame, MySettingsFragment())
                            .commitNow()
                }
                R.id.History->{
                    supportFragmentManager.beginTransaction()
                        .remove(gameHeaderFragment!!)
                        .remove(ticTacToeFragment!!)
                        .remove(MySettingsFragment()!!)
                        .replace(R.id.mainFrame, HistoryFragment())
                        .commitNow()
                }

            }
            true
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        // Bind to the service
        Intent(this, GameService::class.java).also { intent ->
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        // Unbind from the service
        if (bound) {
            unbindService(mConnection)
            bound = false
        }
    }

    fun resetTimeStamp() {
        if (!bound) return
        // Create and send a message to the service, using a supported 'what' value
        try {
            val msg = Message.obtain(
                    null,
                    GameService.MSG_RESET_TIMESTAMP, 0, 0
            )
            mServiceMessenger?.send(msg)
        } catch (e: RemoteException) {
            e.printStackTrace()
        }

    }

    fun getTimeStamp() {
        if (!bound) return
        // Create and send a message to the service, using a supported 'what' value
        try {
            val msg = Message.obtain(
                    null,
                    GameService.MSG_GET_TIMESTAMP, 0, 0
            )
            msg.replyTo = mActivityMessenger
            mServiceMessenger?.send(msg)
        } catch (e: RemoteException) {
            e.printStackTrace()
        }

    }

}