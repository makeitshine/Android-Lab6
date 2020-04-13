/*
 * Copyright (c) 2019 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.raywenderlich.android.rwandroidtutorial

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.android.rwandroidtutorial.database.Player
import com.raywenderlich.android.rwandroidtutorial.database.PlayerListItem
import com.raywenderlich.android.rwandroidtutorial.details.DetailsFragment
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Main Screen
 */
class MainActivity : AppCompatActivity() {
  private val DEFAULT_MSG_LENGTH_LIMIT = 100
  private lateinit var playerViewModel: PlayerViewModel
  private lateinit var adapter: PlayerAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    // Switch to AppTheme for displaying the activity
    setTheme(R.style.AppTheme)

    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    recyclerView.layoutManager =
        LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
    recyclerView.addItemDecoration(DividerItemDecoration(this@MainActivity, RecyclerView
        .VERTICAL))
    adapter = PlayerAdapter(mutableListOf())
    recyclerView.adapter = adapter
    adapter.setOnPlayerTapListener { player ->
      val fragment = DetailsFragment.newInstance(player)

      fragment.show(supportFragmentManager, "DetailsFragment")
    }

    // Enable Send button when there's text to send
    playerEditText.addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

      override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
        sendButton.isEnabled = charSequence.toString().trim { it <= ' ' }.isNotEmpty()
      }

      override fun afterTextChanged(editable: Editable) {}
    })
    playerEditText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT))

    // Send button sends a message and clears the EditText
    sendButton.setOnClickListener {
      val player = Player(0,playerEditText.getText().toString())

      playerViewModel?.setPlayer(player)
      // Clear input box
      playerEditText.setText("")
    }
    playerViewModel = ViewModelProvider(this).get(PlayerViewModel::class.java)
    playerViewModel.getAllPlayers().observe(this, Observer<List<PlayerListItem>> { players ->
      adapter.swapData(players)
    })
  }



}

