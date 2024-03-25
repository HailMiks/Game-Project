package com.mygdx.entropy.Utils;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.entropy.Objects.Enemies.Enemy;
import com.mygdx.entropy.Objects.Player.Player;

public class ContactListen implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if (fa == null || fb == null) return;
        if (fa.getUserData() == null || fb.getUserData() == null) return;

        if ((fa.getUserData() instanceof Player && fb.getUserData() instanceof Enemy) ||
            (fa.getUserData() instanceof Enemy && fb.getUserData() instanceof Player)) {
            
            // Enemy's body
            Fixture enemyFixture = fa.getUserData() instanceof Enemy ? fa : fb;
            Enemy enemy = (Enemy) enemyFixture.getUserData();
            enemy.getBody().setLinearDamping(500f); // Heaviness

            System.out.println("OUCH!");
        }
    }

    @Override
    public void endContact(Contact contact) {
        
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        
    }
}
