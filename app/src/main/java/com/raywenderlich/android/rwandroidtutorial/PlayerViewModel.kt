

package com.raywenderlich.android.rwandroidtutorial

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.raywenderlich.android.rwandroidtutorial.database.Player
import com.raywenderlich.android.rwandroidtutorial.database.PlayerListItem
import com.raywenderlich.android.rwandroidtutorial.database.PlayersDatabase

open class PlayerViewModel(application: Application) : AndroidViewModel(application) {

  private val repository: PlayerRepository

  init {
    val playerDao = PlayersDatabase
        .getDatabase(application, viewModelScope, application.resources)
        .playerDao()
    repository = PlayerRepository(playerDao)
  }

  fun getAllPlayers(): LiveData<List<PlayerListItem>> {
    return repository.getAllPlayers()
  }
  fun setPlayer(player: Player) { repository.setPlayer(player)}
}