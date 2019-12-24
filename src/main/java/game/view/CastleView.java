package game.view;

import game.logic.Castle;
import game.logic.NeutralDukes;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class CastleView {

    private Color col;
    private Rectangle representation;
    private Castle c;

    public CastleView(Castle c) {
        this.c = c;
        representation = new Rectangle(c.getBoundingRect().getMinX(),
                c.getBoundingRect().getMinY(),
                c.getBoundingRect().getWidth(),
                c.getBoundingRect().getHeight());

        representation.setFill(c.getOwner() instanceof NeutralDukes ? Color.GREY : Color.RED);
    }

    public Rectangle getRepresentation() {
        return representation;
    }
}
