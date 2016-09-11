package ir.amin.vocabularymaneger.activities;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import ir.amin.vocabularymaneger.R;
import ir.amin.vocabularymaneger.adapters.VocabViewPagerAdapter;
import ir.amin.vocabularymaneger.entities.Word;
import ir.amin.vocabularymaneger.fragments.WordFragment;
import ir.amin.vocabularymaneger.provider.DatabaseHelper;
import ir.amin.vocabularymaneger.provider.WordProvider;
import ir.amin.vocabularymaneger.webservise.OnDownloadComplete;
import ir.amin.vocabularymaneger.webservise.WebService;
import ir.android.util.Preferences;
import ir.android.widget.TfTextview;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnDownloadComplete {

    private ViewPager viewPager;
    private TfTextview wordNumberText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        wordNumberText = (TfTextview) findViewById(R.id.word_number);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(syncAdapter());
        viewPager.setCurrentItem(Preferences.getInstance(this).getLastSeenWord());

        findViewById(R.id.first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(0);
            }
        });

        findViewById(R.id.last).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(viewPager.getAdapter().getCount() - 1);
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                wordNumberText.setText((position + 1) + " / " + viewPager.getAdapter().getCount());
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    private VocabViewPagerAdapter syncAdapter() {
        final DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext(), WordProvider.tableName);
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        VocabViewPagerAdapter adapter = new VocabViewPagerAdapter(getSupportFragmentManager());
        Cursor c = db.rawQuery("select * from " + WordProvider.tableName, null);
        if (c.moveToFirst())
            do {
                WordFragment fragment = new WordFragment();
                Bundle bundle = new Bundle();
                bundle.putStringArray("word", Word.createFromCursor(c).getArray());
                fragment.setArguments(bundle);
                adapter.addFragment(fragment, "");
            } while (c.moveToNext());
        return adapter;
    }


    @Override
    protected void onResume() {
        super.onResume();
        viewPager.setCurrentItem(Preferences.getInstance(this).getLastSeenWord());
    }

    @Override
    protected void onPause() {
        Preferences pref = Preferences.getInstance(this);
        pref.setLastSeenWord(viewPager.getCurrentItem());
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Preferences pref = Preferences.getInstance(this);
        pref.setLastSeenWord(viewPager.getCurrentItem());
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sync) {
            Log.d("sync", "action_sync");
            WebService webService = WebService.newInstance(this);
            webService.sync();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onDownloadComplete(Object[] objects) {

        Word[] words = (Word[]) objects;
        for (int i = 0; i < words.length; i++) {
            try {
                getContentResolver().insert(
                        WordProvider.CONTENT_URI, words[i].getContentValues());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                viewPager.setAdapter(syncAdapter());
            }
        });
    }
}
