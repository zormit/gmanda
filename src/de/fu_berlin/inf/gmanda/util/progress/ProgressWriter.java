package de.fu_berlin.inf.gmanda.util.progress;

import java.io.File;
import java.io.FileWriter;
import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;

import de.fu_berlin.inf.gmanda.exceptions.UserCancelationException;

/**
 * Writer that counts the number of bytes succesfully written and
 * notifies a listener of changes.
 */
public class ProgressWriter extends FilterWriter {

	protected int count = 0;

	protected StatusListener listener;

	/**
	 * Use this is you want to use the progress input stream and use your own
	 * listener;
	 */
	public interface StatusListener {
		void setStatus(int status);

		void close();
	}

	public static class IProgressListener implements StatusListener {

		int bytesPerProgress = 1;
		int status = -1;

		public IProgress progress;

		public void close() {
			progress.done();
		}

		public IProgressListener(IProgress progress, int bytesPerProgress) {
			this.progress = progress;
			progress.setStyle(IProgress.ProgressStyle.ROTATING);
			progress.setScale(100);
			this.bytesPerProgress = Math.max(1, bytesPerProgress);
		}

		public void setStatus(int status) {
			if (progress.isCanceled())
				throw new UserCancelationException();

			if (status / bytesPerProgress > this.status) {
				if (this.status == -1)
					progress.start();
				this.status = status / bytesPerProgress;
				progress.setProgress(this.status);
			}
		}
	}

	public ProgressWriter(Writer out, StatusListener listener) {
		super(out);
		this.listener = listener;
	}

	/**
	 * Using a rotating progress using the given bytesPerProgress tick and a
	 * scale of 100
	 */
	public ProgressWriter(Writer out, IProgress progress, int bytesPerProgress) {
		this(out, new IProgressListener(progress, bytesPerProgress));

	}

	/**
	 * Open a Writer based on the given file
	 */
	public ProgressWriter(File f, IProgress progress, int estimatedSize) throws IOException {
		this(new FileWriter(f), new IProgressListener(progress, estimatedSize / 100));
	}

	@Override
	public void write(int c) throws IOException {
		super.write(c);
		count++;
		listener.setStatus(count);
	}

	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
		super.write(cbuf, off, len);
		count += len;
		listener.setStatus(count);
	}

	@Override
	public void write(String str, int off, int len) throws IOException {
		super.write(str, off, len);
		count += len;
		listener.setStatus(count);
	}
}