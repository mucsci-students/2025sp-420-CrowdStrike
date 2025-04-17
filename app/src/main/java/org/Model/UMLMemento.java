package org.Model;
import java.util.Stack;



public class UMLMemento
{

    // Each operation should be tied to its own stack for ease of use
    private final Stack<UMLModel> undoHistory = new Stack<>();
    private final Stack<UMLModel> redoHistory = new Stack<>();
    
    private UMLModel currentState;

    /**
     * Updates the current snapshot of the model
     * 
     * Called after every operation
     * @return nothing
     */
    public void saveState(UMLModel currentModel)
    {
        // Push current model onto undo's stack
        currentState = currentModel.deepCopy();
        undoHistory.push(currentState);

        // Clear redo stack since this is a "new" action
        redoHistory.clear();

    }


    /**
     *
     */
    public UMLModel undoState() throws Exception
    {
        if(undoHistory.size()==1)
            {throw new Exception ("Error: There is nothing to undo!");}

        // The most recent state is simply the top of the undo stack
        currentState = undoHistory.pop();

        // Since something is being undone, we should remember how to possibly redo it 
        redoHistory.push(currentState.deepCopy());

        return undoHistory.peek().deepCopy();
    }

    /**
     * 
     */
    public UMLModel redoState() throws Exception
    {
        if(redoHistory.isEmpty())
            {throw new Exception ("Error: There is nothing to redo!");}

        // The most recent undo that we could redo is at the top of the redo stack (courtesy of undoState())
        currentState = redoHistory.pop();

        // Since something is being redone, we should remember how to possibly undo it 
        undoHistory.push(currentState.deepCopy());

        return currentState;
    }

}
