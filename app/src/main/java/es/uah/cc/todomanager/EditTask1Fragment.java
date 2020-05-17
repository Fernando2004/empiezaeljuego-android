package es.uah.cc.todomanager;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.Calendar;

import es.uah.cc.todomanager.domain.TaskList;

/**
 * Una actividad que representa una lista de tareas.
 * @author Fernando García Molino Ejr.de Arturo
 * @version 1.0
 */
/**
 * Una simple subclase {@link Fragment}.
 *  * Las actividades que contienen este fragmento deben implementar el
 *  * {@link EditTask1Fragment.OnFragmentInteractionListener} interfaz
 *  * para manejar eventos de interacción.
 *  * Utilice el método de fábrica {@link EditTask1Fragment # newInstance} para
 *  * crea una instancia de este fragmento.
 */
public class EditTask1Fragment extends Fragment {
    public static final String TAG = "es.uah.cc.todomanager.EditTask1Fragment";
    /**
     * Una clave para transacciones y argumentos de tareas.
     */
    public static final String EDIT_TASK_1 = "es.uah.cc.todomanager.edittask1";
    /**
     * Resultado: se completó la creación de la tarea.
     */
    public static final int TASK_CREATION_COMPLETED = 1;
    /**
     * Resultado: se completó la edición de la tarea.
     */
    public static final int TASK_EDITION_COMPLETED = 2;
    /**
     * Resultado: se canceló la creación de la tarea.
     */
    public static final int TASK_CREATION_CANCELED = 0;
    /**
     * Resultado: se canceló la edición de la tarea.
     */
    public static  final  int TASK_EDITION_CANCELED = -1;
    /**
     * Oyente para eventos.
     */
    private OnEditTaskListener listener;
    /**
     * La tarea que se está editando.
     */
    private TaskList.Task task;

    public EditTask1Fragment() {
        // Required empty public constructor
    }

    /**
     * Un método de fábrica.
     * @param listener El oyente.
     * @param task La tarea a editar.
     * @return Una nueva instancia del fragmento.
     */
    public static EditTask1Fragment newInstance(OnEditTaskListener listener, TaskList.Task task) {
        EditTask1Fragment fragment = new EditTask1Fragment();
        fragment.setOnEditTaskListener(listener);
        Bundle args = new Bundle();
        args.putParcelable(EDIT_TASK_1, task);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Getter para el  listener.
     * @return El oyente.
     */
    public OnEditTaskListener getOnEditTaskListener() {
        return listener;
    }

    /**
     * Setter para el listener.
     * @param listener    El oyente.
     */
    public void setOnEditTaskListener(OnEditTaskListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = this.getActivity();
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(getResources().getString(R.string.title_edit_task_1));
        }

        if (getArguments().containsKey(EDIT_TASK_1)) {
task = getArguments().getParcelable(EDIT_TASK_1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_task1, container, false);
        loadTaskData(rootView);
        return  rootView;
    }

    /**
     * Cargue los datos de la tarea en los campos del formulario.
     * @param view La vista raíz.
     */
    protected void loadTaskData(View view) {
        final View a = view;
        ((EditText) a.findViewById(R.id.task_name)).setText(task.getName());
        Spinner prioritySp = ((Spinner) a.findViewById(R.id.task_priority));
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.priority_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySp.setAdapter(adapter);
        switch (task.getPriority()) {
            case TaskList.Task.HIGH_PRIORITY: prioritySp.setSelection(0); break;
            case TaskList.Task.MEDIUM_PRIORITY: prioritySp.setSelection(1); break;
            case TaskList.Task.LOW_PRIORITY: prioritySp.setSelection(2);
        }
        ((EditText) a.findViewById(R.id.task_description)).setText(task.getDetails());
        ((CheckBox) a.findViewById(R.id.task_is_complex)).setChecked(task.isComplex());
        boolean hasDeadline = task.getDeadline() != null;
        CheckBox deadlineChk = (CheckBox) a.findViewById(R.id.task_has_deadline);
        deadlineChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                View finishButton = a.findViewById(R.id.finish_button);
                View nextButton = a.findViewById(R.id.next_button);
                if (isChecked) {
        finishButton.setVisibility(View.INVISIBLE);
                    nextButton.setVisibility(View.VISIBLE);
                } else {
        finishButton.setVisibility(View.VISIBLE);
                    nextButton.setVisibility(View.INVISIBLE);
                }
            }
        });
        if (hasDeadline) {
            deadlineChk.setChecked(true);
            ((View) a.findViewById(R.id.finish_button)).setVisibility(View.INVISIBLE);
        } else {
            deadlineChk.setChecked(false);
            ((View) a.findViewById(R.id.next_button)).setVisibility(View.INVISIBLE);
        }
        ((Button) a.findViewById(R.id.finish_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFinishPressed(v);
            }
        });
        ((Button) a.findViewById(R.id.next_button)).setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNextPressed(v);
            }
        }));
        ((Button) a.findViewById(R.id.cancel_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelPressed(v);
            }
        });
    }

    /**
     * Va a la pantalla del selector de fecha.
     * @param view La vista presionada.
     */
    public void onNextPressed(View view) {
        updateTask(task);
        listener.onNextStep(task);
    }

    /**
     * Cancela la edición.
     * @param view La vista presionada.
     */
    public void onCancelPressed(View view) {
        listener.onCancel(task);
    }

    /**
     * Termina la edición.
     * @param view La vista presionada.
     */
    public void onFinishPressed(View view) {
updateTask(task);
        listener.onFinish(task);
    }

    /**
     * Actualiza los datos de la tarea con las entradas del formulario.
     * @param task La tarea a actualizar.
     */
    protected void updateTask(TaskList.Task task) {
        View v = getView();
        task.setName(((EditText) v.findViewById(R.id.task_name)).getText().toString());
        task.setDetails(((EditText) v.findViewById(R.id.task_description)).getText().toString());
task.setComplex(((CheckBox) v.findViewById(R.id.task_is_complex)).isChecked());
        if (((CheckBox) v.findViewById(R.id.task_has_deadline)).isChecked())
            task.setDeadline(Calendar.getInstance().getTime());
        else task.setDeadline(null);
        switch (((Spinner) v.findViewById(R.id.task_priority)).getSelectedItemPosition()) {
            case 0: task.setPriority(TaskList.Task.HIGH_PRIORITY); break;
            case 1: task.setPriority(TaskList.Task.MEDIUM_PRIORITY); break;
            case 2: task.setPriority(TaskList.Task.LOW_PRIORITY);
        }
    }

}

