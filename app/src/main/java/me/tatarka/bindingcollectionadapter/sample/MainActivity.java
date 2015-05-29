package me.tatarka.bindingcollectionadapter.sample;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import me.tatarka.bindingcollectionadapter.sample.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements Listeners {
    private static final Stuff stuff = new Stuff();
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setStuff(stuff);
        binding.setListeners(this);
        binding.executePendingBindings();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View.OnClickListener onAddThing() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stuff.addThing();
                binding.list.smoothScrollToPosition(stuff.size() - 1);
            }
        };
    }

    @Override
    public View.OnClickListener onRemoveThing() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stuff.removeThing();
                if (stuff.size() > 0) {
                    binding.list.smoothScrollToPosition(stuff.size() - 1);
                }
            }
        };
    }
}
