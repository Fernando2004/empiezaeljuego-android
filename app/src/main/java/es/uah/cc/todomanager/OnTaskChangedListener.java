package es.uah.cc.todomanager;

import es.uah.cc.todomanager.domain.TaskList;

/**
 * Una actividad que representa una lista de tareas.
 * @author Fernando García Molino Ejr.de Arturo
 * @version 1.0
 */

/**
 * Un oyente para hacer algunas acciones cuando una tarea cambia.
 */
public interface OnTaskChangedListener {
    void onTaskChanged(TaskList.Task task, int position);
}
