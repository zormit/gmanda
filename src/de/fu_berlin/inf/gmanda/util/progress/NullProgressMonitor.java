package de.fu_berlin.inf.gmanda.util.progress;

/**
 * A Progress Monitor that does nothing
 */
public class NullProgressMonitor implements IProgress {

	public void done() {
		
	}

	public IProgress getSub(int workDoneBySubprogress) {
		return this;
	}

	public IProgress getSub(int workDoneBySubprogress, ProgressStyle style) {
		return this;
	}

	public boolean isCanceled() {
		return false;
	}

	public void setNote(String s) {
		
	}

	public void setProgress(int totalWorkDone) {
		
	}

	public void setScale(int totalWork) {
		
	}

	public void setStyle(ProgressStyle style) {
		
	}

	public void start() {
	
	}

	public void work(int i) {
		
	}
}
