package com.Motawer.kalemah.MaterialDesign;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.DialogFragment;

import com.Motawer.kalemah.Adapter.RecyclerAdapter;
import com.Motawer.kalemah.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.muddzdev.styleabletoast.StyleableToast;

public class BottomSheet extends BottomSheetDialogFragment implements AdapterView.OnItemSelectedListener {
    BottomSheetListner sheetListner;
    View view;
    EditText word;
    EditText meaning;
    Spinner spinner;
    Button button;
    String Level,Word,Mean;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bottom_sheet, container, false);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        InitializeUI();
        spinner();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Word=word.getText().toString().toLowerCase().trim();
                Mean=meaning.getText().toString().toLowerCase().trim();
                if (!Word.isEmpty())
                {
                if (!Mean.isEmpty())
                {
                if (!Level.isEmpty())
                {
                    sheetListner.onButtomClicked(Word
                            ,Mean
                            ,Level);
                    dismiss();
                }
                else
                {
                    StyleableToast.makeText(requireContext(), "plz select level", Toast.LENGTH_LONG, R.style.mytoast).show();
                }
                }
                else
                {
                    StyleableToast.makeText(requireContext(), "plz insert meaning", Toast.LENGTH_LONG, R.style.mytoast).show();
                }
                }
                else
                {
                    StyleableToast.makeText(requireContext(), "plz insert word", Toast.LENGTH_LONG, R.style.mytoast).show();
                }
            }
        });
    }

    private void InitializeUI() {
        word = view.findViewById(R.id.word_edit);
        meaning = view.findViewById(R.id.meaning_edit);
        spinner = view.findViewById(R.id.spinner);
        button = view.findViewById(R.id.save_word);
    }
    private void spinner() {

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.level_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       String s = parent.getItemAtPosition(position).toString();
       if (s.equals("A"))
       {
           Level="1";
       }else if (s.equals("B"))
       {
           Level="2";
       }else if (s.equals("C"))
       {
           Level="3";
       }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public interface BottomSheetListner {
        void onButtomClicked(String Word, String meaning, String level);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            sheetListner = (BottomSheetListner) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + "must implement bottom sheet");
        }

    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        RecyclerAdapter recyclerAdapter=new RecyclerAdapter();
        recyclerAdapter.notifyDataSetChanged();
    }
}
