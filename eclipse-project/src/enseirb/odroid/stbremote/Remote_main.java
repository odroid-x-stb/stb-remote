package enseirb.odroid.stbremote;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Remote_main extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_remote_main, menu);
        return true;
    }
}
