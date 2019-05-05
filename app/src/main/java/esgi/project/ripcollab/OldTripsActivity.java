package esgi.project.ripcollab;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class OldTripsActivity extends AppCompatActivity {

    private String apiURI;
    private User user;
    private RequestQueue requestQueue;
    private ListView list;
    private ArrayList<Trajet> trips = new ArrayList<Trajet>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_trips);

        apiURI = (String)getIntent().getStringExtra("apiURI");
        requestQueue = Volley.newRequestQueue(this);
        user = (User)getIntent().getSerializableExtra("USER");

        System.out.println(user.getId() + "IDDDDDDDD");

        getTripsOld();


    }

    public void getTripsOld(){
        String stringURL = apiURI + "users/listTripsOLd.php?idCollaborateur=" + user.getId();

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

            list = (ListView) findViewById(R.id.lv_old);

            list.setAdapter(adapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(OldTripsActivity.this, "Trip finished",Toast.LENGTH_SHORT);
                }
            });
        } else {
            TextView noTrips = (TextView) findViewById(R.id.tv_noTrips);

            noTrips.setText("No old trips yet");
        }


        //final ArrayAdapter<String> adapter = new ArrayAdapter<String>(CollabHome.this, android.R.layout.simple_list_item_1, trajets2);
    }
}
