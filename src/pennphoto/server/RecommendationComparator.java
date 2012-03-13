package pennphoto.server;

import java.util.Comparator;

public class RecommendationComparator implements Comparator<Recommendation> {

	@Override
	public int compare(Recommendation arg0, Recommendation arg1) {
		if (arg0.getScore() > arg1.getScore()) {
			return -1;
		} else if (arg0.getScore() < arg1.getScore()) {
			return 1;
		} else {
			return 0;
		}
	}

}
