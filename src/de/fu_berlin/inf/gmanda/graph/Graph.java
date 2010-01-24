package de.fu_berlin.inf.gmanda.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.colorbrewer.ColorBrewerPalettes;
import org.joda.time.DateTime;

import de.fu_berlin.inf.gmanda.gui.graph.ExportSettings;
import de.fu_berlin.inf.gmanda.qda.PrimaryDocument;

public class Graph {

	protected ExportSettings settings;

	public Graph(ExportSettings settings) {
		this.settings = settings;
	}

	public int nextId = 0;

	public HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();

	public class IntegerHistogram {

		protected int[] bins;

		protected int min;
		protected int max;

		protected int underflows = 0;
		protected int overflows = 0;

		public IntegerHistogram(int from, int to) {
			if (to < from)
				throw new IllegalArgumentException();

			this.min = from;
			this.max = to;

			bins = new int[getNumberOfBins()];
		}

		public int getNumberOfBins() {
			return max - min + 1;
		}

		public int getNonZeroBins() {
			int result = 0;
			for (int bin : bins) {
				if (bin > 0) {
					result++;
				}
			}
			return result;
		}

		public void add(int bin) {
			if (bin < min) {
				underflows++;
				return;
			}
			if (max < bin) {
				overflows++;
				return;
			}
			bins[bin - min]++;
		}
	}

	/**
	 * A cluster is a set of authors that we are grouped together.
	 * 
	 * Any author can only be part of at most one cluster
	 */
	public class Cluster {

		protected String name;

		protected String color;

		public String getName() {
			return name;
		}

		public String toString() {
			return name;
		}

		protected Set<Node> authors = new HashSet<Node>();

		public Cluster(String name, String color) {
			this.name = name;
			this.color = color;
		}

		public String getColor() {
			return color;
		}

		public Set<Node> getAuthors() {
			return authors;
		}

		public int getThreadsStarted() {
			int result = 0;
			for (Node a : getAuthors()) {
				result += a.threadsStarted;
			}
			return result;
		}

		public void add(Node a) {
			if (a.cluster == this)
				return;

			if (a.cluster != null)
				throw new IllegalArgumentException(
						"Only one cluster per node allowed");

			authors.add(a);
			a.cluster = this;
		}

	}

	public class Node implements Comparable<Node> {
		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Node other = (Node) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}

		public int emailsWritten;
		public String name;
		public int id;

		public boolean project = false;

		public Cluster cluster;

		public IntegerHistogram weeks = new IntegerHistogram(1, 52);
		public IntegerHistogram months = new IntegerHistogram(1, 12);

		public int threadsStarted = 0;

		/**
		 * Contains all variations of the name of this author
		 */
		public Set<String> names = new HashSet<String>();

		public Node(String name) {
			id = nextId++;
			Graph.this.nodes.put(id, this);

			this.setName(name);
			names.add(name);
		}

		public void setEmailsWritten(int emailsWritten) {
			this.emailsWritten = emailsWritten;
		}

		public int getEmailsWritten() {
			return emailsWritten;
		}

		public int getNumberOfActiveMonths() {
			return months.getNonZeroBins();
		}

		public int getNumberOfActiveWeeks() {
			return weeks.getNonZeroBins();
		}

		public String getMultiLineName() {
			return getName().replaceAll("\\s+", "\\\\n");
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public int getId() {
			return id;
		}

		public String getNameNoSpace() {
			return name.replaceAll("\\s+", "_");
		}

		public double getWeight() {
			return Math.sqrt(emailsWritten) / 2.0;
		}

		public String getColor(String[] palette) {
			switch (settings.getColoration()) {
			case MONTH:
				return ColorBrewerPalettes.getColorFromInt(palette, months
						.getNonZeroBins(), 12);
			case WEEK:
				return ColorBrewerPalettes.getColorFromInt(palette, weeks
						.getNonZeroBins(), 52);
			case FIXED:
			default:
				return palette[palette.length - 1];// "#0000ff";
			}
		}

		public String getFontColor() {
			return getColor(settings.getFontColorPalette());
		}

		public String getColor() {
			return getColor(settings.getColorPalette());
		}

		@Override
		public int compareTo(Node arg0) {
			return this.emailsWritten - arg0.emailsWritten;
		}

		public void add(PrimaryDocument pd) {
			emailsWritten++;

			DateTime date = pd.getDate();
			int month;
			int week;
			if (date == null) {
				month = 0;
				week = 0;
			} else {
				month = date.getMonthOfYear();
				week = date.getWeekOfWeekyear();
			}
			months.add(month);
			weeks.add(week);
		}

		private Graph getOuterType() {
			return Graph.this;
		}

		public String toString() {
			return name;
		}

		public Cluster getCluster() {
			return cluster;
		}

		public void addName(String authorsName) {
			if (!names.contains(authorsName)) {
				System.out.println("New Name for Author '" + name + "': "
						+ authorsName);
				names.add(authorsName);
			}
		}
	}

	public class Edge {
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((from == null) ? 0 : from.hashCode());
			result = prime * result + ((to == null) ? 0 : to.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof Edge))
				return false;
			Edge other = (Edge) obj;
			if (from == null) {
				if (other.from != null)
					return false;
			} else if (!from.equals(other.from))
				return false;
			if (to == null) {
				if (other.to != null)
					return false;
			} else if (!to.equals(other.to))
				return false;
			return true;
		}

		/**
		 * Creates a directed edge from the given node "from" to the given node
		 * "to"
		 */
		public Edge(Node from, Node to, int size) {
			this.from = from;
			this.to = to;
			this.weight = size;
		}

		public Node from;

		public Node to;

		public int weight;

		public String getAuthor() {
			return from.getName();
		}

		public String getReply() {
			return to.getName();
		}

		public Node getFrom() {
			return from;
		}

		public Node getTo() {
			return to;
		}

		public Node getOtherEnd(Node author) {
			if (this.from.equals(author)) {
				return to;
			}
			if (this.to.equals(author)) {
				return this.from;
			}
			return null;
		}

		public String getFromID() {
			if ("cluster0".equals(from.name)) {
				return "cluster0";
			} else {
				return "" + from.id;
			}
		}

		public String getToID() {
			if ("cluster0".equals(to.name)) {
				return "cluster0";
			} else {
				return "" + to.id;
			}
		}

		public String getAuthorNoSpace() {
			return getAuthor().replaceAll("\\s+", "_");
		}

		public String getReplyNoSpace() {
			return getReply().replaceAll("\\s+", "_");
		}

		public int getEmailsWritten() {
			return weight;
		}

		public int getWeight() {
			return weight;
		}
	}

}
