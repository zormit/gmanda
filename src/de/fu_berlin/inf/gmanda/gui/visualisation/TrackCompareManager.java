package de.fu_berlin.inf.gmanda.gui.visualisation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.ComparatorUtils;
import org.joda.time.DateTime;

import de.fu_berlin.inf.gmanda.qda.PrimaryDocument;
import de.fu_berlin.inf.gmanda.util.Pair;

public class TrackCompareManager {

	Map<String, TrackComparator> comparators = new HashMap<String, TrackComparator>();

	public TrackCompareManager() {
		comparators.put("n", new NameComparator());
		comparators.put("#", new SizeComparator());
		comparators.put("s", new StartComparator());
		comparators.put("e", new EndComparator());
		comparators.put("m", new MedianComparator());
	}

	public Comparator<Pair<String, ? extends Collection<? extends PrimaryDocument>>> getComparator(
			String s) {
		s = s.toLowerCase();

		Comparator<Pair<String, ? extends Collection<? extends PrimaryDocument>>> result = comparators
				.get("n");

		char[] c = s.toCharArray();

		int i = c.length - 1;

		while (i >= 0) {

			if (comparators.containsKey("" + c[i])) {

				Comparator<Pair<String, ? extends Collection<? extends PrimaryDocument>>> next = comparators
						.get("" + c[i]);

				int j = i - 1;
				while (j >= 0 && !comparators.containsKey("" + c[j])
						&& !(c[j] == '-')) {
					j--;
				}

				if (j >= 0 && c[j] == '-') {
					next = Collections.reverseOrder(next);
					i = j;
				}

				@SuppressWarnings("unchecked")
				Comparator<Pair<String, ? extends Collection<? extends PrimaryDocument>>> chained = ComparatorUtils
						.chainedComparator(next, result);
				result = chained;
			}
			i--;
		}

		return result;
	}

	public abstract class TrackComparator
			implements
			Comparator<Pair<String, ? extends Collection<? extends PrimaryDocument>>> {
	}

	public static final int LEFT_LITTLE = -1;
	public static final int RIGHT_LITTLE = 1;

	public class NameComparator extends TrackComparator {
		@SuppressWarnings("unchecked")
		public int compare(
				Pair<String, ? extends Collection<? extends PrimaryDocument>> o1,
				Pair<String, ? extends Collection<? extends PrimaryDocument>> o2) {
			return ComparatorUtils.naturalComparator().compare(o1.p, o2.p);
		}
	}

	public interface ToLong {
		public long toLong(
				Pair<String, ? extends Collection<? extends PrimaryDocument>> pair);
	}

	public class NumericalComparator extends TrackComparator {

		ToLong conv;

		public NumericalComparator(ToLong conv) {
			this.conv = conv;
		}

		public int compare(
				Pair<String, ? extends Collection<? extends PrimaryDocument>> o1,
				Pair<String, ? extends Collection<? extends PrimaryDocument>> o2) {
			return Long.signum(conv.toLong(o1) - conv.toLong(o2));
		}

	}

	public class SizeComparator extends NumericalComparator {
		public SizeComparator() {
			super(new ToLong() {
				public long toLong(
						Pair<String, ? extends Collection<? extends PrimaryDocument>> pair) {
					return pair.v.size();
				}
			});
		}
	}

	public class StartComparator extends NumericalComparator {
		public StartComparator() {
			super(new ToLong() {
				public long toLong(
						Pair<String, ? extends Collection<? extends PrimaryDocument>> o1) {
					DateTime min = new DateTime(Long.MAX_VALUE);
					for (PrimaryDocument pd : o1.v) {
						DateTime d = pd.getDate();
						if (d != null && d.isBefore(min))
							min = d;
					}
					return min.getMillis();
				}
			});
		}
	}

	public class EndComparator extends NumericalComparator {
		public EndComparator() {
			super(new ToLong() {
				public long toLong(
						Pair<String, ? extends Collection<? extends PrimaryDocument>> o1) {
					DateTime max = new DateTime(0);
					for (PrimaryDocument pd : o1.v) {
						DateTime d = pd.getDate();
						if (d != null && d.isAfter(max))
							max = d;
					}
					return max.getMillis();
				}
			});
		}
	}

	public class MedianComparator extends NumericalComparator {

		public MedianComparator() {
			super(new ToLong() {
				@SuppressWarnings("unchecked")
				Comparator<DateTime> c = ComparatorUtils
						.nullHighComparator(ComparatorUtils.NATURAL_COMPARATOR);

				public long toLong(
						Pair<String, ? extends Collection<? extends PrimaryDocument>> o1) {

					ArrayList<PrimaryDocument> a = new ArrayList<PrimaryDocument>(
							o1.v.size());

					for (PrimaryDocument pd : o1.v) {
						if (pd.getDate() != null)
							a.add(pd);
					}

					if (a.size() == 0)
						return Long.MAX_VALUE;

					Collections.sort(a, new Comparator<PrimaryDocument>() {
						public int compare(PrimaryDocument o1,
								PrimaryDocument o2) {
							return c.compare(o1.getDate(), o2.getDate());
						};
					});

					if ((a.size() & 1) == 0) {
						return (a.get(a.size() / 2 - 1).getDate().getMillis() + (a
								.get(a.size() / 2)).getDate().getMillis()) / 2;
					} else {
						return a.get(a.size() / 2).getDate().getMillis();
					}
				}
			});
		}
	}

}
