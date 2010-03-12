package de.fu_berlin.inf.gmanda.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import javax.swing.AbstractAction;
import javax.swing.Action;

import de.fu_berlin.inf.gmanda.gui.misc.ConfigurationAwareFileChooser;
import de.fu_berlin.inf.gmanda.gui.misc.ExtensionDescriptor;
import de.fu_berlin.inf.gmanda.proxies.ProjectProxy;
import de.fu_berlin.inf.gmanda.qda.PrimaryDocument;
import de.fu_berlin.inf.gmanda.qda.Project;
import de.fu_berlin.inf.gmanda.util.Configuration;
import de.fu_berlin.inf.gmanda.util.VariableProxyListener;

/**
 * Used for creating a file that can be read by the Text Information Extractor (TIE).
 * 
 * Invoke tie with the created file as follows:
 * 
 * java -Xmx512m -jar ties-1.0-dep.jar class-train test.dsv
 * 
 * http://www.inf.fu-berlin.de/inst/ag-db/software/tie
 */
public class CreateDSVFileAction extends AbstractAction {

	ProjectProxy projectProxy;

	Configuration configuration;
	
	ConfigurationAwareFileChooser<ExtensionDescriptor> chooser;

	public CreateDSVFileAction(Configuration configuration, ProjectProxy projectProxy) {
		super("Create DSV File...");

		this.projectProxy = projectProxy;
		this.configuration = configuration;

		putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_D));

		projectProxy.addAndNotify(new VariableProxyListener<Project>() {
			public void setVariable(Project newValue) {
				setEnabled(newValue != null);
			}
		});
	}

	public void actionPerformed(ActionEvent arg0) {

		if (projectProxy.getVariable() == null)
			return;

		Project project = projectProxy.getVariable();

		if (chooser == null)
			chooser = new ConfigurationAwareFileChooser<ExtensionDescriptor>(configuration, "DSVLocation", ".dsv", "Delimiter separated value file *.dsv");
		
		File target = chooser.getSaveFile();
		
		if (target == null)
			return;
		
		PrintWriter pw = null;
		try {
			try {
				pw = new PrintWriter(target);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return;
			}

			pw.write("File|Class\n");

			for (PrimaryDocument p : PrimaryDocument.getTreeWalker(project.getPrimaryDocuments())) {

				if (p.getFilename() == null || !new File(p.getFilename()).exists())
					continue;

				StringBuilder sb = new StringBuilder();
				sb.append(p.getFilename()).append("|");

				if (p.getCodeAsString() != null) {
					if (p.getCodeAsString().contains("seen"))
						sb.append("notInteresting");
					else
						if (p.getCodeAsString().contains("spam"))
							sb.append("spam");
						else
							if (p.getCodeAsString().contains("offtopic"))
								sb.append("offtopic");
							else
								sb.append("interesting");
				}
				sb.append('\n');
				pw.write(sb.toString());
			}

		} finally {
			if (pw != null)
				pw.close();
		}
	}
}
