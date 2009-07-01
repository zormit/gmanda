package de.fu_berlin.inf.gmanda.gui.visualisation;

public class VisualizationSetting {

	String filter;

	String partition;

	String rank;

	String color;

	// episode.git@kvm; milestone; s; propose.*\=green, announce.*\=yellow, deadend.*\=red
	
	public VisualizationSetting(String filter, String partition, String rank, String color) {
		this.filter = filter;
		this.partition = partition;
		this.rank = rank;
		this.color = color;
	}

	public String toString() {
		return String.format("Filter: %s\nPartition: %s\nRank: %s\nColor: %s", filter, partition,
			rank, color);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + ((filter == null) ? 0 : filter.hashCode());
		result = prime * result
				+ ((partition == null) ? 0 : partition.hashCode());
		result = prime * result + ((rank == null) ? 0 : rank.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VisualizationSetting other = (VisualizationSetting) obj;
		if (color == null) {
			if (other.color != null)
				return false;
		} else if (!color.equals(other.color))
			return false;
		if (filter == null) {
			if (other.filter != null)
				return false;
		} else if (!filter.equals(other.filter))
			return false;
		if (partition == null) {
			if (other.partition != null)
				return false;
		} else if (!partition.equals(other.partition))
			return false;
		if (rank == null) {
			if (other.rank != null)
				return false;
		} else if (!rank.equals(other.rank))
			return false;
		return true;
	}

}
