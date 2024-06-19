package sample.animations;

import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import javafx.scene.Node;

public class Shaker {
    private TranslateTransition translateTransition;

    public Shaker(Node node) {
        translateTransition = new TranslateTransition(Duration.millis(50),node);
        translateTransition.setFromX(0f);
        translateTransition.setByX(10f);
        translateTransition.setCycleCount(2);
        translateTransition.setAutoReverse(true);

    }

    public void shake(){
        translateTransition.playFromStart();
    }
}
