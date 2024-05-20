package domain.objects.Ymir;

public class WaitState implements YmirState {
    @Override
    public void handle(YmirContext context) {
        context.getView().updateWitchGif(context.getModel().getWitchGif(0));
        context.getView().updateCoinGif(context.getModel().getCoinGif(0));
        context.setCurrentState(context.getNextState());
    }

    @Override
    public int getDelay() {
        return 10000; // 25 seconds
    }
}

