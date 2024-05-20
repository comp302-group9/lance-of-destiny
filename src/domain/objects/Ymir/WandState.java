package domain.objects.Ymir;

public class WandState implements YmirState {
    @Override
    public void handle(YmirContext context) {
        context.getView().updateWitchGif(context.getModel().getWitchGif(1));
        context.getView().updateCoinGif(context.getModel().getCoinGif(1));
        context.setCurrentState(context.getNextState());
    }

    @Override
    public int getDelay() {
        return 2400; // 3 seconds
    }
}
