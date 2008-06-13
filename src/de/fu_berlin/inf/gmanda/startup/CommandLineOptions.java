package de.fu_berlin.inf.gmanda.startup;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import de.fu_berlin.inf.gmanda.exceptions.ReportToUserException;

public class CommandLineOptions {

	@Option(name = "-h", usage = "show this help and exit")
	public boolean help = false;

	@Option(name = "--nt", usage = "do not keep a trail log of edits", 
		aliases = { "-noTrail", "--notrail" })
	public boolean noTrail = false;

	// receives other command line parameters than options
	@Argument
	protected List<String> arguments = new ArrayList<String>();

	public String fileToOpen = null;

	public String getUsage() {
		StringBuilder sb = new StringBuilder();
		sb.append("gmanda [options...] [file to open]\n\n");

		StringWriter stringWriter = new StringWriter();
		parser.printUsage(stringWriter, null);
		sb.append(stringWriter.toString()).append('\n');
		return sb.toString();
	}

	CmdLineParser parser = new CmdLineParser(this);

	public void parse(String[] args) {

		try {
			parser.parseArgument(args);
		} catch (CmdLineException e) {

			StringBuilder sb = new StringBuilder();
			sb.append(e.getMessage()).append('\n').append('\n');
			sb.append(getUsage());

			throw new ReportToUserException(sb.toString());
		}

		if (help) {
			throw new ReportToUserException(getUsage());
		}

		if (arguments.size() > 1)
			System.out
				.println("Warning: Only one argument can be given. The following arguments are ignored:");

		for (String arg : arguments) {
			if (fileToOpen == null) {
				fileToOpen = arg;
			} else {
				System.out.print("  " + arg);
			}
		}

	}
}
