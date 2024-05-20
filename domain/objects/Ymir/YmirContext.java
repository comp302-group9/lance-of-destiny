package domain.objects.Ymir;

public class YmirContext {
    private YmirState currentState;
    private YmirModel model;
    private YmirView view;
    private YmirState[] states;

    public YmirContext(YmirModel model, YmirView view) {
        this.model = model;
        this.view = view;
        states = new YmirState[]{new WaitState(), new WandState(), new CastState()};
        currentState = states[0]; // Initial state
    }

    public void setCurrentState(YmirState state) {
        this.currentState = state;
    }

    public YmirState getNextState() {
        int currentIndex = java.util.Arrays.asList(states).indexOf(currentState);
        return states[(currentIndex + 1) % states.length];
    }

    public YmirModel getModel() {
        return model;
    }

    public YmirView getView() {
        return view;
    }

    public void request() {
        currentState.handle(this);
    }

    public int getCurrentStateDelay() {
        return currentState.getDelay();
    }
}
