package com.schedulearn.schedulearn;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConnectionsFragment extends Fragment {

    private static final String TAG = "ConnectionsFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_connections, container,false);
        RecyclerView recyclerView = v.findViewById(R.id.connections_recycler_view);
        ConnectionsAdapter adapter = new ConnectionsAdapter(getContext(), MainActivity.mConnectionsPfps, MainActivity.mConnectionsNames);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return v;
    }

    public class ConnectionsAdapter extends RecyclerView.Adapter<ConnectionsAdapter.ConnectionsViewHolder> {

        private ArrayList<String> mConnectionsPfps = new ArrayList<>();
        private ArrayList<String> mConnectionsNames = new ArrayList<>();
        private Context mContext;


        public class ConnectionsViewHolder extends RecyclerView.ViewHolder {

            private CircleImageView connectionsPfp;
            private TextView connectionsName;
            private RelativeLayout listItemLayout;

            public ConnectionsViewHolder(@NonNull View itemView) {
                super(itemView);

                connectionsPfp = itemView.findViewById(R.id.connection_pfp);
                connectionsName = itemView.findViewById(R.id.connection_name);
                listItemLayout = itemView.findViewById(R.id.list_item);
            }
        }

        public ConnectionsAdapter(Context context, ArrayList<String> connectionsPfps, ArrayList<String> connectionsNames) {
            mConnectionsPfps = connectionsPfps;
            mConnectionsNames = connectionsNames;
            mContext = context;
        }

        @NonNull
        @Override
        public ConnectionsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_connection, viewGroup, false);
            ConnectionsViewHolder holder = new ConnectionsViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull final ConnectionsViewHolder connectionsViewHolder, final int i) {
            Glide.with(mContext)
                    .asBitmap()
                    .load(mConnectionsPfps.get(i))
                    .into(connectionsViewHolder.connectionsPfp);
            connectionsViewHolder.connectionsName.setText(mConnectionsNames.get(i));

            connectionsViewHolder.listItemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), mConnectionsNames.get(i), Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            Toast.makeText(getContext(), mConnectionsNames.size() + "", Toast.LENGTH_LONG);
            return mConnectionsNames.size();
        }


    }
}
