package es.iessaladillo.pedrojoya.pr167;

import android.app.Application;

import com.facebook.stetho.Stetho;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
