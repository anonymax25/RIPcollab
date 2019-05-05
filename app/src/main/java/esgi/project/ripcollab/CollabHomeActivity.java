package esgi.project.ripcollab;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



public class CollabHomeActivity extends AppCompatActivity{

    //private static final String apiURI = "http://ziongames.fr/API/api/users/list.php";
    private String apiURI;
    private User user;
    private TextView title;
    private TextView Id;
    private TextView Name;
    private TextView Hours;
    private Switch Online;
    private RatingBar Rating;
    private RequestQueue requestQueue;
    private int isOnline;
    private static ListView listView;
    private ArrayList<Trajet> trips = new ArrayList<Trajet>();

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.disconnect, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mybutton) {
            new AlertDialog.Builder(CollabHomeActivity.this)
                    .setTitle("Question ?")
                    .setMessage("Etes-vous sur de vouloir vous déconnecter ?")
                    .setPositiveButton("oui", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish(); //ferme l'activitée en cours
                        }
                    })
                    .setNegativeButton("non", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setCancelable(false)
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {


        trips.clear();
        if (trips.size() != 0){
            listView.setAdapter(null);
        }
        getCollabInfo(requestQueue, user.getId());
        getTripsToValidate();

        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collab_home);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        apiURI = (String)getIntent().getStringExtra("apiURI");

        user = (User)getIntent().getSerializableExtra("SESSION_USER");
        requestQueue = Volley.newRequestQueue(this);

        getCollabInfo(requestQueue,user.getId());

        System.out.println(user.getMetier());
        getTripsToValidate();

        initInfos();
        initOnline();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Toast.makeText(CollabHomeActivity.this, "Click on disconnect",Toast.LENGTH_LONG).show();

    }

    public void getTripsToValidate(){
            String stringURL = apiURI + "users/listTrips.php?idCollaborateur=" + user.getId();

            JsonArrayRequest objectRequest = new JsonArrayRequest(
                    Request.Method.GET,
                    stringURL,
                    null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {

                                JSONArray jsonarray = new JSONArray(response.toString());

                                for (int i = 0; i < jsonarray.length(); i++) {
                                    if (jsonarray.getJSONObject(i) != null){
                                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                                        System.out.println(jsonobject.toString(2));
                                        trips.add(new Trajet(jsonobject.getInt("idTrajet"),jsonobject.getInt("idClient"),jsonobject.getInt("idChauffeur"),
                                                jsonobject.getString("heureDebut"),jsonobject.getString("heureFin"), jsonobject.getString("dateResevation"),
                                                jsonobject.getInt("distanceTrajet"),jsonobject.getDouble("prixtrajet"),jsonobject.getString("debut"),jsonobject.getString("fin"),
                                                jsonobject.getString("duration"),jsonobject.getString("state"),jsonobject.getInt("stateDriver"),0));
                                    }
                                }
                                createListView();

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

    public void createListView(){

        System.out.println(trips);

        if (trips.size() != 0){
            TextView noTrips = (TextView) findViewById(R.id.tv_noTrips);
            noTrips.setHeight(0);
            noTrips.setText("");

            CustomArrayAdapter adapter = new CustomArrayAdapter(trips, this, requestQueue);

            listView = (ListView) findViewById(R.id.lvTrajets);

            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(CollabHomeActivity.this, TrajetActivity.class);
                    intent.putExtra("TRAJET",trips.get(position));
                    intent.putExtra("USER",user);
                    intent.putExtra("apiURI", apiURI);
                    startActivity(intent);
                }
            });
        } else {
            TextView noTrips = (TextView) findViewById(R.id.tv_noTrips);

            noTrips.setText("No trips for you yet.\nSorry :'(");
        }


        //final ArrayAdapter<String> adapter = new ArrayAdapter<String>(CollabHome.this, android.R.layout.simple_list_item_1, trajets2);
    }

    public void initInfos(){
        title = (TextView)findViewById(R.id.title);
        title.setText(title.getText().toString() + user.getMetier());

        Id = (TextView)findViewById(R.id.tvid);
        Id.setText("User ID: " + user.getId());

        Name = (TextView)findViewById(R.id.tvName);
        Name.setText("Collab: " + user.getFirst_name() + " " + user.getLast_name());
    }

    public void initOnline(){
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

                            user.setMetier(response.getString("metier"));
                            System.out.println(" METIERRRRR " + user.getMetier());

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
                        Log.e("ERRHTTP", "http err onResponse collab: " + error.toString());
                    }
                }
        );
        requestQueue.add(objectRequest);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    trips.clear();
                    if (trips.size() != 0){
                        listView.setAdapter(null);
                    }


                    getCollabInfo(requestQueue, user.getId());
                    getTripsToValidate();

                    Toast.makeText(CollabHomeActivity.this,"Information refreshed",Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.navigation_validated:
                    if (!CollabHomeActivity.this.isFinishing()){
                        Intent intent = new Intent(CollabHomeActivity.this, ValidActivity.class);
                        intent.putExtra("SESSION_USER", user);
                        intent.putExtra("apiURI", apiURI);
                        startActivity(intent);
                    }

                    return true;
                case R.id.navigation_old:
                    Intent intent = new Intent(CollabHomeActivity.this, OldTripsActivity.class);
                    intent.putExtra("USER", user);
                    intent.putExtra("apiURI", apiURI);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };
}
