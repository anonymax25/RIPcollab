package esgi.project.ripcollab;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CustomArrayAdapterValid extends BaseAdapter implements ListAdapter {
    private ArrayList<Trajet> list;
    private Context context;
    private RequestQueue requestQueue;
    private String apiURI;
    private User user;


    public CustomArrayAdapterValid(ArrayList<Trajet> list, Context context, RequestQueue requestQueue, String apiURI, User user) {
        this.list = list;
        this.context = context;
        this.requestQueue = requestQueue;
        this.apiURI = apiURI;
        this.user = user;
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int pos) {
        return this.list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(this.context);
            view = inflater.inflate(R.layout.custom_list_view_valid, null);
        }

        final Trajet current = (Trajet) getItem(position);

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
        listItemText.setText(current.getHeureDebut() + " from " + current.getDebut() + " to " + current.getFin());

        //Handle buttons and add onClickListeners
        Button finish = (Button)view.findViewById(R.id.btn_finish);
        System.out.println(user.getMetier());
        if (user.getMetier().equals("chauffeur")){
            if (current.getAboveStartTime() == 0){
                finish.setText("Le trajet n'as meme pas encore commencé\nvous ne pouvez pas le terminer");
                finish.setEnabled(false);
            }
        }else {
            finish.setText("Seul le chauffeur peut terminer le trajet");
            finish.setEnabled(false);
        }



        finish.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                //Toast.makeText(context, "Trip finished " + current.getIdTrajet() ,Toast.LENGTH_LONG).show();
                list.remove(position);



                String stringURL = apiURI + "users/putTripFinished.php?idTrajet=" + current.getIdTrajet();

                JsonObjectRequest objectRequest = new JsonObjectRequest(
                        Request.Method.GET,
                        stringURL,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    Toast.makeText(context, "Trip done",Toast.LENGTH_LONG).show();
                                    System.out.println(response.toString(2));
                                }catch (JSONException e){
                                    System.out.println(e);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("ERRHTTP", "http err onResponse finished: " + error.toString());
                            }
                        }
                );


                requestQueue.add(objectRequest);

                notifyDataSetChanged();
            }
        });

        return view;
    }
}