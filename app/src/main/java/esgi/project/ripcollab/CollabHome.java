package esgi.project.ripcollab;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;



public class CollabHome extends AppCompatActivity {

    private TextView Id;
    private TextView Name;
    private Switch Online;
    private RatingBar Rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collab_home);

        User user = new User(getIntent().getIntExtra("SESSION_ID",-1),getIntent().getStringExtra("SESSION_EMAIL"),getIntent().getStringExtra("SESSION_PASSWORD"),
                getIntent().getStringExtra("SESSION_LAST_NAME"),getIntent().getStringExtra("SESSION_FIRST_NAME"),getIntent().getStringExtra("SESSION_BIRTHDAY"),
                getIntent().getStringExtra("SESSION_GENDER"),getIntent().getStringExtra("SESSION_AVATAR"),getIntent().getStringExtra("SESSION_ZIP_CODE"),getIntent().getStringExtra("SESSION_ADDRESS"),
                getIntent().getIntExtra("SESSION_ISBANNED",-1),getIntent().getIntExtra("SESSION_ISADMIN",-1),getIntent().getIntExtra("SESSION_ISCOLLABORATEUR",-1));


        RequestQueue requestQueue = Volley.newRequestQueue(this);

        isOnline(requestQueue,user.getId());




        Id = (TextView)findViewById(R.id.tvid);
        Id.setText("User ID: " + user.getId());

        Name = (TextView)findViewById(R.id.tvName);
        Name.setText("Collab: " + user.getFirst_name() + " " + user.getLast_name());
        Name.setTextSize(12,2);

        Online = (Switch) findViewById(R.id.swOnline);


        Online.setOnClickListener(new View.OnClickListener() {



            
            @Override
            public void onClick(View v) {
                System.out.println("click online");
            }
        });

    }

    public void isOnline(RequestQueue requestQueue, int id){

        String stringURL = "http://192.168.43.220:80/mrbriatte/esgiPark/api/users/get.php?id=" + id;

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, stringURL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            System.out.println(response.toString(2));

                            //Online Switch
                            Online = (Switch) findViewById(R.id.swOnline);
                            if (response.getInt("isOnline") == 1){
                                Online.setChecked(true);
                            } else {
                                Online.setChecked(false);
                            }

                            //Rating bar
                            Rating = (RatingBar) findViewById(R.id.rbRating);
                            double rating = response.getDouble("rating");
                            if (rating == 5){
                                Rating.setRating(5);
                            } else if (rating < 5 && rating >= 4){
                                Rating.setRating(4);
                            } else if (rating < 4 && rating >= 3){
                                Rating.setRating(3);
                            } else if (rating < 3 && rating >= 2){
                                Rating.setRating(2);
                            } else if (rating < 2 && rating >= 1){
                                Rating.setRating(1);
                            } else if (rating < 1 && rating >= 0){
                                Rating.setRating(0);
                            }

                        }catch (JSONException e){
                            System.out.println(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERRHTTP", "http err onResponse: " + error.toString());
                    }
                }
        );
        requestQueue.add(objectRequest);
    }
}
