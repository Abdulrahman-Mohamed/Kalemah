package com.Motawer.kalemah.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Motawer.kalemah.Adapter.Exams_Adapter;
import com.Motawer.kalemah.Excercise_Levels;
import com.Motawer.kalemah.Models.ExamsItems;
import com.Motawer.kalemah.My_Words_Quizz;
import com.Motawer.kalemah.R;

import java.util.ArrayList;
import java.util.List;

public class exams_frag extends Fragment {
    Toolbar toolbar;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    Exams_Adapter exams_adapter;
    private View view;
    ImageView imageView , imageView1 ;
    List<ExamsItems> itemsList=new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return view = inflater.inflate(R.layout.frag_exams, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        InitiatView();

    }

    private void InitiatView() {

        recyclerView = view.findViewById(R.id.recycler_exam_items);
        linearLayoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        ExamsItems examsItems = new ExamsItems(R.drawable.ic_exam1, R.drawable.ic_feather, "Exercise");
        ExamsItems examsItems2 = new ExamsItems(R.drawable.ic_item_model, R.drawable.ic_feather, "Words");

        itemsList.add(examsItems);
        itemsList.add(examsItems2);

        exams_adapter = new Exams_Adapter(itemsList);
        recyclerView.setAdapter(exams_adapter);
        exams_adapter.setOnItemClickListener(new Exams_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if ( itemsList.get(position).getTextView().toString().equals("Exercise"))
                {
                    Intent intent = new Intent(getActivity(), Excercise_Levels.class);
                    startActivity(intent);
                   // Toast.makeText(requireContext(),itemsList.get(position).getTextView().toString(),Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(getActivity(), exams_frag.class);
//                    startActivity(intent);
                }
                if ( itemsList.get(position).getTextView().toString().equals("Words"))
                {
                    Intent intent = new Intent(getActivity(), My_Words_Quizz.class);
                    startActivity(intent);
                    // Toast.makeText(requireContext(),itemsList.get(position).getTextView().toString(),Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(getActivity(), exams_frag.class);
//                    startActivity(intent);
                }

            }
        });

    }
}
