package com.Motawer.kalemah.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Motawer.kalemah.Adapter.RecyclerAdapter;
import com.Motawer.kalemah.MaterialDesign.BottomSheet;
import com.Motawer.kalemah.MaterialDesign.BottomSheetEdit;
import com.Motawer.kalemah.R;
import com.Motawer.kalemah.RoomDataBase.Word;
import com.Motawer.kalemah.ViewModel.WordsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class words_frag extends Fragment {
    private WordsViewModel viewModel;
    GetID getID;
    View view;
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    LinearLayoutManager linearLayoutManager;
    CoordinatorLayout coordinatorLayout;
    Snackbar snackbar;
    Toolbar toolbar;
    int undo = 0;
    private int id;
    SearchView searchView;
    TextView count;
    int size;
    ArrayList<Word> wordArrayList = new ArrayList<>();
    private FloatingActionButton FAB;
    //  MeowBottomNavigation btv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_words, container, false);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InitializUI();
        recyclerInit();
        //setViewModel();
        search();
        addWord();

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        setHasOptionsMenu(true);
    }

    private void swipeDeleteAndEdit() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0
                , ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();
                Word word = recyclerAdapter.getWordAt(pos);
                id = recyclerAdapter.getWordAt(pos).getID();
                ArrayList<Word> loadList = loadData();

                switch (direction) {
                    case ItemTouchHelper.LEFT:
                        // viewModel.deleteAllWords();
                        if (wordArrayList.size() == 0) {
                            recyclerAdapter.notifyDataSetChanged();
                            recyclerAdapter.notifyAll();
                        }
                        viewModel.delete(recyclerAdapter.getWordAt(pos));
                        //recyclerAdapter.removeAt(pos,requireContext());
                        recyclerAdapter.notifyItemRemoved(pos);
                        recyclerAdapter.removeshared(word, requireContext(), loadList);
                        snackbar(pos, word);
                        if (pos == 0) {
                            recyclerAdapter.clear();
                            count.setText("0");
                        }
                        break;
                    //StyleableToast.makeText(requireContext(), "Word deleted", Toast.LENGTH_LONG, R.style.mytoast).show();

                    case ItemTouchHelper.RIGHT:


                        getID.getidfrompos(id);
                        BottomSheetEdit bottomSheet = new BottomSheetEdit();
                        bottomSheet.show(getActivity().getSupportFragmentManager(), "BottomSheetdialog");

                }
              //  recyclerAdapter.notifyDataSetChanged();

            }

            private ArrayList<Word> loadData() {
                ArrayList<Word> wordArrayList;
                final String KEY = "FAVORIT_WORDS";
                final String WORD_FAVORIT = "MY_FAV_WORDS";
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(KEY, Context.MODE_PRIVATE);
                Gson gson = new Gson();
                String json = sharedPreferences.getString(WORD_FAVORIT, null);
                Type type = new TypeToken<ArrayList<Word>>() {
                }.getType();
                wordArrayList = gson.fromJson(json, type);

                //recyclerAdapter.setWordList(wordArrayList);

                if (wordArrayList == null) {
                    wordArrayList = new ArrayList<>();
                }
                return wordArrayList;
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
    }

    private void addWord() {

        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheet bottomSheet = new BottomSheet();
                bottomSheet.show(getActivity().getSupportFragmentManager(),
                        "BottomSheetdialog");

            }
        });
    }

    private void search() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                recyclerAdapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recyclerAdapter.getFilter().filter(newText);
                return true;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                setViewModel();
                return false;
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu, menu);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.all_levels:
                viewModel.getAllWords().observe(getViewLifecycleOwner(), new Observer<List<Word>>() {
                    @Override
                    public void onChanged(List<Word> words) {
                        //  recyclerAdapter.clear();
                        int size = words.size();
                        count.setText(String.valueOf(size));
                        recyclerAdapter.setWordList(words);
                        recyclerAdapter.notifyDataSetChanged();
                    }
                });
                return true;

            case R.id.all_User_levels:
                viewModel.getAllUserWords().observe(getViewLifecycleOwner(), new Observer<List<Word>>() {
                    @Override
                    public void onChanged(List<Word> words) {
                        // recyclerAdapter.clear();
                        int size = words.size();
                        count.setText(String.valueOf(size));
                        recyclerAdapter.setWordList(words);
                        recyclerAdapter.notifyDataSetChanged();

                    }
                });
                return true;

            case R.id.Level_C:
                viewModel.getlevelWords(-3).observe(getViewLifecycleOwner(), new Observer<List<Word>>() {
                    @Override
                    public void onChanged(List<Word> words) {
                        int size = words.size();
                        count.setText(String.valueOf(size));
                        recyclerAdapter.setWordList(words);
                    }
                });
                return true;
            case R.id.Level_B:
                viewModel.getlevelWords(-2).observe(getViewLifecycleOwner(), new Observer<List<Word>>() {
                    @Override
                    public void onChanged(List<Word> words) {
                        int size = words.size();
                        count.setText(String.valueOf(size));
                        recyclerAdapter.setWordList(words);
                    }
                });
                return true;
            case R.id.Level_A:
                viewModel.getlevelWords(-1).observe(getViewLifecycleOwner(), new Observer<List<Word>>() {
                    @Override
                    public void onChanged(List<Word> words) {
                        int size = words.size();
                        count.setText(String.valueOf(size));
                        recyclerAdapter.setWordList(words);
                    }
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void snackbar(final int pos, final Word word) {
        snackbar = Snackbar.make(coordinatorLayout, "item deleted",
                BaseTransientBottomBar.LENGTH_SHORT).addCallback(new Snackbar.Callback() {
            @Override
            public void onShown(Snackbar sb) {
                snackbar.setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // undo = 1;
                        Toast.makeText(requireContext(), "recoverd", Toast.LENGTH_SHORT).show();
                        recyclerAdapter.restoreItem(word, pos);
                        viewModel.insetr(word);
                        recyclerView.scrollToPosition(pos);


                    }
                });

            }

            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
//
            }
        });
        snackbar.show();

    }

    private void InitializUI() {
        FAB = view.findViewById(R.id.add_note);
        recyclerView = view.findViewById(R.id.recycler_words);
        // btv = getActivity().findViewById(R.id.botnav);
        coordinatorLayout = view.findViewById(R.id.coordinator);
        toolbar = view.findViewById(R.id.words_toolbar);
        searchView = view.findViewById(R.id.search_view);
        count = view.findViewById(R.id.count_text);

    }

    private void recyclerInit() {
        linearLayoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerAdapter = new RecyclerAdapter();
        setViewModel();
        recyclerView.setAdapter(recyclerAdapter);
        swipeDeleteAndEdit();
    }

    private void setViewModel() {
        viewModel = new ViewModelProvider((ViewModelStoreOwner) requireContext()).get(WordsViewModel.class);
        viewModel.getAllWords().observe(getViewLifecycleOwner(), new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                if (!words.isEmpty()) {
                    wordArrayList = new ArrayList<>(words);
                    recyclerAdapter.setWordList(words);
                    size = words.size();
                    count.setText(String.valueOf(size));
                } else {
                    viewModel.setFireBaseWords();
                }
            }

        });

    }

    public interface GetID {
        void getidfrompos(int id);
    }

    //ViewModel Necessary
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
