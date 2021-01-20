package ec.solmedia.photoplace.main.ui.settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .add(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
