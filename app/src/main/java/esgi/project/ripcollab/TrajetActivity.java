package esgi.project.ripcollab;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.Request;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class TrajetActivity extends AppCompatActivity {

    private User user;
    private RequestQueue requestQueue;
    private Trajet trajet;
    private TextView tv;
    private String apiURI;
    private Button deleteBtn;
    private Button addBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trajet);

        apiURI = (String)getIntent().getStringExtra("apiURI");
        requestQueue = Volley.newRequestQueue(this);

        user = (User)getIntent().getSerializableExtra("USER");

        trajet = (Trajet)getIntent().getSerializableExtra("TRAJET");

        tv = (TextView) findViewById(R.id.tv_trajet);
        tv.setText(trajet.toString());

        //Handle buttons and add onClickListeners
        deleteBtn = (Button)findViewById(R.id.delete_btn);
        addBtn = (Button)findViewById(R.id.add_btn);

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(TrajetActivity.this)
                        .setTitle("Question ?")
                        .setMessage("Etes-vous sur de vouloir refuser le trajet ?")
                        .setPositiveButton("oui", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(TrajetActivity.this, "Trip refused",Toast.LENGTH_LONG).show();
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
        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(TrajetActivity.this)
                        .setTitle("Question ?")
                        .setMessage("Etes-vous sur de vouloir accepter le trajet ?")
                        .setPositiveButton("oui", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(TrajetActivity.this, "Trip validated",Toast.LENGTH_LONG).show();

                                String stringURL = apiURI + "users/putTripValidated.php?idTrajet=" + trajet.getIdTrajet() + "&metier=" + user.getMetier() + "&idCollab=" + user.getId();

                                JsonObjectRequest objectRequest = new JsonObjectRequest(
                                        Request.Method.PUT,
                                        stringURL,
                                        null,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try {
                                                    System.out.println(response.toString(2));
                                                    finish();
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
}
