package helper;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import static helper.Const.PPM;

public class BodyHelper {

    public static Body createIsometricBody(float x, float y, float width, float height, boolean isStatic, float density, boolean sensor, World world) {
        Body body = defineBody(x, y, isStatic, world);

        Vector2 left = new Vector2(-width / 2 / PPM, 0);
        Vector2 right = new Vector2(width / 2 / PPM, 0);
        Vector2 top = new Vector2(0, height / 2 / PPM);
        Vector2 bottom = new Vector2(0, -height / 2 / PPM);

        Vector2[] vertices = new Vector2[4];
        vertices[0] = left;
        vertices[1] = top;
        vertices[2] = right;
        vertices[3] = bottom;

        PolygonShape shape = new PolygonShape();
        shape.set(vertices);

        defineFixture(shape, body, density, sensor);

        shape.dispose();
        return body;
    }

    public static Body createCircularBody(float x, float y, float radius, boolean isStatic, float density, boolean sensor, World world) {
        Body body = defineBody(x, y, isStatic, world);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(radius / PPM);

        defineFixture(circleShape, body, density, sensor);
        circleShape.dispose();

        return body;
    }

    private static Body defineBody(float x, float y, boolean isStatic, World world) {
        BodyDef def = new BodyDef();
        def.fixedRotation = true;
        def.type = isStatic == false ? BodyDef.BodyType.DynamicBody : BodyDef.BodyType.StaticBody; // movable
        def.position.set(x / PPM, y / PPM);
        def.fixedRotation = true; // --> false -> rotate when hit (could be nice)
        return world.createBody(def);
    }

    private static void defineFixture(Shape shape, Body body, float density, boolean sensor) {
        FixtureDef groundFixture = new FixtureDef();
        groundFixture.shape = shape;
        groundFixture.density=density; // dichte
        groundFixture.restitution = 0;
        groundFixture.friction=0; // reibung
        body.createFixture(groundFixture).setSensor(sensor);
    }
}
