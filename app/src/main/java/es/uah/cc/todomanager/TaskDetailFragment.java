package es.uah.cc.todomanager;

import android.app.Activity;
import java.text.DateFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import es.uah.cc.todomanager.domain.TaskList;


/**
 * Una actividad que representa una lista de tareas.
 * @author Fernando García Molino Ejr.de Arturo
 * @version 1.0
 */

/**
 * Un fragmento que representa una sola pantalla de detalles de Tarea.
 * Este fragmento está contenido en una {@link TaskListActivity}
 * en modo de dos paneles (en tabletas) o un {@link TaskDetailActivity}
 * en teléfonos.
 */

public class TaskDetailFragment extends Fragment implements CompleteTaskDialog.CompleteDialogListener, CancelTaskDialog.CancelDialogListener {
    public  static  final  String TAG = "es.uah.cc.todomanager.TaskDetailFragment";
    /**
     * La clave para la posición del elemento en la vista de lista incluida en los argumentos de entrada.
     */

    public static final String ARG_ITEM_POS = "es.uah.cc.todomanager.item_ps";

    /**
     * La tarea que presenta este fragmento.
     */
    private TaskList.Task mItem;
    /**
     * La posición de la tarea en la vista de lista.
     */
    private int position;
    /**
     * El oyente de los cambios sufridos por la tarea.
     */
    private OnTaskChangedListener listener = null;
    /**
     * El oyente para las interacciones del botón de edición.
     */
    private OnEditButtonListener editButtonListener;

    public TaskDetailFragment() {
    }
    /**
     *
     Un método de fábrica.
     * @param taskChangedListener El oyente para cambios en la tarea.
     * @param editButtonListener El oyente para las interacciones del botón de edición.
     * @param task La tarea que se presentará.
     * @param position La posición de la tarea en la vista de lista.
     * @return Un instancia de fragmento.
     */


    public static TaskDetailFragment newInstance(OnTaskChangedListener taskChangedListener, OnEditButtonListener editButtonListener, TaskList.Task task, int position) {
        TaskDetailFragment fragment = new TaskDetailFragment();
        fragment.setOnTaskChangedListener(taskChangedListener);
        fragment.setEditButtonListener(editButtonListener);
        Bundle args = new Bundle();
        args.putParcelable(TaskListActivity.ARG_TASK, task);
        args.putInt(ARG_ITEM_POS, position);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Getter for EditButtonListener
     * @return El oyente.
     */
    public OnEditButtonListener getEditButtonListener() {
        return editButtonListener;
    }

    /**
     * El setter para EditButtonListener.
     * @param editButtonListener    El oyente.
     */
    public void setEditButtonListener(OnEditButtonListener editButtonListener) {
        this.editButtonListener = editButtonListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(TaskListActivity.ARG_TASK)) {
            // Load the task.
            mItem = (TaskList.Task) getArguments().getParcelable(TaskListActivity.ARG_TASK);
            position = getArguments().getInt(ARG_ITEM_POS);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(getResources().getString(R.string.task_details_title));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.task_detail, container, false);

        // Shows the task details.
        if (mItem != null) {
fillData(rootView);
showProgressIfComplex(rootView);
        }

        setHasOptionsMenu(true);

        return rootView;
    }

    /**
     *
     * Rellena las vistas de texto con los datos de la tarea.
     * @param rootView La vista raíz.
     */
    protected  void fillData(View rootView) {
    ((TextView) rootView.findViewById(R.id.task_name)).setText(getResources().getString(R.string.task_name) + " " + mItem.getName());
    int p = R.string.medium_priority;
    switch (mItem.getPriority()) {
        case TaskList.Task.HIGH_PRIORITY: p = R.string.high_priority; break;
        case TaskList.Task.MEDIUM_PRIORITY: p = R.string.medium_priority; break;
        case TaskList.Task.LOW_PRIORITY: p = R.string.low_priority; break;
    }
    ((TextView) rootView.findViewById(R.id.task_priority)).setText(getResources().getString(R.string.task_priority) + " " + getResources().getString(p));
    int s = R.string.pending_task;
    switch (mItem.getStatus().getStatusDescription()) {
        case TaskList.PendingTask.STATUS: s = R.string.pending_task; break;
        case TaskList.CompletedTask.STATUS: s = R.string.completed_task; break;
        case TaskList.CanceledTask.STATUS: s = R.string.canceled_task; break;
    }
    ((TextView) rootView.findViewById(R.id.task_status)).setText(getResources().getString(R.string.task_status) + " " + getResources().getString(s));
    ((TextView) rootView.findViewById(R.id.task_description)).setText(getResources().getString(R.string.task_description) + "\n" + mItem.getDetails());
        // If the task has a deadline, it shows it.
    if (mItem.getDeadline() != null) ((TextView) rootView.findViewById(R.id.task_deadline)).setText(getResources().getString(R.string.task_deadline) + " " + DateFormat.getDateInstance(DateFormat.SHORT).format(mItem.getDeadline()));
    else ((View) rootView.findViewById(R.id.task_deadline)).setVisibility(View.INVISIBLE);
}

    /**
     * Muestra una barra de progreso si la tarea es compleja.
     * @param rootView    La vista raíz.
     */
    protected  void showProgressIfComplex(View rootView) {
        if (mItem.isComplex()) {
            SeekBar bar = (SeekBar) rootView.findViewById(R.id.task_progress);
            bar.setProgress(mItem.getCompleted());
            bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    // the task progress is updated each time the progress bar is changed.
                    mItem.setCompleted(progress);
                    // If progress reaches 100%, task is automatically marked as completed.
                    if (progress == 100) completeTask();
                    listener.onTaskChanged(mItem, position);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
//Nothing to do.
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
// Nothing to do.
                }
            });

        }
        else {
            ((View) rootView.findViewById(R.id.task_progress)).setVisibility(View.INVISIBLE);
            ((View) rootView.findViewById(R.id.task_completion)).setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.task_menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        // If task is not pending, the user will not be able to edit, complete or cancel it.
        if (!(mItem.getStatus() instanceof TaskList.PendingTask)) {
            menu.removeItem(R.id.complete_button);
            menu.removeItem(R.id.cancel_button);
            menu.removeItem(R.id.edit_task_option);
        }
    }

    @Override
    public void onComplete(TaskList.Task task, int position) {
        ((TextView) getView().findViewById(R.id.task_status)).setText(getResources().getString(R.string.task_status) + " " + getResources().getString(R.string.completed_task));
        listener.onTaskChanged(task, position);
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onCancel(TaskList.Task task, int position) {
        ((TextView) getView().findViewById(R.id.task_status)).setText(getResources().getString(R.string.task_status) + " " + getResources().getString(R.string.canceled_task));
        listener.onTaskChanged(task, position);
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_task_option: editTask(); return true;
            case R.id.complete_button: completeTask(); return true;
            case R.id.cancel_button: cancelTask(); return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Inicia una actividad  EditTaskActivity.
     */
    protected void editTask() {
editButtonListener.init(mItem);
    }

    /**
     * Marca una tarea como completada.
     */
    protected void completeTask() {
        CompleteTaskDialog dialog = new CompleteTaskDialog(mItem, position, this);
        Bundle args = new Bundle();
        args.putParcelable(TaskListActivity.ARG_TASK, mItem);
        dialog.setArguments(args);
        dialog.show(getFragmentManager(), "CompleteTask");
    }

    /**
     * Marca una tarea como cancelada.
     */
    protected void cancelTask() {
        CancelTaskDialog dialog = new CancelTaskDialog(mItem, -1, this);
        Bundle args = new Bundle();
        args.putParcelable(TaskListActivity.ARG_TASK, mItem);
        dialog.setArguments(args);
        dialog.show(getFragmentManager(), "CancelDialog");
    }

    /**
     * Getter de OnTaskChangedListener.
     * @return El oyente.
     */
    public OnTaskChangedListener getOnTaskChangedListener() {
        return listener;
    }

    /**
     * Setter de OnTaskChangedListener.
     * @param listener    El oyente.
     */
    public void setOnTaskChangedListener(OnTaskChangedListener listener) {
        this.listener = listener;
    }
}

