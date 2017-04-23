package uk.co.rossbinden.kindergarten.component;

import com.badlogic.ashley.core.ComponentMapper;

public final class ComponentMappers {

    public static final ComponentMapper<BodyComponent> BODY = ComponentMapper.getFor(BodyComponent.class);
    public static final ComponentMapper<TextureComponent> TEXTURE = ComponentMapper.getFor(TextureComponent.class);

}
