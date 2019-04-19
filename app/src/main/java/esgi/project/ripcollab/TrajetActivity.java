package esgi.project.ripcollab;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

    private RequestQueue requestQueue;
    private Trajet trajet;
    private TextView tv;
    private static final String apiURI = "http://192.168.43.220:80/-WEB-R.I.P-Project/API/api/";
    private Button deleteBtn;
    private Button addBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trajet);

        requestQueue = Volley.newRequestQueue(this);

        trajet = (Trajet)getIntent().getSerializableExtra("TRAJET");

        tv = (TextView) findViewById(R.id.tv_trajet);
        tv.setText(trajet.toString());

        //Handle buttons and add onClickListeners
        deleteBtn = (Button)findViewById(R.id.delete_btn);
        addBtn = (Button)findViewById(R.id.add_btn);

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(TrajetActivity.this, "Trip refused",Toast.LENGTH_LONG).show();
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(TrajetActivity.this, "Trip validated",Toast.LENGTH_LONG).show();

                String stringURL = apiURI + "users/putTripValidated.php?idTrajet=" + trajet.getIdTrajet();

                JsonObjectRequest objectRequest = new JsonObjectRequest(
                        Request.Method.GET,
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
        });

    }
}
