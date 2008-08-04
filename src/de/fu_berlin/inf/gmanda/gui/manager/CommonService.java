package de.fu_berlin.inf.gmanda.gui.manager;

import java.awt.Frame;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JOptionPane;

import de.fu_berlin.inf.gmanda.exceptions.DoNotShowToUserException;
import de.fu_berlin.inf.gmanda.exceptions.ReportToUserException;
import de.fu_berlin.inf.gmanda.proxies.ForegroundWindowProxy;
import de.fu_berlin.inf.gmanda.util.progress.IProgress;
import de.fu_berlin.inf.gmanda.util.progress.NestableProgressMonitor;

public class CommonService {

	ForegroundWindowProxy windowProxy;

	public CommonService(ForegroundWindowProxy windowProxy) {
		this.windowProxy = windowProxy;
	}

	ExecutorService threadService = Executors.newSingleThreadExecutor();

	/**
	 * Run the given runnable using a single item thread pool.
	 * 
	 * This methods returns immediately.
	 * 
	 * Exceptions thrown from the runnable that are either
	 * {@link DoNotShowToUserException} or {@link ReportToUserException} will be
	 * caught and processed.
	 * 
	 * @param r
	 */
	public void run(final Runnable r, final String messageToUserOnError) {
		threadService.execute(wrapExceptions(r, messageToUserOnError));
	}
	
	public void runSync(Runnable r, String messageToUserOnError) {
		wrapExceptions(r, messageToUserOnError).run();
	}
	
	public Runnable wrapExceptions(final Runnable r, final String messageToUserOnError){
		return new Runnable() {
			public void run() {
				try {
					r.run();
				} catch (DoNotShowToUserException e) {
					// TODO maybe put into log files...
				} catch (ReportToUserException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(windowProxy.getAsFrameOrNull(),
						messageToUserOnError + ":\n" + e.getErrorMessage());
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		};
	}

	public IProgress getProgressBar(String title) {
		return new NestableProgressMonitor(windowProxy.getAsFrameOrNull(), title);
	}

	public IProgress getProgressBar(String title, int todo) {
		IProgress progress = new NestableProgressMonitor(windowProxy.getAsFrameOrNull(), title);
		progress.setScale(todo);
		progress.start();
		return progress;
	}
	
	public Frame getForegroundWindowOrNull(){
		return windowProxy.getAsFrameOrNull();
	}
	
	public void log(String s){
		System.out.println(s);
	}

}
