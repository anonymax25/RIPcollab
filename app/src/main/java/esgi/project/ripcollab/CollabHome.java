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



public class CollabHome extends AppCompatActivity {

    private static final String apiURI = "http://192.168.43.220:80/mrbriatte/esgiPark/api/";
    private TextView Id;
    private TextView Name;
    private TextView Hours;
    private Switch Online;
    private RatingBar Rating;
    private Button Refresh;
    private Button TrajetsValide;
    private User user;
    private RequestQueue requestQueue;
    private int isOnline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collab_home);

        user = (User)getIntent().getSerializableExtra("SESSION_USER");


        requestQueue = Volley.newRequestQueue(this);
        getCollabInfo(requestQueue,user.getId());


        Id = (TextView)findViewById(R.id.tvid);
        Id.setText("User ID: " + user.getId());

        Name = (TextView)findViewById(R.id.tvName);
        Name.setText("Collab: " + user.getFirst_name() + " " + user.getLast_name());



        Refresh = (Button) findViewById(R.id.btnRefresh);
        Refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCollabInfo(requestQueue, user.getId());
            }
        });

        TrajetsValide = (Button) findViewById(R.id.btnValidTrips);
        TrajetsValide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!CollabHome.this.isFinishing()){
                    Intent intent = new Intent(CollabHome.this, ValidActivity.class);
                    intent.putExtra("SESSION_USER", user);
                    startActivity(intent);
                }


            }
        });



        Online = (Switch) findViewById(R.id.swOnline);
        Online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchOnline(user.getId());
                System.out.println("click online");
            }
        });

    }

    public void switchOnline(int userId){
        switch (this.isOnline){
            case 1:
                this.isOnline = 0;
                break;
            case 0:
                this.isOnline = 1;
                break;
        }
        String stringURL = apiURI + "users/putOnline.php?id=" + userId + "&isOnline=" + this.isOnline;

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.PUT,
                stringURL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERR", "http err isOnline onResponse: " + error.toString());
                    }
                }
        );
        requestQueue.add(objectRequest);

    }

    public void getCollabInfo(RequestQueue requestQueue, int id){

        String stringURL = apiURI + "users/get.php?id=" + id;

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
                                isOnline = 1;
                            } else {
                                Online.setChecked(false);
                                isOnline = 0;
                            }

                            //Rating bar
                            Rating = (RatingBar) findViewById(R.id.rbRating);
                            double rating = response.getDouble("rating");
                            if (rating == 5.0){
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

                            Hours = (TextView) findViewById(R.id.tvHours);
                            Hours.setText("heures collab:\n" + response.getInt("heuresTravailees") + "h");

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
