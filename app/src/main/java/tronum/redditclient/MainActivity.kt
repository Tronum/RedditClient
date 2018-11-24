package tronum.redditclient

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import tronum.redditclient.utils.replaceFragment
import tronum.redditclient.view.base.BaseFragment
import tronum.redditclient.view.MainScreenFragment

class MainActivity : AppCompatActivity() {
    private val visibleFragments: List<Fragment>
        get() {
            val visible = ArrayList<Fragment>()
            val fm = supportFragmentManager
            val fragments = fm.fragments
            for (fragment in fragments) {
                if (fragment != null && fragment.isVisible && fragment.userVisibleHint)
                    visible.add(fragment)
            }
            return visible
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)

        if (savedInstanceState == null) {
            replaceFragment(MainScreenFragment.newInstance(), R.id.fragmentContainer)
        }
    }

    override fun onBackPressed() {
        var handled = false
        val visibleFragment = getVisibleFragment()
        if (visibleFragment != null && visibleFragment is BaseFragment<*>) {
            handled = visibleFragment.onBackPressed()
        }
        if (!handled) {
            super.onBackPressed()
        }
    }

    private fun getVisibleFragment(): Fragment? {
        return when (visibleFragments.size) {
            1 -> visibleFragments[0]
            else -> null
        }
    }
}
