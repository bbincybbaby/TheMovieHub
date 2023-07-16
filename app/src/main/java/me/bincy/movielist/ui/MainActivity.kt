package me.bincy.movielist.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dagger.hilt.android.AndroidEntryPoint
import me.bincy.movielist.R
import me.bincy.movielist.ui.viewmodel.MovieListViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var closeBtn: View? = null
    private var searchView: SearchView? = null

    private val viewModel: MovieListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        removeMenuProvider(menuProvider)
        val toolBar = findViewById<Toolbar>(R.id.mainToolBar)
        setSupportActionBar(toolBar)
        enableDisableActionBarItems(true)
        toolBar.setNavigationOnClickListener {
            finish()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (searchView?.isIconified == false) {
                    searchView?.setQuery("", false)
                    searchView?.isIconified = true
                    enableDisableActionBarItems(true)
                } else {
                    isEnabled = false
                    onBackPressed()
                }
            }
        })

        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { _, insets ->
            val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            viewModel.setKeyboardVisibility(imeVisible)
            showSearchViewCloseButton()
            insets
        }

        addMenuProvider(menuProvider)
    }

    private val menuProvider = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.menu_movie_list, menu)
            val searchItem = menu.findItem(R.id.search)
            searchView = searchItem.actionView as SearchView
            searchView?.maxWidth = Integer.MAX_VALUE
            searchView?.isIconified = true

            searchView?.setOnSearchClickListener {
                searchView?.onActionViewExpanded()
                enableDisableActionBarItems(false)
            }

            closeBtn = searchView?.findViewById(androidx.appcompat.R.id.search_close_btn)
            closeBtn?.setOnClickListener {
                if (searchView?.query?.isNotBlank() == true) {
                    searchView?.setQuery("", false)
                } else {
                    searchView?.isIconified = true
                    enableDisableActionBarItems(true)
                }
            }

            val wasKeyboardVisible = viewModel.isKeyBoardVisible()
            if (viewModel.queryStateFlow.value.isNotBlank() || wasKeyboardVisible) {
                searchView?.onActionViewExpanded()
                searchView?.setQuery(viewModel.queryStateFlow.value, false)
                if (wasKeyboardVisible) {
                    searchView?.requestFocus()
                } else {
                    searchView?.clearFocus()
                }
                showSearchViewCloseButton()
            }

            searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    showSearchViewCloseButton()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    viewModel.setQuery(newText ?: "")
                    showSearchViewCloseButton()
                    return true
                }
            })
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return false
        }
    }

    fun enableDisableActionBarItems(isEnable: Boolean) {
        supportActionBar?.setDisplayShowTitleEnabled(isEnable)
        supportActionBar?.setDisplayHomeAsUpEnabled(isEnable)
        supportActionBar?.setDisplayShowHomeEnabled(isEnable)
        showSearchViewCloseButton()
    }

    private fun showSearchViewCloseButton() {
        if (searchView?.isIconified == false) {
            closeBtn?.isEnabled = true
            closeBtn?.visibility = View.VISIBLE
        }
    }
}