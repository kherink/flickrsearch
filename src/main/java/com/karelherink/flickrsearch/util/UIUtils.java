package com.karelherink.flickrsearch.util;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import com.karelherink.flickrsearch.App;

/**
 * Created by kaja on 21/09/2014.
 */
public class UIUtils {

    public static void toastOnUIThread(final int stringResId) {
        (new Handler(Looper.getMainLooper())).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(App.getContext(), stringResId, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
