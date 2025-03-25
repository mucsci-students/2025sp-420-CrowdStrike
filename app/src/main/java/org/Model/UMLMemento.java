package org.Model;
import java.util.Stack;


public class UMLMemento
{

    // Each operation should be tied to its own stack for ease of use
    private final Stack<UMLModel> undoHistory = new Stack<>();
    private final Stack<UMLModel> redoHistory = new Stack<>();

    /**
     * Gets a snapshot of the entire model and saves it as a "state"
     * 
     * Called after every operation
     * @return nothing
     */
    public void saveState(UMLModel currentModel)
    {
        // Push current model onto undo's stack
        undoHistory.push(currentModel);

        // Clear redo stack since this is a "new" action
        redoHistory.clear();

    }

    /**
     *
     */
    public UMLModel undoState() throws Exception
    {
        if(undoHistory.isEmpty())
            {throw new Exception ("Error: There is nothing to undo!");}

        // The most recent state is simply the top of the undo stack
        UMLModel state = undoHistory.pop();

        // Since something is being undone, we should remember how to possibly redo it 
        redoHistory.push(state);

        return state;
    }

    /**
     * 
     */
    public UMLModel redoState() throws Exception
    {
        if(redoHistory.isEmpty())
            {throw new Exception ("Error: There is nothing to redo!");}

        // The most recent undo that we could redo is at the top of the redo stack (courtesy of undoState())
        UMLModel state = redoHistory.pop();

        // Since something is being redone, we should remember how to possibly undo it 
        undoHistory.push(state);

        return state;
    }

}
