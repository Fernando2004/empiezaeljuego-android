package es.uah.cc.todomanager;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;


import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import es.uah.cc.todomanager.domain.TaskList;

/**
 * Una actividad que representa una lista de tareas.
 * @author Fernando García Molino Ejr.de Arturo
 * @version 1.0
 */

public class CompleteTaskDialog extends DialogFragment {
    private static final String TAG = "CompleteTaskDialog";

    private TaskList.Task task;
    private int position;
    private CompleteDialogListener listener;

    public CompleteTaskDialog(TaskList.Task task, int position, CompleteDialogListener listener) {
        this.task = task;
        this.position = position;
        this.listener = listener;
    }

    @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
        final TaskList.Task task = getArguments().getParcelable(TaskListActivity.ARG_TASK);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            return builder.setMessage(String.format(getResources().getString(R.string.complete_task_dialog_message), task.getName()))
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
task.complete();
                            listener.onComplete(task, position);
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Nothing to do.
                        }
                    })
                    .create();
        }

        public static interface CompleteDialogListener {
void onComplete(TaskList.Task task, int position);
        }
}

