package com.example.codechefeventsapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.codechefeventsapp.R;
import com.example.codechefeventsapp.adapters.PastEventAdapter;
import com.example.codechefeventsapp.adapters.UpcomingEventAdapter;
import com.example.codechefeventsapp.data.Utils;
import com.example.codechefeventsapp.data.models.Event;
import com.example.codechefeventsapp.view_models.EventViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.example.codechefeventsapp.activities.MainActivity.TAG;

public class HomeFragment extends Fragment {

    EventViewModel eventViewModel;
    UpcomingEventAdapter upcomingEventAdapter;
    PastEventAdapter pastEventAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUpcomingEvents();
        initPastEvents();
        initViewModel();
    }

    private void initViewModel() {
        eventViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()))
                .get(EventViewModel.class);

        eventViewModel.getEventsFromFirebaseAndStore();
        eventViewModel.getAllContests().observe(getViewLifecycleOwner(), new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> eventList) {
                Log.d(TAG, "onChanged: ");
                List<Event> upcomingEventList = new ArrayList<>();
                List<Event> pastEventList = new ArrayList<>();
                for (Event event : eventList) {
                    if (Utils.isPastEvent(event)) upcomingEventList.add(event);
                    else pastEventList.add(event);
                }
                upcomingEventAdapter.setEventList(upcomingEventList);
                pastEventAdapter.setEventList(pastEventList);
            }
        });
    }

    /**
     * Initialises upcomingEventAdapter with empty list and attach it to ViewPager
     */
    private void initUpcomingEvents() {
        upcomingEventAdapter = new UpcomingEventAdapter(new ArrayList<>(), getContext());
        ViewPager viewPager = getView().findViewById(R.id.viewPager);
        viewPager.setAdapter(upcomingEventAdapter);
    }

    /**
     * Initialises pastEventAdapter with empty list and attach it to RecyclerView
     */
    private void initPastEvents() {
        pastEventAdapter = new PastEventAdapter(new ArrayList<>());
        RecyclerView pastRecyclerView = getView().findViewById(R.id.pastRecyclerView);
        pastRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        pastRecyclerView.setAdapter(pastEventAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.notifications:
                Toast.makeText(getContext(), "Notifications", Toast.LENGTH_SHORT).show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}