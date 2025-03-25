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
        try
        {
            // push current model onto undo's stack
            //undoHistory.push(<CURRENT_STATE>);

            // clear redo stack since this is a "new" action
            redoHistory.clear();
        }
        catch(Exception e)
        {

        }

    }

    /**
     *
     */
    public UMLModel undoState() throws Exception
    {
        try
        {
            if(undoHistory.isEmpty())
                {throw new Exception ("Error: There is nothing to undo!");}

            // The most recent state is simply the top of the undo stack
            UMLModel state = undoHistory.pop();

            // Since something is being undone, we should remember how to possibly redo it 
            //redoHistory.push(<CURRENT_STATE>)

            return state;

        }
        catch(Exception e)
        {
            return null;
        }

    }

    /**
     * 
     */
    public UMLModel redoState() throws Exception
    {
        try
        {
            if(redoHistory.isEmpty())
                {throw new Exception ("Error: There is nothing to redo!");}

            // The most recent undo that we could redo is at the top of the redo stack (courtesy of undoState())
            UMLModel state = redoHistory.pop();

            // Since something is being redone, we should remember how to possibly undo it 
            //undoHistory.push(<CURRENT_STATE>);

            return state;

        }
        catch(Exception e)
        {
            return null;
        }

    }

}
