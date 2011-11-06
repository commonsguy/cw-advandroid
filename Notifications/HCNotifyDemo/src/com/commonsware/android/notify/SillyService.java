package com.commonsware.android.notify;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.SystemClock;
import android.provider.Settings;
import android.widget.RemoteViews;


public class SillyService extends IntentService {
  private static int NOTIFICATION_ID=1337;
  
  public SillyService() {
    super("SillyService");
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    NotificationManager mgr=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    Notification.Builder builder=new Notification.Builder(this);

    builder
      .setContent(buildContent(0))
      .setTicker(getText(R.string.ticker), buildTicker())
      .setContentIntent(buildContentIntent())
      .setDeleteIntent(buildDeleteIntent())
      .setLargeIcon(buildLargeIcon())
      .setSmallIcon(R.drawable.ic_stat_notif_small_icon);
    
    Notification notif=builder.getNotification();
    
    notif.flags=notif.flags | Notification.FLAG_ONGOING_EVENT;

    for (int i=0;i<20;i++) {
      notif.contentView.setProgressBar(android.R.id.progress,
                                       100, i*5, false);
      mgr.notify(NOTIFICATION_ID, notif);
      
      if (i==0) {
        notif.tickerText=null;
        notif.tickerView=null;
      }
      
      SystemClock.sleep(1000);
    }
    
    mgr.cancel(NOTIFICATION_ID);
  }
  
  private PendingIntent buildDeleteIntent() {
    Intent i=new Intent(Settings.ACTION_SOUND_SETTINGS);
    
    return(PendingIntent.getActivity(this, 0, i, 0));
  }

  private Bitmap buildLargeIcon() {
    Bitmap raw=BitmapFactory.decodeResource(getResources(),
                                            R.drawable.icon);
    
    return(raw);
  }

  private RemoteViews buildTicker() {
    RemoteViews ticker=new RemoteViews(this.getPackageName(),
                                       R.layout.ticker);
    
    ticker.setTextViewText(R.id.ticker_text,
                           getString(R.string.ticker));
    
    return(ticker);
  }

  private PendingIntent buildContentIntent() {
    Intent i=new Intent(Settings.ACTION_SETTINGS);
    
    return(PendingIntent.getActivity(this, 0, i, 0));
  }

  private RemoteViews buildContent(int progress) {
    RemoteViews content=new RemoteViews(this.getPackageName(),
                                       R.layout.content);
    
    return(content);
  }
}