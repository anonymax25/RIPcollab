package esgi.project.ripcollab;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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


public class CollabHome extends AppCompatActivity {

    //private static final String apiURI = "http://ziongames.fr/API/api/users/list.php";
    private static final String apiURI = "http://192.168.43.220:80/-WEB-R.I.P-Project/API/api/";
    private User user;
    private TextView Id;
    private TextView Name;
    private TextView Hours;
    private Switch Online;
    private RatingBar Rating;
    private Button Refresh;
    private Button Quit;
    private Button TrajetsValide;
    private RequestQueue requestQueue;
    private int isOnline;
    private static ListView listView;
    private ArrayList<Trajet> trips = new ArrayList<Trajet>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collab_home);

        user = (User)getIntent().getSerializableExtra("SESSION_USER");
        requestQueue = Volley.newRequestQueue(this);

        getCollabInfo(requestQueue,user.getId());

        getTripsToValidate();

        initInfos();
        initRefrseh();
        initOnline();
        initDeco();
        trajetValideListenerButton();

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Toast.makeText(CollabHome.this, "Click on disconnect",Toast.LENGTH_LONG).show();

    }

    public void getTripsToValidate(){
            String stringURL = apiURI + "users/listTrips.php?idChauffeur=" + user.getId();

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
                                                jsonobject.getString("duration"),jsonobject.getString("state"),jsonobject.getInt("stateDriver")                                     ));

                                        //trajets2.add(jsonobject.getString("heureDebut") + " From " + jsonobject.getString("debut") + " to " + jsonobject.getString("fin"));
                                        //System.out.println("http" + jsonobject.toString(2));
                                        //Trajet trip = new TR
                                    }
                                }

                                //System.out.println(trajets2);

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
            CustomArrayAdapter adapter = new CustomArrayAdapter(trips, this, requestQueue);

            listView = (ListView) findViewById(R.id.lvTrajets);

            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(CollabHome.this, TrajetActivity.class);
                    intent.putExtra("TRAJET",trips.get(position));
                    startActivity(intent);
                }
            });
        } else {
            TextView noTrips = (TextView) findViewById(R.id.tv_noTrips);
            noTrips.setText("No trips for you yet.\nSorry :'(");
        }


        //final ArrayAdapter<String> adapter = new ArrayAdapter<String>(CollabHome.this, android.R.layout.simple_list_item_1, trajets2);
    }

    public void initDeco(){
        Quit = (Button) findViewById(R.id.btnDeco);
        Quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(CollabHome.this)
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
        });
    }


    public void trajetValideListenerButton(){
        TrajetsValide = (Button) findViewById(R.id.btnValidTrips);
        TrajetsValide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                String stringURL = apiURI + "users/listTrips.php?idChauffeur=" + user.getId();

                JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, stringURL,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {

                                    System.out.println(response.toString(2));

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
                */
                if (!CollabHome.this.isFinishing()){
                    Intent intent = new Intent(CollabHome.this, ValidActivity.class);
                    intent.putExtra("SESSION_USER", user);
                    startActivity(intent);
                }


            }
        });
    }

    public void initInfos(){
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

    public void initRefrseh(){
        Refresh = (Button) findViewById(R.id.btnRefresh);
        Refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                trips.clear();
                listView.setAdapter(null);

                getCollabInfo(requestQueue, user.getId());
                getTripsToValidate();
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
