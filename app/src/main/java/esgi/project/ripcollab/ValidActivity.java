package esgi.project.ripcollab;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ValidActivity extends AppCompatActivity {

    private Button Back;
    private User user;
    private TextView Name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valid);

        user = (User)getIntent().getSerializableExtra("SESSION_USER");

        Name = (TextView)findViewById(R.id.tvName2);
        Name.setText("Collab: " + user.getFirst_name() + " " + user.getLast_name());

        Back = (Button) findViewById(R.id.btnBack);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
