package com.example.staff;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

import static com.example.staff.Common.simpleDateFormat;

public class StaffHomeActivity extends AppCompatActivity implements ITimeSlotLoadAdapter, ITimeSlotLoadListner, INotificationCountListner {

    DrawerLayout drawerLayout;
    TextView txt_barber_name;
    NavigationView navigationView;

    //@BindView(R.id.recycler_time_slot)
    RecyclerView recycler_time_slot;


    HorizontalCalendarView calendarView;
    CollectionReference notificationCollection;
    ActionBarDrawerToggle actionBarDrawerToggle;
    ITimeSlotLoadAdapter iTimeSlotLoadAdapter;
    ITimeSlotLoadListner iTimeSlotLoadListner;
    CollectionReference currentBookingDateCollection;
    DocumentReference barberDoc;
    android.app.AlertDialog alertDialog;

    EventListener<QuerySnapshot> notificationEvent;
    EventListener<QuerySnapshot> bookingEvent;
    ListenerRegistration notificationListner;
    ListenerRegistration bookingRealtimeListner;
    INotificationCountListner iNotificationCountListner;
    TextView txt_notification_badge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_home);
        drawerLayout=findViewById(R.id.activity_main);
        navigationView=findViewById(R.id.navigation_view);
        recycler_time_slot=findViewById(R.id.recycler_time_slot);
        calendarView=findViewById(R.id.calendarView);
        ButterKnife.bind(this);
        init();

        initView();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;


        return super.onOptionsItemSelected(item);
    }

    private void initView() {
       actionBarDrawerToggle =new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId()==R.id.menu_exit)
                    logout();
                if(menuItem.getItemId()==R.id.profile)
                    startActivity(new Intent(StaffHomeActivity.this,profile.class));

                return true;
            }
        });

        View headerView =navigationView.getHeaderView(0);
        txt_barber_name=(TextView)headerView.findViewById(R.id.txt_barber_name);
        txt_barber_name.setText(Common.currentUser);

        Calendar date=Calendar.getInstance();
        date.add(Calendar.DATE,0);
        loadAvailableTimeSlotofBarber(Common.userid, Common.simpleDateFormat.format(date.getTime()));
        recycler_time_slot.setHasFixedSize(true);
        GridLayoutManager layoutManager=new GridLayoutManager(this,3);
        recycler_time_slot.setLayoutManager(layoutManager);
        recycler_time_slot.addItemDecoration(new SpacesItemDecoration(8));

        Calendar startDate= Calendar.getInstance();
        startDate.add(Calendar.DATE,0);
        Calendar endDate=Calendar.getInstance();
        endDate.add(Calendar.DATE,2);

        HorizontalCalendar horizontalCalendar=new HorizontalCalendar.Builder(this,R.id.calendarView).range(startDate,endDate).datesNumberOnScreen(1).mode(HorizontalCalendar.Mode.DAYS).defaultSelectedDate(startDate).configure().end().build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                if(Common.bookingDate.getTimeInMillis()!=date.getTimeInMillis())
                {
                    Common.bookingDate=date;
                    loadAvailableTimeSlotofBarber(Common.userid,simpleDateFormat.format(date.getTime()));
                }

            }
        });


    }

    @Override
    public void onBackPressed() {

    }

    private void logout() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?").setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),login.class));
                finish();
            }
        }).setNegativeButton("Cancel",null);

        AlertDialog alert=builder.create();
        alert.show();

    }



    private void loadAvailableTimeSlotofBarber(final String barberId, final String bookDate) {

            barberDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {
                    DocumentSnapshot documentSnapshot=task.getResult();
                    if (documentSnapshot.exists())
                    {
                        CollectionReference date=FirebaseFirestore.getInstance().collection("All Saloon").document(Common.state_name).collection("Branch").document(Common.selectedSalon.getSalonId()).collection("Barber").document(barberId).collection(bookDate);
                        date.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful())
                                {
                                    QuerySnapshot querySnapshot=task.getResult();
                                    if (querySnapshot.isEmpty())
                                    {
                                        iTimeSlotLoadListner.onTimeSlotLoadEmpty();
                                    }
                                    else
                                    {
                                        List<TimeSlot> timeSlots=new ArrayList<>();
                                        for (QueryDocumentSnapshot document:task.getResult())
                                            timeSlots.add(document.toObject(TimeSlot.class));
                                        iTimeSlotLoadListner.onTimeSlotLoadSuccess(timeSlots);
                                    }


                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                iTimeSlotLoadListner.onTimeSlotLoadFailed(e.getMessage());
                            }
                        });

                    }

                }
            }
        });


    }

    private void init() {

        iTimeSlotLoadListner=this;
       iNotificationCountListner=this;
        initNotificationRealtimeUpdate();
        initBookingRealtimeUpdate();


    }

    private void initBookingRealtimeUpdate() {

        barberDoc= FirebaseFirestore.getInstance().collection("All Saloon").document(Common.state_name).collection("Branch").document(Common.selectedSalon.getSalonId()).collection("Barber").document(Common.userid);
        final Calendar date=Calendar.getInstance();
        date.add(Calendar.DATE,0);
        bookingEvent=new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                loadAvailableTimeSlotofBarber(Common.userid, simpleDateFormat.format(date.getTime()));
            }
        };
        currentBookingDateCollection=barberDoc.collection(simpleDateFormat.format(date.getTime()));
        bookingRealtimeListner=currentBookingDateCollection.addSnapshotListener(bookingEvent);

    }


    private void initNotificationRealtimeUpdate() {
        notificationCollection=FirebaseFirestore.getInstance().collection("All Saloon").document(Common.state_name).collection("Branch").document(Common.selectedSalon.getSalonId()).collection("Barber").document(Common.userid).collection("Notifications");
        notificationEvent=new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots.size()>0)
                    loadNotification();;
            }
        };
        notificationListner=notificationCollection.whereEqualTo("read",false).addSnapshotListener(notificationEvent);
    }

    @Override
    public void onTimeSlotLoadSuccess(List<TimeSlot> timeSlotList) {
        MyTimeSlotAdapter adapter=new MyTimeSlotAdapter(this,timeSlotList);

        recycler_time_slot.setAdapter(adapter);

    }

    @Override
    public void onTimeSlotLoadFailed(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onTimeSlotLoadEmpty() {

        MyTimeSlotAdapter adapter=new MyTimeSlotAdapter(this);
        recycler_time_slot.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.staff_home_menu, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_cart);

        View actionView = MenuItemCompat.getActionView(menuItem);
        txt_notification_badge = (TextView) actionView.findViewById(R.id.cart_badge);

        loadNotification();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return true;
    }

    private void loadNotification() {
            notificationCollection.whereEqualTo("read",false).get().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(StaffHomeActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    if(task.isSuccessful())
                    {
                        iNotificationCountListner.onNotificationCountSuccess(task.getResult().size());
                    }
                }
            });

    }

  @Override
    public void onNotificationCountSuccess(int count) {

        if (count==0)
            txt_notification_badge.setVisibility(View.INVISIBLE);
        else
        {
            txt_notification_badge.setVisibility(View.VISIBLE);
            if (count<=9)
                txt_notification_badge.setText(String.valueOf(count));
            else
                txt_notification_badge.setText("9+");

        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        initBookingRealtimeUpdate();
        initNotificationRealtimeUpdate();
    }

    @Override
    protected void onStop() {
        if (notificationListner!=null)
            notificationListner.remove();
        if (bookingRealtimeListner!=null)
            bookingRealtimeListner.remove();

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (notificationListner!=null)
            notificationListner.remove();
        if (bookingRealtimeListner!=null)
            bookingRealtimeListner.remove();
        super.onDestroy();
    }


}