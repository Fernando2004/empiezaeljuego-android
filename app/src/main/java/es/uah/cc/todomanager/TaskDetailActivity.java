package es.uah.cc.todomanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import es.uah.cc.todomanager.R;
import es.uah.cc.todomanager.domain.TaskList;

/**
 * Una actividad que representa una lista de tareas.
 * @author Fernando García Molino Ejr.de Arturo
 * @version 1.0
 */

/**
 * Una actividad que representa una sola pantalla de detalles de Tarea.
 *  La actividad solo se utiliza en dispositivos de ancho estrecho.
 *  En dispositivos del tamaño de una tableta,
 *  los detalles del artículo se presentan junto con una lista de artículos
 *  en una {@link TaskListActivity}.
 */
public class TaskDetailActivity extends AppCompatActivity implements OnTaskChangedListener{
    private final static String TAG = "TaskDetailActivity";
    /**
     * El código de actividad para el código de solicitud.
     */
    public static final int ACTIVITY_CODE = 1;
    /**
     * Resultado: la tarea cambió.
     */
    public final static int CHANGED = 1;
    /**
     * Resultado: la tarea no cambió.
     */
    public final static int NOT_CHANGED = 0;
    /**
     * Un campo que indica si la tarea cambió o no.
     */
    private boolean changed = false;
    /**
     * La posición de la tarea en la vista de lista.
     */
    private int position;
    /**
     * La tarea a mostrar.
     */
    private TaskList.Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            task = getIntent().getParcelableExtra(TaskListActivity.ARG_TASK);
            position = getIntent().getIntExtra(TaskDetailFragment.ARG_ITEM_POS, -1);
            TaskDetailFragment fragment = TaskDetailFragment.newInstance(this, new OnDetailsEditButtonListener(), task, position);
            getSupportFragmentManager().beginTransaction()
                    .addToBackStack(TaskDetailFragment.TAG)
                    .add(R.id.task_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, TaskListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskChanged(TaskList.Task task, int position) {
        changed = true;
        this.task = task;
    }

    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(TaskDetailFragment.ARG_ITEM_POS, position);
        resultIntent.putExtra(TaskListActivity.ARG_TASK, task);
        int result = changed ? CHANGED : NOT_CHANGED;
        setResult(result, resultIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EditTaskActivity.ACTIVITY_CODE) {
            if (resultCode == EditTask1Fragment.TASK_EDITION_COMPLETED) {
                changed = true;
                task = data.getParcelableExtra(TaskListActivity.ARG_TASK);
            }
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        TaskDetailFragment fragment = TaskDetailFragment.newInstance(this, new OnDetailsEditButtonListener(), task, position);
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.task_detail_container, fragment)
                .commit();
    }

    protected  class OnDetailsEditButtonListener implements OnEditButtonListener {
        @Override
        public void init(TaskList.Task task) {
            Intent intent = new Intent(getApplicationContext(), EditTaskActivity.class);
            intent.putExtra(TaskListActivity.ARG_TASK, task);
            startActivityForResult(intent, EditTaskActivity.ACTIVITY_CODE);
        }
    }
}

