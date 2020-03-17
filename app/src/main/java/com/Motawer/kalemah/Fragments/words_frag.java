package com.Motawer.kalemah.Fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Motawer.kalemah.Adapter.RecyclerAdapter;
import com.Motawer.kalemah.MainActivity;
import com.Motawer.kalemah.MaterialDesign.BottomSheet;
import com.Motawer.kalemah.MaterialDesign.BottomSheetEdit;
import com.Motawer.kalemah.R;
import com.Motawer.kalemah.RoomDataBase.Word;
import com.Motawer.kalemah.ViewModel.WordsViewModel;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.muddzdev.styleabletoast.StyleableToast;

import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class words_frag extends Fragment  {
    private WordsViewModel viewModel;
    GetID getID;
    View view;
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    LinearLayoutManager linearLayoutManager;
    CoordinatorLayout coordinatorLayout;
    Animation slide_down, slide_up;
    Snackbar snackbar;
    int undo = 0;
    private int id;



    private FloatingActionButton FAB;
    MeowBottomNavigation btv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_words, container, false);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InitializUI();
        btv.setVisibility(View.VISIBLE);
        //btv.startAnimation(start);
        recyclerInit();
        setViewModel();


        //btv.startAnimation(start);
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheet bottomSheet = new BottomSheet();
                bottomSheet.show(getActivity().getSupportFragmentManager(), "BottomSheetdialog");

            }
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0
                , ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                 int pos = viewHolder.getAdapterPosition();
                id=  recyclerAdapter.getWordAt(pos).getID();
                switch (direction) {
                    case ItemTouchHelper.LEFT:

                        snackbar(pos);
                        break;


                    //StyleableToast.makeText(requireContext(), "Word deleted", Toast.LENGTH_LONG, R.style.mytoast).show();

                    case ItemTouchHelper.RIGHT:


                         getID.getidfrompos(id);
                        BottomSheetEdit bottomSheet = new BottomSheetEdit();
                        bottomSheet.show(getActivity().getSupportFragmentManager(), "BottomSheetdialog");

                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(requireContext(), R.color.delete))
                        .addSwipeLeftActionIcon(R.drawable.ic_delete_black_24dp)
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark))
                        .addSwipeRightActionIcon(R.drawable.ic_edit_black_24dp)
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }).attachToRecyclerView(recyclerView);
        btv.clearAnimation();
    }


    private void snackbar(final int pos) {
        snackbar = Snackbar.make(coordinatorLayout, "item deleted",
                BaseTransientBottomBar.LENGTH_SHORT).addCallback(new Snackbar.Callback() {
            @Override
            public void onShown(Snackbar sb) {
                //recyclerAdapter.removeAt(pos);
                snackbar.setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        undo = 1;
                        Toast.makeText(requireContext(), "recoverd", Toast.LENGTH_SHORT).show();
                        setViewModel();
                        recyclerAdapter.notifyItemChanged(pos);

                    }
                });

                // recyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                if (undo == 0) {
                    viewModel.delete(recyclerAdapter.getWordAt(pos));

                    //  recyclerAdapter.notifyItemChanged(pos);
                } else if (undo == 1) {
                    undo = 0;

                }
            }
        });
        snackbar.show();

    }


    private void InitializUI() {
        FAB = view.findViewById(R.id.add_note);
        recyclerView = view.findViewById(R.id.recycler_words);
        btv = getActivity().findViewById(R.id.botnav);
        coordinatorLayout = view.findViewById(R.id.coordinator);

        slide_down = AnimationUtils.loadAnimation(getActivity(),
                R.anim.anim);
        slide_up = AnimationUtils.loadAnimation(getActivity(),
                R.anim.slide_up);
//        start = AnimationUtils.loadAnimation(getActivity(),
//                R.anim.slide_up);
//        slide_up.cancel();
//        slide_down.cancel();


    }

    private void recyclerInit() {
        linearLayoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerAdapter = new RecyclerAdapter();
        recyclerView.setAdapter(recyclerAdapter);
    }

    private void setViewModel() {
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

    @Override
    public void onResume() {
        super.onResume();
        btv.clearAnimation();
        slide_up.reset();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {

                    btv.setVisibility(View.INVISIBLE);
                    btv.startAnimation(slide_down);
                } else {

                    btv.setVisibility(View.VISIBLE);
                    //  btv.startAnimation(slide_up);

                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        btv.clearAnimation();
        slide_up.reset();

    }
    public interface GetID {
        void getidfrompos(int id);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            getID = (GetID) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + "must implement bottom sheet");
        }
    }
}
