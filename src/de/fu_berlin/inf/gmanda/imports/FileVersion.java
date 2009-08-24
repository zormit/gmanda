package de.fu_berlin.inf.gmanda.imports;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Writer;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.core.BaseException;

import de.fu_berlin.inf.gmanda.exceptions.DoNotShowToUserException;
import de.fu_berlin.inf.gmanda.exceptions.ReportToUserException;
import de.fu_berlin.inf.gmanda.exceptions.UserCancelationException;
import de.fu_berlin.inf.gmanda.qda.PrimaryDocumentData;
import de.fu_berlin.inf.gmanda.qda.PrimaryDocumentDataV1;
import de.fu_berlin.inf.gmanda.qda.ProjectData;
import de.fu_berlin.inf.gmanda.qda.ProjectDataV1;
import de.fu_berlin.inf.gmanda.qda.ProjectDataV2;
import de.fu_berlin.inf.gmanda.util.progress.IProgress;
import de.fu_berlin.inf.gmanda.util.progress.ProgressInputStream;

public enum FileVersion {

	V1 {
		Class<?> projectData = ProjectDataV1.class;
		Class<?> pdData = PrimaryDocumentDataV1.class;
		
		@Override
		public ProjectData load(File f, IProgress p) {

			try {
				Thread.sleep(150);
			} catch (InterruptedException e2) {
			}

			switch (JOptionPane.showConfirmDialog(null,
				"Upgrading from Fileformat Version 1. Do you want me to create a back up copy of\n"
					+ f.getAbsolutePath() + "?")) {

			case JOptionPane.YES_OPTION:
				JFileChooser fc = new JFileChooser(f.getParentFile());
				int returnVal = fc.showSaveDialog(null);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File dest;
					if (null != (dest = fc.getSelectedFile())) {
						try {
							FileUtils.copyFile(f, dest);
						} catch (IOException e) {
							throw new ReportToUserException(e);
						}
					}
				} else {
					throw new DoNotShowToUserException("Abort upgrade");
				}
				break;
			case JOptionPane.CANCEL_OPTION:
				throw new DoNotShowToUserException("Abort upgrade");
			}

			XStream x = new XStream();

			x.alias("project", projectData);
			x.alias("document", pdData);

			x.addImplicitCollection(projectData, "children");
			x.addImplicitCollection(pdData, "children");

			x.useAttributeFor(pdData, "id");
			x.useAttributeFor(pdData, "filename");

			x.registerConverter(new MyPropertiesConverter());

			try {
				return FileVersionConvert.fromV1((ProjectDataV1) x.fromXML(new ProgressInputStream(f, p)));
			} catch (FileNotFoundException e1) {
				throw new RuntimeException(e1);
			}
		}

		@SuppressWarnings("all")
		public void save(ProjectData project, Writer out) {

			/*
			 * This code is here only for legacy purposes, so that you can see
			 * how it worked
			 */
			if (true)
				throw new RuntimeException("Deprecated");

			XStream x = new GmpXStream();
			x.alias("project", projectData);
			x.alias("document", pdData);

			x.addImplicitCollection(projectData, "children");
			x.addImplicitCollection(pdData, "children");

			x.useAttributeFor(pdData, "id");
			x.useAttributeFor(pdData, "filename");

			x.registerConverter(new MyPropertiesConverter());

			x.toXML(project, out);
		}
	}, V2 {
		Class<?> projectData = ProjectDataV2.class;
		Class<?> pdData = PrimaryDocumentDataV1.class;
		
		@Override
		public ProjectData load(File f, IProgress p) {

			ProgressInputStream in;
			try {
				in = new ProgressInputStream(f, p);
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			}

			XStream x = new GmpXStream();

			x.alias("project", projectData);
			x.alias("document", pdData);

			x.addImplicitCollection(projectData, "children");
			x.addImplicitCollection(pdData, "children");

			x.useAttributeFor(pdData, "id");
			x.useAttributeFor(pdData, "filename");

			x.registerConverter(new MyPropertiesConverter());

			try {
				ObjectInputStream o = x.createObjectInputStream(new InputStreamReader(in));

				FileVersion version = getVersionInfo(o);

				if (version != this) {
					throw new RuntimeException("Wrong version string found.");
				}

				return FileVersionConvert.fromV2((ProjectDataV2) o.readObject());
			} catch (ConversionException e){
				if (e.getCause() instanceof UserCancelationException)
					throw (UserCancelationException)e.getCause();
				else
					throw e;
			} catch (Exception e) {
				throw new ReportToUserException(e);
			}
		}

		@SuppressWarnings("all")
		@Deprecated
		public void save(ProjectData project, Writer out) {
			
			/*
			 * This code is here only for legacy purposes, so that you can see
			 * how it worked
			 */
			if (true)
				throw new RuntimeException("Deprecated");
			
			XStream x = new GmpXStream();
			x.alias("project", projectData);
			x.alias("document", pdData);

			x.addImplicitCollection(projectData, "children");
			x.addImplicitCollection(pdData, "children");

			x.useAttributeFor(pdData, "id");
			x.useAttributeFor(pdData, "filename");

			x.registerConverter(new MyPropertiesConverter());

			try {
				ObjectOutputStream o = x.createObjectOutputStream(out, "gmanda-project");
				o.writeObject(this.toVersion());
				o.writeObject(project);
				o.close();
			} catch (IOException e) {
				// do nothing
			}
		}
	}, V3 {
		
		@Override
		public ProjectData load(File f, IProgress p) {

			ProgressInputStream in;
			try {
				in = new ProgressInputStream(f, p);
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			}

			XStream x = new GmpXStream();

			x.alias("project", ProjectData.class);
			x.alias("document", PrimaryDocumentData.class);

			x.addImplicitCollection(ProjectData.class, "rootDocuments");
			x.addImplicitCollection(PrimaryDocumentData.class, "children");

			x.useAttributeFor(PrimaryDocumentData.class, "filename");

			x.registerConverter(new MyPropertiesConverter());

			try {
				ObjectInputStream o = x.createObjectInputStream(new InputStreamReader(in));

				FileVersion version = getVersionInfo(o);

				if (version != this) {
					throw new RuntimeException("Wrong version string found.");
				}

				return FileVersionConvert.fromV3((ProjectData) o.readObject());
			} catch (ConversionException e){
				if (e.getCause() instanceof UserCancelationException)
					throw (UserCancelationException)e.getCause();
				else
					throw e;
			} catch (Exception e) {
				throw new ReportToUserException(e);
			}
		}

		@SuppressWarnings("all")
		@Deprecated
		public void save(ProjectData project, Writer out) {
			
			/*
			 * This code is here only for legacy purposes, so that you can see
			 * how it worked
			 */
			if (true)
				throw new RuntimeException("Deprecated");
			
			XStream x = new GmpXStream();
			x.alias("project", ProjectData.class);
			x.alias("document", PrimaryDocumentData.class);

			x.addImplicitCollection(ProjectData.class, "rootDocuments");
			x.addImplicitCollection(PrimaryDocumentData.class, "children");

			x.useAttributeFor(PrimaryDocumentData.class, "filename");

			x.registerConverter(new MyPropertiesConverter());

			try {
				ObjectOutputStream o = x.createObjectOutputStream(out, "gmanda-project");
				o.writeObject(this.toVersion());
				o.writeObject(project);
				o.close();
			} catch (IOException e) {
				// do nothing
			}
		}
	}, V4 {

		@Override
		public ProjectData load(File f, IProgress p) {

			ProgressInputStream in;
			try {
				in = new ProgressInputStream(f, p);
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			}

			XStream x = new GmpXStream();

			x.alias("project", ProjectData.class);
			x.alias("document", PrimaryDocumentData.class);

			x.addImplicitCollection(ProjectData.class, "rootDocuments");
			x.addImplicitCollection(PrimaryDocumentData.class, "children");

			x.useAttributeFor(PrimaryDocumentData.class, "filename");

			x.registerConverter(new MyPropertiesConverter());

			try {
				ObjectInputStream o = x.createObjectInputStream(new InputStreamReader(in));

				FileVersion version = getVersionInfo(o);

				if (version != this) {
					throw new RuntimeException("Wrong version string found.");
				}

				return (ProjectData) o.readObject();
			} catch (ConversionException e){
				if (e.getCause() instanceof UserCancelationException)
					throw (UserCancelationException)e.getCause();
				else
					throw e;
			} catch (Exception e) {
				throw new ReportToUserException(e);
			}
		}

		public void save(ProjectData project, Writer out) {
			XStream x = new GmpXStream();
			x.alias("project", ProjectData.class);
			x.alias("document", PrimaryDocumentData.class);

			x.addImplicitCollection(ProjectData.class, "rootDocuments");
			x.addImplicitCollection(PrimaryDocumentData.class, "children");

			x.useAttributeFor(PrimaryDocumentData.class, "filename");

			x.registerConverter(new MyPropertiesConverter());

			try {
				ObjectOutputStream o = x.createObjectOutputStream(out, "gmanda-project");
				o.writeObject(this.toVersion());
				o.writeObject(project);
				o.close();
			} catch (IOException e) {
				// do nothing
			}
		}
		
	};

	public VersionString toVersion() {
		return new VersionString(this.name());
	}

	public static FileVersion fromVersionString(VersionString versionString) {

		try {
			return FileVersion.valueOf(versionString.version);
		} catch (Exception e) {
			throw new ReportToUserException("Unsupported file version '"
				+ versionString.version + "'\nPlease update to a newer version of GmanDA.");
		}
	}

	public abstract ProjectData load(File f, IProgress p);

	public abstract void save(ProjectData project, Writer out);

	public static FileVersion getVersionInfo(ObjectInputStream s) {
		FileVersion version;
		try {
			version = FileVersion.fromVersionString((VersionString) s.readObject());
		} catch (ConversionException e){
			if (e.getCause() instanceof UserCancelationException)
				throw (UserCancelationException)e.getCause();
			else
				version = FileVersion.V1;
		} catch (IOException e) {
			version = FileVersion.V1;
		} catch (ClassNotFoundException e) {
			version = FileVersion.V1;
		} catch (BaseException e) {
			version = FileVersion.V1;
		}
		return version;
	}

	public static class GmpXStream extends XStream {
		public GmpXStream() {
			alias("metadata", VersionString.class);
		}
	}

}
