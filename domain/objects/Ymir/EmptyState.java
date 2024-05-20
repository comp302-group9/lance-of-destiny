package domain.objects.Ymir;

public class EmptyState implements YmirState {
    @Override
    public void handle(YmirContext context) {
        context.setCurrentState(new WaitState()); // Transition to WaitState
        context.request(); // Immediately handle the next state
    }

    @Override
    public int getDelay() {
        return 0; // No delay for the EmptyState
    }
}

