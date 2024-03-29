package com.Motawer.kalemah.Fragments;

import android.animation.Animator;
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
import android.widget.LinearLayout;
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
import com.Motawer.kalemah.MaterialDesign.AddWord_DialogEdit;
import com.Motawer.kalemah.R;
import com.Motawer.kalemah.RoomDataBase.Word;
import com.Motawer.kalemah.ViewModel.WordsViewModel;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
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
    boolean state=false;

    //  int undo = 0;
    private int id;
    int y = 1;
    SearchView searchView;
    TextView count;
    LinearLayout linearLayout;
    FloatingActionButton floatingActionButton;
    int size;
    //  int firstVisibleInListview;

    ArrayList<Word> wordArrayList = new ArrayList<>();
    // private FloatingActionButton FAB;
    //  MeowBottomNavigation btv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_words, container, false);
        InitializUI();

        return view;

    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerInit();
        onScroll();
        search();

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        setHasOptionsMenu(true);
    }



/*

this method use on swipe to make options to the user to have easy swip tp delete or swipe to edit

it has two dirictions

first one is left
and that is for delete when the user swipe to left the background turn into red
and then the delete icon appear
it delete the word from the view model then notify the recycler adapter that the word in this postition
was deleted and finally it delete the word from the shared prefrences to delete it from the favourite list

second one is right
and this one to edit same as previous it turn the back ground volor into blue and then
show the edit icon when it swipe it trigger the dialog of editting

*/
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
                        AddWord_DialogEdit addWord_dialogEdit = new AddWord_DialogEdit();
                        addWord_dialogEdit.show(requireActivity().getSupportFragmentManager(), "WordDialog");
                        recyclerAdapter.notifyItemChanged(pos);

                }
                //  recyclerAdapter.notifyDataSetChanged();

            }


            private ArrayList<Word> loadData() {
                ArrayList<Word> wordArrayList;
                final String KEY = "FAVORIT_WORDS";
                final String WORD_FAVORIT = "MY_FAV_WORDS";
                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(KEY, Context.MODE_PRIVATE);
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


/*
this methode trigger the search filters on the recycler adapter so the text written on the search view
will be shown in the recycler
 */

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
/*
attach menu item to the activity
 */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu, menu);


    }
/*
use the menu item elements to filter words as
all words
favourite
c
a
b
 */
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
                        state=true;
                    }
                });
//                if (state)
                return true;

            case R.id.Favourite:
                loadData();
                if (state)
                return true;

            case R.id.Level_C:
                viewModel.getlevelWords(-3).observe( getViewLifecycleOwner(),new Observer<List<Word>>() {
                    @Override
                    public void onChanged(List<Word> words) {
                        int size = words.size();
                        count.setText(String.valueOf(size));
                        recyclerAdapter.setWordList(words);
                        recyclerAdapter.notifyDataSetChanged();
                        recyclerView.setAdapter(recyclerAdapter);
                        state=true;

                    }
                });
              //  if (state)
                return true;
            case R.id.Level_B:
                viewModel.getlevelWords(-2).observe( getViewLifecycleOwner(),new Observer<List<Word>>() {
                    @Override
                    public void onChanged(List<Word> words) {
                        int size = words.size();
                        count.setText(String.valueOf(size));
                        recyclerAdapter.setWordList(words);
                        recyclerAdapter.notifyDataSetChanged();
                        state=true;
                    }
                });
              //  if (state)
                return true;
            case R.id.Level_A:
                viewModel.getlevelWords(-1).observe( getViewLifecycleOwner(),new Observer<List<Word>>() {
                    @Override
                    public void onChanged(List<Word> words) {
                        int size = words.size();
                        count.setText(String.valueOf(size));
                        recyclerAdapter.setWordList(words);
                        recyclerAdapter.notifyDataSetChanged();
                        state=true;

                    }
                });
             //   if (state)
                return true;
            default:
               return false;
        }

    }
    /*
    load data from shared prefrences to set it on the favourite list
     */

    private void loadData() {
        final String KEY = "FAVORIT_WORDS";
        final String WORD_FAVORIT = "MY_FAV_WORDS";
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(KEY, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(WORD_FAVORIT, null);
        Type type = new TypeToken<ArrayList<Word>>() {
        }.getType();
        wordArrayList = gson.fromJson(json, type);
        if (wordArrayList != null)
            recyclerAdapter.setWordList(wordArrayList);
        recyclerAdapter.notifyDataSetChanged();
if (wordArrayList != null)
    count.setText(String.valueOf(wordArrayList.size()));
        state=true;


        if (wordArrayList == null) {
            wordArrayList = new ArrayList<>();
        }
    }
/*
show the snackbar of unndoing

// to do
 */
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
        // FAB = view.findViewById(R.id.add_note);
        recyclerView = view.findViewById(R.id.recycler_words);
        // btv = getActivity().findViewById(R.id.botnav);
        coordinatorLayout = getActivity().findViewById(R.id.coordinator);
        toolbar = view.findViewById(R.id.words_toolbar);
        searchView = view.findViewById(R.id.search_view);
        count = view.findViewById(R.id.count_text);

    }

    private void recyclerInit() {
        linearLayoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        //recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setItemViewCacheSize(wordArrayList.size());
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerAdapter = new RecyclerAdapter();
        setViewModel();
        recyclerView.setAdapter(recyclerAdapter);
        swipeDeleteAndEdit();

    }
/*
on scroll
to make the bottom nav menu interact with the recycler scroll to enhance the user ecperince
 */
    private void onScroll() {

        linearLayout = getActivity().findViewById(R.id.bottom_Nav_Bar);
        floatingActionButton = getActivity().findViewById(R.id.words_fab);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean state = false;

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                System.out.println(newState + " state" );
//
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                linearLayout.setVisibility(View.VISIBLE);
                floatingActionButton.setVisibility(View.VISIBLE);
                //System.out.println(dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItemCount = layoutManager.getItemCount();
                int lastVisible = layoutManager.findLastVisibleItemPosition();
                boolean endHasBeenReached = lastVisible + 1 >= totalItemCount;
                System.out.println(endHasBeenReached);
                if (totalItemCount > 10 && endHasBeenReached && !state) {
                    YoYo.with(Techniques.SlideOutDown)
                            .duration(400)
                            .onEnd(new YoYo.AnimatorCallback() {
                                @Override
                                public void call(Animator animator) {
                                    linearLayout.setVisibility(View.GONE);

                                }
                            })
                            .playOn(linearLayout);

                    YoYo.with(Techniques.SlideOutDown)
                            .duration(400)
                            .onEnd(new YoYo.AnimatorCallback() {
                                @Override
                                public void call(Animator animator) {
                                    floatingActionButton.setVisibility(View.GONE);

                                }
                            })
                            .playOn(floatingActionButton);
                    state = true;
                }
                if (!endHasBeenReached && state) {
                    YoYo.with(Techniques.SlideInUp)
                            .duration(400)
                            .playOn(linearLayout);
                    linearLayout.setVisibility(View.VISIBLE);

                    YoYo.with(Techniques.SlideInUp)
                            .duration(400)
                            .playOn(floatingActionButton);
                    floatingActionButton.setVisibility(View.VISIBLE);
                    y = 1;
                    state=false;
                }
            }


        });
    }

/*
the view model this fetch all the user words
 */
    private void setViewModel() {
        viewModel = new ViewModelProvider((ViewModelStoreOwner) requireContext()).get(WordsViewModel.class);
        viewModel.getAllWords().observe(getViewLifecycleOwner(), new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                if (!words.isEmpty()) {
                    wordArrayList = new ArrayList<>(words);
                    recyclerAdapter.setWordList(words);
                    recyclerAdapter.notifyDataSetChanged();
                    size = words.size();
                    count.setText(String.valueOf(size));
                }

                if (words.isEmpty()) {
                    viewModel.setFireBaseWords();
                }
            }

        });

    }
/*
get id of the word to use it later in editing
 */
    public interface GetID {
        void getidfrompos(int id);
    }
/*
necessary to attach other views and interface into this fragment
 */
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
