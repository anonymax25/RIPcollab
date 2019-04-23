package esgi.project.ripcollab;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class NavHomeActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private ListView listView;
    private String[] trips = {"erer","uauauau","hehehhe","aaaa","bbbb"};

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    return true;
                case R.id.navigation_validated:
                    Intent intent = new Intent(NavHomeActivity.this,NavHomeActivity.class);

                    return true;
                case R.id.navigation_old:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_home);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.id.lvTrajets,trips);

        listView = (ListView) findViewById(R.id.lvTrajets);

        listView.setAdapter(adapter);
    }

}
