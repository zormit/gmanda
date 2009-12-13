package de.fu_berlin.inf.gmanda.graph;

import org.joda.time.DateTime;

import de.fu_berlin.inf.gmanda.qda.PrimaryDocument;

public class Graph {

	public enum Coloration {
		MONTH, WEEK, FIXED;
	}

	public int nextId = 0;

	public Coloration coloration = Coloration.MONTH;

	public class IntegerHistogram {

		public int[] bins;

		public IntegerHistogram(int numberOfBins) {
			bins = new int[numberOfBins];
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
			bins[bin]++;
		}
	}

	public class Author implements Comparable<Author> {
		/* (non-Javadoc)
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

		/* (non-Javadoc)
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
			Author other = (Author) obj;
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
		public IntegerHistogram weeks = new IntegerHistogram(53);
		public IntegerHistogram months = new IntegerHistogram(13);

		public Author(String name) {
			id = nextId++;
			this.setName(name);
		}

		public void setEmailsWritten(int emailsWritten) {
			this.emailsWritten = emailsWritten;
		}

		public int getEmailsWritten() {
			return emailsWritten;
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

		public String getColorFromInt(int i, int max) {

			int color = i / (max / 4);

			switch (color) {
			case 0:
				return "#ffffb2";
			case 1:
				return "#fecc5c";
			case 2:
				return "#fd8c3c";
			case 3:
			default:
				return "#e31a1c";
			}
		}

		public String getColor() {
			switch (coloration) {
			case MONTH:
				return getColorFromInt(months.getNonZeroBins(), 12);
			case WEEK:
				return getColorFromInt(weeks.getNonZeroBins(), 52);
			case FIXED:
			default:
				return "#0000ff";
			}
		}

		@Override
		public int compareTo(Author arg0) {
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
		
		public String toString(){
			return name;
		}
	}

	public class Edge {
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((author == null) ? 0 : author.hashCode());
			result = prime * result + ((reply == null) ? 0 : reply.hashCode());
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
			if (author == null) {
				if (other.author != null)
					return false;
			} else if (!author.equals(other.author))
				return false;
			if (reply == null) {
				if (other.reply != null)
					return false;
			} else if (!reply.equals(other.reply))
				return false;
			return true;
		}

		public Edge(Author author, Author reply, int size) {
			this.author = author;
			this.reply = reply;
			this.weight = size;
		}

		public Author author;

		public Author reply;

		public int weight;

		public String getAuthor() {
			return author.getName();
		}
		
		public Author getFrom(){
			return author;
		}
		
		public Author getTo(){
			return reply;
		}
		
		public String getFromID(){
			if ("cluster0".equals(author.name)){
				return "cluster0"; 
			} else {
				return "" + author.id;
			}
		}
		
		public String getToID(){
			if ("cluster0".equals(reply.name)){
				return "cluster0"; 
			} else {
				return "" + reply.id;
			}
		}

		public String getAuthorNoSpace() {
			return getAuthor().replaceAll("\\s+", "_");
		}

		public String getReplyNoSpace() {
			return getReply().replaceAll("\\s+", "_");
		}

		public String getReply() {
			return reply.getName();
		}

		public int getWeight() {
			return weight;
		}
	}

}
