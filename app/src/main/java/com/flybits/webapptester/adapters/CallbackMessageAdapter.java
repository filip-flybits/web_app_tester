package com.flybits.webapptester.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flybits.webapptester.R;
import com.flybits.webapptester.models.JSMessage;

import java.util.ArrayList;

/**
 * Created by Fil on 8/17/2017.
 */

public class CallbackMessageAdapter extends RecyclerView.Adapter<CallbackMessageAdapter.ViewHolder>{

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<JSMessage> messages;

    public CallbackMessageAdapter(Context context, ArrayList<JSMessage> messageList)
    {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.messages = messageList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = inflater.inflate(R.layout.item_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        JSMessage item = messages.get(position);
        holder.txtTime.setText(item.getTime() + "");
        holder.txtJson.setText(item.getJson());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTime;
        TextView txtJson;
        View viewThisView;

        public ViewHolder(View v)
        {
            super(v);
            viewThisView = v;
            txtTime = v.findViewById(R.id.item_message_txtTime);
            txtJson = v.findViewById(R.id.item_message_txtJson);
        }
    }

}
