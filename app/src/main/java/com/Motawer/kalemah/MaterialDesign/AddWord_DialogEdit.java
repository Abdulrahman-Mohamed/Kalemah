package com.Motawer.kalemah.MaterialDesign;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.Motawer.kalemah.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.muddzdev.styleabletoast.StyleableToast;

public class AddWord_DialogEdit extends AppCompatDialogFragment
{
    AddWord_DialogEdit.AddWordSheetEditeListner sheetListner;
    AddWord_DialogEdit.refreshrecycler refresh;
    View view;
    EditText word;
    EditText meaning;
    NumberPicker numberPicker;
    private String[] pickerVals;
    Button button;
    String Level,Word,Mean;
    AlertDialog alertDialog;
    int rate;

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        refresh.onRecyclerRefresh();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {
        alertDialog =  new MaterialAlertDialogBuilder(getActivity(),R.style.MyRounded_MaterialComponents_MaterialAlertDialog)  // for fragment you can use getActivity() instead of this
                .setView(R.layout.word_dialog) // custom layout is here
                .show();


        Initiate();
        spinner();

        return  alertDialog;
    }

    private void spinner()
    {
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(2);
        pickerVals  = new String[] {"A", "B", "C"};
        numberPicker.setDisplayedValues(pickerVals);
        numberPicker.setValue(0);
        if (numberPicker.getValue()==0)
        {
            Level="-1";
            rate=5;
        }
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (pickerVals[picker.getValue()].equals("A"))
                {

                    Level="-1";
                    rate=5;
                }else if (pickerVals[picker.getValue()].equals("B"))

                {
                    Level="-2";
                    rate=3;
                }else if (pickerVals[picker.getValue()].equals("C"))

                {
                    Level="-3";
                    rate=1;
                }
            }
        });
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
//                R.array.level_array, android.R.layout.simple_spinner_item);
//        // Specify the layout to use when the list of choices appears
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        // Apply the adapter to the spinner
//        spinner.setAdapter(adapter);
//        spinner.setOnItemSelectedListener(this);
    }

    private void Initiate()
    {
        word = alertDialog.findViewById(R.id.word_edit);
        meaning = alertDialog.findViewById(R.id.meaning_edit);
        numberPicker=alertDialog.findViewById(R.id.number_picker);
        button = alertDialog.findViewById(R.id.save_word);

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
                            sheetListner.onAddWordEditClicked(Word
                                    ,Mean
                                    ,Level,rate);
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

//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        String s = parent.getItemAtPosition(position).toString();
//        if (s.equals("A"))
//        {
//            Level="-1";
//            rate=5;
//        }else if (s.equals("B"))
//        {
//            Level="-2";
//            rate=3;
//        }else if (s.equals("C"))
//        {
//            Level="-3";
//            rate=1;
//        }
//
//
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//
//    }

    public interface AddWordSheetEditeListner {
        void onAddWordEditClicked(String Word, String meaning, String level,int rate);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            sheetListner = (AddWordSheetEditeListner) context;
            refresh= (refreshrecycler) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + "must implement bottom sheet");
        }

    }

    public  interface refreshrecycler
    {
        void onRecyclerRefresh();

    }
}
