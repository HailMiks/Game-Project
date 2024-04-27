package com.mygdx.entropy.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.entropy.Objects.Enemies.Enemy;
import com.mygdx.entropy.Objects.Items.Esuba;
import com.mygdx.entropy.Objects.Items.Needle;
import com.mygdx.entropy.Objects.Items.PictureFrame;
import com.mygdx.entropy.Objects.Items.Threads;
import com.mygdx.entropy.Objects.Items.Button;
import com.mygdx.entropy.Objects.Items.Crayons;
import com.mygdx.entropy.Objects.Items.Crow;
import com.mygdx.entropy.Objects.Player.Player;

public class ContactListen implements ContactListener {

    public boolean esubaInteract = false;
    public boolean pickNeedle = false;
    public boolean pickButton = false;
    public boolean pickCrow = false;
    public boolean pickThreads = false;
    public boolean pickPicture = false;
    public boolean pickCrayons = false;


    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if ((fa.getUserData() instanceof Player && fb.getUserData() instanceof Enemy) ||
            (fa.getUserData() instanceof Enemy && fb.getUserData() instanceof Player)) {
            
            // Enemy's body
            Fixture enemyFixture = fa.getUserData() instanceof Enemy ? fa : fb;
            Enemy enemy = (Enemy) enemyFixture.getUserData();
            enemy.getBody().setLinearDamping(500f); // Heaviness

            System.out.println("OUCH!");
        }

        if ((fa.getUserData() instanceof Player && fb.getUserData() instanceof Esuba) ||
            (fa.getUserData() instanceof Esuba && fb.getUserData() instanceof Player)) {
            
            // Mr Esuba's body
            Fixture itemFixture = fa.getUserData() instanceof Esuba ? fa : fb;
            Esuba esuba = (Esuba) itemFixture.getUserData();
            
            System.out.println("Detected player-item contact");
            System.out.println(fa.getUserData());
            System.out.println(fb.getUserData());

            esubaInteract = true;
            System.out.println(esubaInteract);
        }

        if ((fa.getUserData() instanceof Player && fb.getUserData() instanceof Needle) ||
            (fa.getUserData() instanceof Needle && fb.getUserData() instanceof Player)) {
            
            // Needle Item's body
            Fixture needleFixture = fa.getUserData() instanceof Needle ? fa : fb;
            Needle needle = (Needle) needleFixture.getUserData();
            
            System.out.println("Detected player-needle contact");
            System.out.println(fa.getUserData());
            System.out.println(fb.getUserData());

            pickNeedle = true;
            System.out.println(pickNeedle);
        }

        if ((fa.getUserData() instanceof Player && fb.getUserData() instanceof Button) ||
            (fa.getUserData() instanceof Button && fb.getUserData() instanceof Player)) {
            
            // Button Item's body
            Fixture buttonFixture = fa.getUserData() instanceof Button ? fa : fb;
            Button button = (Button) buttonFixture.getUserData();
            
            System.out.println("Detected player-button contact");
            System.out.println(fa.getUserData());
            System.out.println(fb.getUserData());

            pickButton = true;
            System.out.println(pickButton);
        }

        if ((fa.getUserData() instanceof Player && fb.getUserData() instanceof Crow) ||
            (fa.getUserData() instanceof Crow && fb.getUserData() instanceof Player)) {
            
            // Crow Item's body
            Fixture crowFixture = fa.getUserData() instanceof Crow ? fa : fb;
            Crow crow = (Crow) crowFixture.getUserData();
            
            System.out.println("Detected player-crow contact");
            System.out.println(fa.getUserData());
            System.out.println(fb.getUserData());

            pickCrow = true;
            System.out.println(pickCrow);
        }

        if ((fa.getUserData() instanceof Player && fb.getUserData() instanceof Crayons) ||
            (fa.getUserData() instanceof Crayons && fb.getUserData() instanceof Player)) {
            
            // Crayon Item's body
            Fixture crayonsFixture = fa.getUserData() instanceof Crayons ? fa : fb;
            Crayons crayons = (Crayons) crayonsFixture.getUserData();
            
            System.out.println("Detected player-crayons contact");
            System.out.println(fa.getUserData());
            System.out.println(fb.getUserData());

            pickCrayons = true;
            System.out.println(pickCrayons);
        }

        if ((fa.getUserData() instanceof Player && fb.getUserData() instanceof Threads) ||
            (fa.getUserData() instanceof Threads && fb.getUserData() instanceof Player)) {
            
            // Item's body
            Fixture threadsFixture = fa.getUserData() instanceof Threads ? fa : fb;
            Threads threads = (Threads) threadsFixture.getUserData();
            
            System.out.println("Detected player-button contact");
            System.out.println(fa.getUserData());
            System.out.println(fb.getUserData());

            pickThreads = true;
            System.out.println(pickThreads);
        }

        if ((fa.getUserData() instanceof Player && fb.getUserData() instanceof PictureFrame) ||
            (fa.getUserData() instanceof PictureFrame && fb.getUserData() instanceof Player)) {
            
            // Item's body
            Fixture pictureFixture = fa.getUserData() instanceof PictureFrame ? fa : fb;
            PictureFrame picture = (PictureFrame) pictureFixture.getUserData();
            
            System.out.println("Detected player-picture contact");
            System.out.println(fa.getUserData());
            System.out.println(fb.getUserData());

            pickPicture = true;
            System.out.println(pickPicture);
        }
    }

    @Override
    public void endContact(Contact contact) {
        esubaInteract = false;
        pickNeedle = false;
        pickButton = false;
        pickCrow = false;
        pickThreads = false;
        pickPicture = false;
        pickCrayons = false;
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        
    }
}