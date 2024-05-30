package domain.objects.Barrier;

import java.util.ArrayList;

public interface BarrierObserver {
	void onDebrisCreated(ArrayList<Debris> debrisList);
}