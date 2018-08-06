package com.pujianto131.myfirebasedispatcher;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnStartScheduler, btnCancelScheduler;


    FirebaseJobDispatcher mDispatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStartScheduler = (Button)findViewById(R.id.btn_set_scheduler);
        btnCancelScheduler = (Button)findViewById(R.id.btn_cancel_scheduler);

        btnStartScheduler.setOnClickListener(this);
        btnCancelScheduler.setOnClickListener(this);
        mDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_set_scheduler){
            startDispatcher();
            Toast.makeText(this, "Dispatcher started", Toast.LENGTH_SHORT).show();
        }
        if (v.getId() == R.id.btn_cancel_scheduler){
            cancelDispatcher();
            Toast.makeText(this, "Dispatcher Cancelled", Toast.LENGTH_SHORT).show();
        }

    }


    private String DISPATCHER_TAG = "mydispatcher";
    private String CITY = "Surabaya";

    public void startDispatcher(){
        Bundle myExtrasBundle = new Bundle();
        myExtrasBundle.putString(MyJobService.EXTRAS_CITY, CITY);

        Job myJob = mDispatcher.newJobBuilder()
                .setService(MyJobService.class)
                .setTag(DISPATCHER_TAG)
                .setRecurring(true)
                .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                .setTrigger(Trigger.executionWindow(0, 60))
                .setReplaceCurrent(true)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setConstraints(
                        Constraint.ON_ANY_NETWORK
                )
                .setExtras(myExtrasBundle)
                .build();
        mDispatcher.mustSchedule(myJob);
    }

    public void cancelDispatcher(){
        mDispatcher.cancel(DISPATCHER_TAG);
    }


}
