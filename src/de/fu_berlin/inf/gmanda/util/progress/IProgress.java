package de.fu_berlin.inf.gmanda.util.progress;


/**
 * An interface for nestable Progress Monitors.
 * 
 * The default implementation to use is NestableProgressMonitor.
 */
public interface IProgress {

	public enum ProgressStyle {
		NORMAL,
		DOUBLING,
		ROTATING
	}
	
	/**
	 * Return a new nested IProgress that will contribute the amount given to
	 * the total work for this progress.
	 * 
	 * @param workDoneBySubprogress
	 *            The amount of work that the nested IProgress will contribute
	 *            to this progress.
	 * 
	 * @return A new nested IProgress
	 */
	IProgress getSub(int workDoneBySubprogress);
	
	public void setStyle(ProgressStyle style);
	
	/**
	 * Returns a new nested IProgress for which the total amount of work is not known.
	 * 
	 * This IProgress will double its scale automatically as soon as the progress exceeds the 
	 * scale.  
	 * 
	 * @param workDoneBySubprogress
	 * @return
	 */
	IProgress getSub(int workDoneBySubprogress, ProgressStyle style);
	
	/**
	 * This method can be used to set the total amount of work associated with
	 * this progress.
	 * 
	 * @param totalWork
	 *            The total amount of work associated with this progress.
	 */
	void setScale(int totalWork);

	/**
	 * Add the given amount to the amount of work being done.
	 * 
	 * This is a convenience method for keeping track of the total work being
	 * done.
	 * 
	 * @param additionalWorkDone
	 *            Adds the given amount to the total amount of work being done.
	 */
	void work(int i);

	/**
	 * Set the work already done to the value given.
	 * 
	 * @param totalWorkDone
	 *            The total amount of work already accomplished.
	 */
	void setProgress(int totalWorkDone);

	/**
	 * This can be called to notify the user of the work currently being done.
	 * 
	 * @param s
	 */
	void setNote(String s);

	/**
	 * This should be called if the work activities are begun.
	 * 
	 * IProgress implementors will start clocking the time, showing interfaces,
	 * resetting progress, etc.
	 * 
	 */
	void start();

	/**
	 * This should be called if all the work activities are finished.
	 * 
	 * IProgress implementors will close their interfaces, report completion,
	 * etc.
	 * 
	 */
	void done();

	/**
	 * Will return true if the user has clicked the cancel button.
	 * 
	 * The operation should terminate at the next possible moment.
	 * 
	 * @return Whether the user has clicked the cancel button.
	 */
	boolean isCanceled();
}
