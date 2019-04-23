package esgi.project.ripcollab;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ValidActivity extends AppCompatActivity {

    private User user;
    private TextView Name;
    private ListView listView;
    private ArrayList<Trajet> trips = new ArrayList<Trajet>();
    private String apiURI;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valid);

        apiURI = (String)getIntent().getStringExtra("apiURI");
        user = (User)getIntent().getSerializableExtra("SESSION_USER");
        requestQueue = Volley.newRequestQueue(this);

        getValidTrips();

        initInfos();


    }

    public void initInfos(){
        Name = (TextView)findViewById(R.id.tvName2);
        Name.setText("Collab: " + user.getFirst_name() + " " + user.getLast_name());
    }

    public void getValidTrips(){

        String stringURL = apiURI + "users/listTripsValidated.php?idCollaborateur=" + user.getId() +"&metier=" + user.getMetier();

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

        for (int i = 0; i < trips.size() ; i++) {
            System.out.println(trips.get(i).toString());
        }


        CustomArrayAdapterValid adapter = new CustomArrayAdapterValid(trips, this, requestQueue,apiURI);

        listView = (ListView) findViewById(R.id.lv_validated);

        listView.setAdapter(adapter);



        //final ArrayAdapter<String> adapter = new ArrayAdapter<String>(CollabHome.this, android.R.layout.simple_list_item_1, trajets2);
    }

}
