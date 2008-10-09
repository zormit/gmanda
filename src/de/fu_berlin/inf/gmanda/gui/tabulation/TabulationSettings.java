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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		final TabulationSettings other = (TabulationSettings) obj;
		
		if (this.noIntersectX != other.noIntersectX || this.noIntersectY != other.noIntersectY)
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
		if (groupBy == null) {
			if (other.groupBy != null)
				return false;
		} else if (!groupBy.equals(other.groupBy))
			return false;

		if (filterBy == null) {
			if (other.filterBy != null)
				return false;
		} else if (!filterBy.equals(other.filterBy))
			return false;

		return true;
	}

}
