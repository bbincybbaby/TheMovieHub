package me.bincy.movielist.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import dagger.hilt.android.AndroidEntryPoint
import me.bincy.movielist.R
import me.bincy.movielist.ui.viewmodel.SearchListViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var searchView: SearchView? = null
    private var navController: NavController? = null

    private val viewModel: SearchListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolBar = findViewById<Toolbar>(R.id.mainToolBar)
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.mainFragmentContainerView) as NavHostFragment?
        navController = navHostFragment?.navController
        navController?.let { setupActionBarWithNavController(it) }
        navController?.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.searchListFragment) {
                searchView?.onActionViewExpanded()
            } else {
                searchView?.onActionViewCollapsed()
            }
        }

        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_movie_list, menu)
                val searchItem = menu.findItem(R.id.search)
                searchView = searchItem.actionView as SearchView
                searchView?.setOnSearchClickListener {
                    if (navController?.currentDestination?.id == R.id.movieListingFragment) {
                        navController?.navigate(R.id.action_movieListingFragment_to_searchListFragment)
                    }
                }
                searchView?.setOnCloseListener {
                    navController?.navigateUp()
                    return@setOnCloseListener false
                }

                if (navController?.currentDestination?.id == R.id.searchListFragment) {
                    searchView?.onActionViewExpanded()
                    if (viewModel.queryData.value.isNotBlank())
                        searchView?.setQuery(viewModel.queryData.value, false)
                }

                searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        viewModel.setQuery(newText ?: "")
                        return true
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }
        })


    }

    override fun onSupportNavigateUp(): Boolean {
        return navController?.navigateUp() == true || super.onSupportNavigateUp()
    }
}