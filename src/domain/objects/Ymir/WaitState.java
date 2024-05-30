package domain.objects.Ymir;

public class WaitState implements YmirState {
    @Override
    public void handle(YmirContext context) {
        context.getView().updateWitchGif(context.getModel().getWitchGif(0));
        context.getView().updateCoinGif(context.getModel().getCoinGif(0));
        context.setCurrentState(context.getNextState());
        if (context.getModel().getFlag()==0){
            context.getModel().castSpell();
            context.getModel().setFlag(1);
        }
    }

    @Override
    public int getDelay() {
        return 1500; // 25 secondss
    }
}

