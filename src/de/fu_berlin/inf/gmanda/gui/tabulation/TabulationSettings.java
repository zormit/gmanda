package de.fu_berlin.inf.gmanda.gui.tabulation;

public class TabulationSettings {

	String xDim;

	String yDim;

	String groupBy;

	String filterBy;
	
	boolean noIntersectX;
	
	boolean noIntersectY;

	public TabulationSettings(String xDim, String yDim, String groupBy, String filterBy, 
		boolean noIntersectX, boolean noIntersectY) {
		this.xDim = xDim;
		this.yDim = yDim;
		this.groupBy = groupBy;
		this.filterBy = filterBy;
		this.noIntersectX = noIntersectX;
		this.noIntersectY = noIntersectY;
	}

	public String toString() {
		return String.format("X: %s     Y: %s     Group by: %s    Filter: %s    Show no intersect: %b %b", xDim, yDim, groupBy,
			filterBy, noIntersectX, noIntersectY);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((filterBy == null) ? 0 : filterBy.hashCode());
		result = prime * result + ((groupBy == null) ? 0 : groupBy.hashCode());
		result = prime * result + (noIntersectX ? 1231 : 1237);
		result = prime * result + (noIntersectY ? 1231 : 1237);
		result = prime * result + ((xDim == null) ? 0 : xDim.hashCode());
		result = prime * result + ((yDim == null) ? 0 : yDim.hashCode());
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
		TabulationSettings other = (TabulationSettings) obj;
		
		if (noIntersectX != other.noIntersectX)
			return false;
		if (noIntersectY != other.noIntersectY)
			return false;
		
		if (filterBy == null) {
			if (other.filterBy != null)
				return false;
		} else if (!filterBy.equals(other.filterBy))
			return false;
		if (groupBy == null) {
			if (other.groupBy != null)
				return false;
		} else if (!groupBy.equals(other.groupBy))
			return false;

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
		return true;
	}

}
