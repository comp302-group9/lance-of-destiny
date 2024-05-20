package domain.objects.Ymir;

public interface YmirState {
    void handle(YmirContext context);
    int getDelay();
}
