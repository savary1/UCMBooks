package fdi.pad.clearData;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import fdi.pad.libro.LibroExecutor;
import fdi.pad.ucmbooks.MainActivity;
import fdi.pad.ucmbooks.R;

public class ClearDataActivity extends AppCompatActivity {

    final LibroExecutor libros = new LibroExecutor(MainActivity.mainContext);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clear_data);
        Toolbar toolbar = findViewById(R.id.toolbarClearData);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Button button = findViewById(R.id.buttonClearData);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                libros.deleteAllFromFileSystem();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
