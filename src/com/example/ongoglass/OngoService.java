package com.example.ongoglass;

import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.LiveCard.PublishMode;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

public class OngoService extends Service {
	private static final String LIVE_CARD_TAG = "tatdbg_ongoing";
	private LiveCard mLiveCard;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (mLiveCard == null) {
			mLiveCard = new LiveCard(this, LIVE_CARD_TAG);
			mLiveCard.setViews(new RemoteViews(getPackageName(),
					R.layout.layout_card));
			Intent menuIntent = new Intent(this, MainActivity.class);
			menuIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
					Intent.FLAG_ACTIVITY_CLEAR_TASK);
			mLiveCard.setAction(PendingIntent.getActivity(
					this, 0, menuIntent, 0));
			mLiveCard.attach(this);
			mLiveCard.publish(PublishMode.REVEAL);
		} else {
			mLiveCard.navigate();
		}
		return START_STICKY;
	}

}
