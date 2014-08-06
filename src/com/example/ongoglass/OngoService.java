package com.example.ongoglass;

import java.io.IOException;

import com.google.android.glass.timeline.DirectRenderingCallback;
import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.LiveCard.PublishMode;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.hardware.Camera;
import android.os.IBinder;
import android.view.SurfaceHolder;
import android.widget.RemoteViews;

public class OngoService extends Service {
	private static final String LIVE_CARD_TAG = "tatdbg_ongoing";
//	private final Handler mHandler = new Handler();

	private LiveCard mLiveCard;
	private Camera mCamera;
	
	private class RenderingCallback implements DirectRenderingCallback {
		@Override
		public void surfaceChanged(SurfaceHolder holder,
				int format, int width, int height) {
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			mCamera = Camera.open();
			try {
				mCamera.setPreviewDisplay(holder);
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
			mCamera.startPreview();
//			mHandler.postDelayed(new Runnable() {
//				@Override
//				public void run() {
//					mLiveCard.unpublish();
//					mHandler.postDelayed(new Runnable() {
//						@Override
//						public void run() {
//							mLiveCard.publish(PublishMode.REVEAL);
//						}
//					}, 10 * 1000);
//				}
//			}, 3 * 1000);
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			mCamera.stopPreview();
			mCamera.release();
		}

		@Override
		public void renderingPaused(SurfaceHolder holder, boolean paused) {
			if (paused) {
				mCamera.stopPreview();
			} else {
				mCamera.startPreview();
			}
		}
	}

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
			mLiveCard.setDirectRenderingEnabled(true)
					.getSurfaceHolder().addCallback(new RenderingCallback());
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
//		mHandler.post(new Runnable() {
//			@Override
//			public void run() {
//				mLiveCard.navigate();
//				mHandler.postDelayed(this, 30 * 1000);
//			}
//		});
		return START_STICKY;
	}

}
