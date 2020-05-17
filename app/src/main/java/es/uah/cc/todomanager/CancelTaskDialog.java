package es.uah.cc.todomanager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import es.uah.cc.todomanager.domain.TaskList;

/**
 * Una actividad que representa una lista de tareas.
 * @author Fernando García Molino Ejr.de Arturo
 * @version 1.0
 */
/**
 * Un diálogo para confirmar la cancelación de una tarea.
 */

public class CancelTaskDialog extends DialogFragment {
    private static final String TAG = "CancelTaskDialog";

    private TaskList.Task task;
    private int position;
    private CancelDialogListener listener;

    /**
     constructor para un CancelTaskDialog.
     * @param task La tarea que se está cancelando.
     * @param position La posición de la tarea en la vista de lista.
     * @param listener El oyente para cancelar eventos.
     */
    public CancelTaskDialog(TaskList.Task task, int position, CancelDialogListener listener) {
        this.task = task;
        this.position = position;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final TaskList.Task task = getArguments().getParcelable(TaskListActivity.ARG_TASK);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder.setMessage(String.format(getResources().getString(R.string.cancel_task_dialog_message), task.getName()))
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        task.cancel();
                        listener.onCancel(task, position);
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Nothing to do.
                    }
                })
                .create();
    }

    /**
     * Un oyente para los eventos CancelTaskDialog.
     */
    public static interface  CancelDialogListener {
        /**
         * Todo cuando se cancela una tarea.
         * @param task La tarea que se ha cancelado.
         * @param position La posición de la tarea en la vista de lista.
         */
        void onCancel(TaskList.Task task, int position);
    }
}

