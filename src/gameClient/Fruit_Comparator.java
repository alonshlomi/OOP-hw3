package gameClient;

import java.util.Comparator;

public class Fruit_Comparator implements Comparator<Fruit> {

	@Override
	public int compare(Fruit o1, Fruit o2) {
		double ans = o1.getValue() - o2.getValue();
		if (ans < 0) {
			return 1;
		} else if (ans == 0) {
			return 0;
		} else {
			return -1;
		}
	}

}
