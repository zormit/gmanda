package de.fu_berlin.inf.gmanda.util.progress;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class NestableProgressMonitor implements IProgress {

	public NestableProgressMonitor(final Frame parent, final String message) {
		pm = new ProgressDialog(message, parent);
		root = new ChildProgress(pm, 100, ProgressStyle.NORMAL);
	}

	ProgressDialog pm;

	boolean canceled = false;

	ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	ScheduledFuture<?> beeperHandle;

	public void start() {
		if (beeperHandle == null){

			// After 100 millisecs show dialog
			scheduler.schedule(new Runnable(){
				public void run() {
					SwingUtilities.invokeLater(new Runnable(){
						public void run() {
							if (shouldStillStart)
								pm.setVisible(true);
							hasBeenDone = true;
						}
					});
				}
			}, 100, TimeUnit.MILLISECONDS);
			
			
			// Update progress bar
			beeperHandle = scheduler.scheduleAtFixedRate(new Runnable() {
				public void run() {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							pm.update();
						}
					});
				}
			}, 100, 100, TimeUnit.MILLISECONDS);
		}
			
	}

	boolean shouldStillStart = true;
	
	boolean hasBeenDone = false;
	
	public void done() {

		shouldStillStart = false;
		
		if (beeperHandle != null) {
			// Make sure that the dialog is not getting enabled after we canceled it
			beeperHandle.cancel(true);
			beeperHandle = null;
		}
		
		pm.setVisible(false);
		
		scheduler.execute(new Runnable(){
			public void run() {
				while (!hasBeenDone){
					try {
						wait(25);
					} catch (InterruptedException e) {
						// No we do not care
					}
					pm.setVisible(false);
				}
			}
		});
	}
	
	class ProgressDialog extends JDialog implements IProgressReceiver {

		JLabel note = new JLabel("");
		JProgressBar bar = new JProgressBar(JProgressBar.HORIZONTAL, 0, 101);
		JButton button = new JButton(new AbstractAction("Cancel") {
			public void actionPerformed(ActionEvent arg0) {
				canceled = true;
				setEnabled(false);
			}
		});
		
		int done = 0;

		public ProgressDialog(String title, Frame owner) {
			super(owner, title, true);

			JPanel master = new JPanel();
			master.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
			master.setLayout(new BorderLayout(5, 5));

			master.add(note, BorderLayout.PAGE_START);
			master.add(bar, BorderLayout.CENTER);
			JPanel panel = new JPanel();
			panel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
			panel.add(button);
			master.add(panel, BorderLayout.PAGE_END);

			getContentPane().add(master);

			setSize(350, 100);
			
			setLocationRelativeTo(owner);

			pack();
		}

		public void update() {
			bar.setValue(done + 1);
		}

		public void work(int i) {
			done += i;
		}

		public void setNote(String s) {
			if (!bar.isStringPainted())
				bar.setStringPainted(true);
			bar.setString(s);
		}

		public void done() {
			bar.setValue(101);
		}
	}
	
	interface IProgressReceiver {
		
		void work(int i);

		void setNote(String s);
	}
	
	class ChildProgress implements IProgress, IProgressReceiver {

		public ChildProgress(IProgressReceiver parent, int work, ProgressStyle style) {
			this.parent = parent;
			this.work = work;
			this.style = style;
		}

		IProgressReceiver parent;
		int progress = 0;
		int lastContribution = 0;
		int scale = 1;

		int work;

		ProgressStyle style;

		public void setScale(int i) {
			setPandS(progress, i);
		}

		public void setPandS(int newProgress, int newScale) {

			if (newProgress < 0)
				newProgress = 0;
			
			if (newScale < 0)
				newScale = 1;
			
			switch (style) {
			case NORMAL:
				if (newProgress > newScale)
					newProgress = newScale;
			
			case DOUBLING:
				while (newProgress > newScale)
					newScale *= 2;
				break;
			case ROTATING:
				newProgress %= newScale;
				break;
			default:
			}

			int newContribution = 0;
			if (newScale != 0)
				newContribution = newProgress * work / newScale;
			
			parent.work(-lastContribution + newContribution);
			lastContribution = newContribution;
			this.scale = newScale;
			this.progress = newProgress;
		}

		public void setNote(String s) {
			parent.setNote(s);
		}

		public void work(int workDone) {
			setProgress(progress + workDone);
		}

		public void setProgress(int newProgress) {
			setPandS(newProgress, scale);
		}

		public int getProgress() {
			return progress;
		}
		
		public void setStyle(ProgressStyle style){
			this.style = style;
		}

		public IProgress getSub(int work) {
			return new ChildProgress(this, work, ProgressStyle.NORMAL);
		}

		public IProgress getSub(int work, ProgressStyle style) {
			return new ChildProgress(this, work, style);
		}

		public void done() {
			setProgress(scale);
		}

		public void start() {
			setProgress(0);
		}

		public boolean isCanceled() {
			return canceled;
		}
	}
	/**
	 * Dispatch all calls to root IProgress.
	 */

	IProgress root;
	
	public IProgress getSub(int i) {
		return root.getSub(i, ProgressStyle.NORMAL);
	}

	public IProgress getSub(int i, ProgressStyle style) {
		return root.getSub(i, style);
	}

	public void setScale(int i) {
		root.setScale(i);
	}

	public void setNote(String s) {
		root.setNote(s);
	}

	public void setProgress(int i) {
		root.setProgress(i);
	}
	
	public boolean isCanceled() {
		return root.isCanceled();
	}

	public void setStyle(ProgressStyle style) {
		root.setStyle(style);
	}

	public void work(int i) {
		root.work(i);
	}
}
