package me.tatarka.bindingcollectionadapter.sample;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import me.tatarka.bindingcollectionadapter.sample.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        
        binding.drawerLayout.setDrawerListener(new ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open_drawer, R.string.close_drawer));
        
        binding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Fragment fragment;
                switch (menuItem.getItemId()) {
                    case R.id.action_listview:
                        fragment = new FragmentListView();
                        break;
                    case R.id.action_recyclerview:
                        fragment = new FragmentRecyclerView();
                        break;
                    case R.id.action_viewpager:
                        fragment = new FragmentViewPagerView();
                        break;
                    default:
                        binding.drawerLayout.closeDrawers();
                        return false;
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content, fragment)
                        .commit();
                menuItem.setChecked(true);
                binding.drawerLayout.closeDrawers();
                return true;
            }
        });
    }
}
