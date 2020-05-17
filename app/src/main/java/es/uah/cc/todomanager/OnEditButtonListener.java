package es.uah.cc.todomanager;

import es.uah.cc.todomanager.domain.TaskList;

/**
 * Una actividad que representa una lista de tareas.
 * @author Fernando García Molino Ejr.de Arturo
 * @version 1.0
 */
/**
 * Un oyente para el evento de inicio de edición.
 */
public interface OnEditButtonListener {
    /**
     * Inicia el procedimiento de edición.
     * @param task
     */
    void init(TaskList.Task task);
}
