package com.Motawer.kalemah.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Motawer.kalemah.Adapter.RecyclerAdapter;
import com.Motawer.kalemah.R;
import com.Motawer.kalemah.RoomDataBase.Word;
import com.Motawer.kalemah.ViewModel.WordsViewModel;

import java.util.List;

public class words_frag extends Fragment {
    private WordsViewModel viewModel;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_words, container, false);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_words);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false));
        recyclerView.setHasFixedSize(true);
         final RecyclerAdapter recyclerAdapter=new RecyclerAdapter();
         recyclerView.setAdapter(recyclerAdapter);
        viewModel = new ViewModelProvider((ViewModelStoreOwner) requireContext()).get(WordsViewModel.class);
        viewModel.getAllWords().observe(getViewLifecycleOwner(), new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                // recycler view on change
                recyclerAdapter.setWordList(words);
               // Toast.makeText(requireContext(), "hi", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
