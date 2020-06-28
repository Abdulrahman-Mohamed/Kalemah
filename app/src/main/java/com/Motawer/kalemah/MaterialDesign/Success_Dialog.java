package com.Motawer.kalemah.MaterialDesign;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.Motawer.kalemah.Excercise_Levels;
import com.Motawer.kalemah.R;

public class Success_Dialog extends AppCompatDialogFragment
{
    TextView pointsTextView;
    Button button;
    View view;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
         int p=getArguments().getInt("points");

        LayoutInflater layoutInflater=getActivity().getLayoutInflater();
         view=layoutInflater.inflate(R.layout.success_dialog,null);

       Initiate();
        if (!String.valueOf(p).equals(null))
        pointsTextView.setText(String.valueOf(p));

        AlertDialog.Builder alert=new AlertDialog.Builder(getActivity());
        AlertDialog alertDialog=alert.create();

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.setView(view);
        alertDialog.setContentView(view);
        alertDialog.show();
    return alertDialog;
    }

    private void Initiate()
    {
        pointsTextView=view.findViewById(R.id.points);
        button=view.findViewById(R.id.succeed_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), Excercise_Levels.class);
                startActivity(intent);
            }
        });
    }
}
