package com.example.androidlabs;

import android.app.AppComponentFactory;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class DetailsFragment extends Fragment {

   private Bundle dataFromActivity;
   private long id;
   private AppCompatActivity parentActivity;
   private Boolean isTablet = false;

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dataFromActivity = getArguments();
        id = dataFromActivity.getLong("id");

        View result = inflater.inflate(R.layout.fragment_details, container, false);

       TextView idView = (TextView) result.findViewById(R.id.idMsg);
       idView.setText("ID=" + id);

       TextView message = (TextView)result.findViewById(R.id.message);
       message.setText("Message: " + dataFromActivity.getString("item"));

       CheckBox checkBox = (CheckBox)result.findViewById(R.id.checkBtn);
       checkBox.setChecked(dataFromActivity.getBoolean("isSent"));

       Button hideButton = (Button) result.findViewById(R.id.hideBtn);
       hideButton.setOnClickListener( click -> {
           parentActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
       });

        return result;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        parentActivity = (AppCompatActivity)context;
    }

    public void setTablet(boolean tablet){
       isTablet = tablet;
    }
}