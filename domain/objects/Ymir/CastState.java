package domain.objects.Ymir;

public class CastState implements YmirState {
    @Override
    public void handle(YmirContext context) {
        int rand = context.getModel().getRandomOffset();
        context.getView().updateWitchGif(context.getModel().getWitchGif(2 + rand));
        context.getView().updateCoinGif(context.getModel().getCoinGif(2 + rand));
        context.setCurrentState(context.getNextState());
    }

    @Override
    public int getDelay() {
        return 2000; // 2 seconds
    }
}

