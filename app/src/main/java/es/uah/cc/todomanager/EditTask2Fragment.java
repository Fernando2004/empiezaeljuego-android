package es.uah.cc.todomanager;


import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.Calendar;
import java.util.Date;

import es.uah.cc.todomanager.domain.TaskList;

/**
 * Una actividad que representa una lista de tareas.
 * @author Fernando García Molino Ejr.de Arturo
 * @version 1.0
 */
/**
 * Un simple {@link Fragment} subclass.
 * Usado para {@link EditTask2Fragment#newInstance} método de fábrica para
 * crea una instancia de este fragmento.
 */
public class EditTask2Fragment extends Fragment {

    public static final String TAG = "es.uah.cc.todomanager.EditTask2Fragment";
    /**
     * La clave para transacciones y datos de tareas en los argumentos.
     */
    public static final String EDIT_TASK_2 = "es.uah.cc.todomanager.edittask2";
    /**
     * La tarea que se está editando.
     */
    private TaskList.Task task;
    /**
     * El oyente para eventos.
     */
    private OnEditTaskListener listener;

    public EditTask2Fragment() {
        /**
         * Obligatorio constructor público vacío
          */
    }

    /**
     * Getter para OnEditTaskListener.
     * @return El Oyente
     */
    public OnEditTaskListener getOnEditTaskListener() {
        return listener;
    }

    /**
     * Setter para OnEditTaskListener.
     * @param listener
     */
    public void setOnEditTaskListener(OnEditTaskListener listener) {
        this.listener = listener;
    }

    /**
     *
     Método de fábrica
     * @param listener El oyente a usar.
     * @param task La tarea a editar.
     * @return Una nueva instancia del fragmento.
     */

    public static EditTask2Fragment newInstance(OnEditTaskListener listener, TaskList.Task task) {
        EditTask2Fragment fragment = new EditTask2Fragment();
        Bundle args = new Bundle();
        args.putParcelable(EDIT_TASK_2, task);
        fragment.setArguments(args);
        fragment.setOnEditTaskListener(listener);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = this.getActivity();
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(getResources().getString(R.string.title_edit_task_2));
        }

        if (getArguments().containsKey(EDIT_TASK_2)) {
            task = getArguments().getParcelable(EDIT_TASK_2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_task2, container, false);
setup(rootView);
        return rootView;
    }

    /**
     * Muestra el selector de fecha y configura el resto del diseño.
     * @param view
     */
    protected void setup(View view) {
        final View v = view;
        DatePicker picker = (DatePicker) v.findViewById(R.id.task_deadline);
        picker.setMinDate(Calendar.getInstance().getTimeInMillis() - 1000);
        final Date d = task.getDeadline();
        picker.init(d.getYear(), d.getMonth(), d.getDay(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, monthOfYear);
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                task.setDeadline(c.getTime());
            }
        });
        ((Button) v.findViewById(R.id.finish_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFinishPressed(v);
            }
        });
        ((Button) v.findViewById(R.id.previous_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPreviousPressed(v);
            }
        });
        ((Button) v.findViewById(R.id.cancel_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelPressed(v);
            }
        });
    }

    /**
     * Termina la edición.
     * @param v La vista presionada.
     */
    protected void onFinishPressed(View v) {
    listener.onFinish(task);
}

    /**
     *
     * Cancela la edición de la tarea.
     * @param v La vista presionada.
     */
    protected void onCancelPressed(View v) {
    listener.onCancel(task);
}

    /**
     * Va a la pantalla anterior.
     * @param v    La vista presionada.
     */
    protected  void  onPreviousPressed(View v) {
listener.onPreviousStep(task);
}

}
