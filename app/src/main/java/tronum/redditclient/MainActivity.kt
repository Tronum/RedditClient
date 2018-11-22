package tronum.redditclient

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
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
        showMainScreen()
    }

    private fun showMainScreen() {
        showFragment(MainScreenFragment.newInstance(), R.id.fragmentContainer)
    }

    private fun showFragment(fragment: BaseFragment<*>, containerViewId: Int) {
        clearFragmentStack()
        supportFragmentManager.beginTransaction()
            .replace(containerViewId, fragment, fragment.javaClass.simpleName)
            .commitAllowingStateLoss()
    }

    private fun showFragmentBackStacked(fragment: BaseFragment<*>, containerViewId: Int) {
        val tag = fragment.javaClass.simpleName
        supportFragmentManager.beginTransaction()
            .replace(containerViewId, fragment, tag)
            .addToBackStack(tag)
            .commitAllowingStateLoss()
    }

    private fun clearFragmentStack() {
        val fragmentManager = supportFragmentManager
        while (fragmentManager.backStackEntryCount > 0) {
            val first = fragmentManager.getBackStackEntryAt(0)
            fragmentManager.popBackStackImmediate(first.name, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }

    private fun popToRootFragment() {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    private fun popFragment(): Boolean {
        return supportFragmentManager.popBackStackImmediate()
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
