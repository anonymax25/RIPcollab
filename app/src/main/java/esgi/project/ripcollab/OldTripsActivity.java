package esgi.project.ripcollab;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class OldTripsActivity extends AppCompatActivity {

    private String apiURI;
    private User user;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_trips);

        apiURI = (String)getIntent().getStringExtra("apiURI");
        requestQueue = Volley.newRequestQueue(this);
        user = (User)getIntent().getSerializableExtra("USER");

    }
}
