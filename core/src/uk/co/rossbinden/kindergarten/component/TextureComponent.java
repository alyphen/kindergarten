package uk.co.rossbinden.kindergarten.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureComponent implements Component {

    private TextureRegion texture;

    public TextureComponent(TextureRegion texture) {
        this.texture = texture;
    }

    public TextureRegion getTexture() {
        return texture;
    }
}
