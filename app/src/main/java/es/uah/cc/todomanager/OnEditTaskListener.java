package es.uah.cc.todomanager;

import es.uah.cc.todomanager.domain.TaskList;

/**
 * Una actividad que representa una lista de tareas.
 * @author Fernando García Molino Ejr.de Arturo
 * @version 1.0
 */

/**
 *
 Esta interfaz debe implementarse mediante actividades que contengan tarea de edición
 * fragmento para permitir que se comunique una interacción en estos fragmentos
 * a la actividad y potencialmente otros fragmentos contenidos en ese
 * actividad.
 * <p>
 * Vea la lección de capacitación de Android <a href =
 * "http://developer.android.com/training/basics/fragments/communicating.html"
 *> Comunicación con otros fragmentos </a> para más información.
 */

public interface OnEditTaskListener {
    void onNextStep(TaskList.Task task);
    void onPreviousStep(TaskList.Task task);
    void onCancel(TaskList.Task task);
    void onFinish(TaskList.Task task);
}
