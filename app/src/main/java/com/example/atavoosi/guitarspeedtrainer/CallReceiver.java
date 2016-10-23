package com.example.atavoosi.guitarspeedtrainer;

import android.content.Context;
import android.util.Log;

import java.util.Date;

public class CallReceiver extends PhonecallReceiver {

    @Override
    protected void onIncomingCallReceived(Context ctx, String number, Date start)
    {
        //بعدا که دکمه پلی-پاوز درست شد، به جای استپ، از پاوز استفاده شود
        MainActivity.stop();
        Log.i("number", "onIncomingCallReceived:" + number);
    }

    @Override
    protected void onIncomingCallAnswered(Context ctx, String number, Date start)
    {
        //

        Log.i("number", "onIncomingCallAnswered:" + number);
    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end)
    {
        //
        Log.i("number", "onIncomingCallEnded:" + number);
    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start)
    {
        //
        Log.i("number", "onOutgoingCallStarted:" + number);
    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end)
    {
        //
        Log.i("number", "onOutgoingCallEnded:" + number);
    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start)
    {
        //
        Log.i("number", "onMissedCall:" + number);
    }

}
