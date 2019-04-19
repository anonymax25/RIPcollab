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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CustomArrayAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<Trajet> list;
    private Context context;
    private RequestQueue requestQueue;
    private static final String apiURI = "http://192.168.43.220:80/-WEB-R.I.P-Project/API/api/";



    public CustomArrayAdapter(ArrayList<Trajet> list, Context context, RequestQueue requestQueue) {
        this.list = list;
        this.context = context;
        this.requestQueue = requestQueue;
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
            view = inflater.inflate(R.layout.custom_list_view, null);
        }

        final Trajet current = (Trajet) getItem(position);

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
        listItemText.setText(current.getHeureDebut() + " from " + current.getDebut() + " to " + current.getFin());


        return view;
    }
}