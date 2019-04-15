package esgi.project.ripcollab;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ValidActivity extends AppCompatActivity {

    private Button Back;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collab_home);

        user = (User)getIntent().getSerializableExtra("SESSION_USER");

        /*
        Back = (Button) findViewById(R.id.btnBack);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ValidActivity.this, CollabHome.class);
                intent.putExtra("SESSION_USER", user);
                startActivity(intent);
            }
        });
        */


    }
}
