package com.Motawer.kalemah.MaterialDesign;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.Motawer.kalemah.Adapter.RecyclerAdapter;
import com.Motawer.kalemah.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.muddzdev.styleabletoast.StyleableToast;

public class AddWord_Dialog extends AppCompatDialogFragment
{
    View view;
    NumberPicker numberPicker;
    EditText word;
    EditText meaning;
    Spinner spinner;
    Button button;
    String Level,Word,Mean;
    int rate;
    AlertDialog alertDialog;
    AddWord_Dialog.BottomSheetListner sheetListner;
    private String[] pickerVals;
    
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
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireActivity(),
//                R.array.level_array, R.layout.spinner_item);
//        // Specify the layout to use when the list of choices appears
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
//        // Apply the adapter to the spinner
//        spinner.setAdapter(adapter);
//        spinner.setGravity(Spinner.TEXT_ALIGNMENT_GRAVITY);
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String s = parent.getItemAtPosition(position).toString();
//                if (s.equals("A"))
//                {
//
//                    Level="-1";
//                    rate=5;
//                }else if (s.equals("B"))
//                {
//                    Level="-2";
//                    rate=3;
//                }else if (s.equals("C"))
//                {
//                    Level="-3";
//                    rate=1;
//                }
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
    }

    private void Initiate()
    {
        word = alertDialog.findViewById(R.id.word_edit);
        meaning = alertDialog.findViewById(R.id.meaning_edit);
        numberPicker=alertDialog.findViewById(R.id.number_picker);
       // spinner = alertDialog.findViewById(R.id.spinner);
        button = alertDialog.findViewById(R.id.save_word);
        button.setOnClickListener(new View.OnClickListener()
        {
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



    public interface BottomSheetListner
    {
        void onButtomClicked(String Word, String meaning, String level,int rate);
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
