package ui;

import exception.LogException;
import model.EventLog;

/**
 * Code in this class is retrieved from:
 * https://github.students.cs.ubc.ca/CPSC210/AlarmSystem
 *
 * Defines behaviours that event log printers must support.
 */
public interface LogPrinter {
	/**
     * MODIFIES: EventLog
     * EFFECTS Prints the log
	 * @param el  the event log to be printed
	 * @throws LogException when printing fails for any reason
	 */
    void printLog(EventLog el) throws LogException;


    //EFFECTS: Prints the events on to the console
    void consolePrint(EventLog el);

    //MODIFIES: EventLog
    //EFFECTS: clears the event log
    void clearLog(EventLog el);
}
