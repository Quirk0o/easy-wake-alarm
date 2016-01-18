package eu.obrok.easywake;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MyActivity extends AppCompatActivity implements TCPListener {

    private static final int PICK_RINGTONE_REQUEST = 1;
    private Ringtone ringtone;
    private AsyncTCPReceiver asyncTCPReceiver;

    public void setRingtone(Ringtone ringtone) {
        this.ringtone = ringtone;
        TextView textView = (TextView) findViewById(R.id.ringtone);

        if (ringtone != null)
            textView.setText(ringtone.getTitle(this));
        else
            textView.setText("Silent");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Uri ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        setRingtone(RingtoneManager.getRingtone(this, ringtoneUri));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PICK_RINGTONE_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

                Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                if (uri == null)
                    setRingtone(null);
                else
                    setRingtone(RingtoneManager.getRingtone(this, uri));
            }
        }
    }

    public void chooseRingtone(View view) {
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        startActivityForResult(intent, PICK_RINGTONE_REQUEST);
    }

    public void createServer(View view) {
        ToggleButton toggle = (ToggleButton) findViewById(R.id.togglebutton);
        if (toggle.isChecked()) {
            asyncTCPReceiver = new AsyncTCPReceiver();
            asyncTCPReceiver.addListener(this);
            asyncTCPReceiver.execute();
        }
        else {
            asyncTCPReceiver.cancel(true);
            ringtone.stop();
        }
    }

    @Override
    public void packetReceived() {
        ringtone.play();
    }
}
