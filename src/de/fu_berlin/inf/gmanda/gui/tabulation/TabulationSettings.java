package de.fu_berlin.inf.gmanda.gui.tabulation;

public class TabulationSettings {

	String xDim;

	String yDim;

	String groupBy;
	
	public TabulationSettings(String filter, String partition, String rank) {
		this.xDim = filter;
		this.yDim = partition;
		this.groupBy = rank;
	}

	public String toString() {
		return String.format("X: %s\nY: %s\nGroup by: %s", xDim, yDim,
			groupBy);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final TabulationSettings other = (TabulationSettings) obj;
		if (xDim == null) {
			if (other.xDim != null)
				return false;
		} else if (!xDim.equals(other.xDim))
			return false;
		if (yDim == null) {
			if (other.yDim != null)
				return false;
		} else if (!yDim.equals(other.yDim))
			return false;
		if (groupBy == null) {
			if (other.groupBy != null)
				return false;
		} else if (!groupBy.equals(other.groupBy))
			return false;
		return true;
	}

}
