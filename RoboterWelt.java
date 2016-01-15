import java.util.*;
import java.lang.Boolean;
import java.lang.reflect.*;
import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Die einzigen aktiven Akteure in der Roboterwelt sind die Roboter.
 * Die Welt besteht aus 14 * 10 Feldern.
 */

public class RoboterWelt extends World
{
    private static int zellenGroesse = 50;

    /**
     * Erschaffe eine Welt mit 14 * 10 Zellen.
     */
    public RoboterWelt()
    {
        super(14, 10, zellenGroesse);
        setBackground("images/Bodenplatte.png");
        setPaintOrder(Roboter.class, Schraube.class, Akku.class, Wand.class);
        Greenfoot.setSpeed(15);

        RobbyTest robbyTest = new RobbyTest(this);
        if (!robbyTest.testAllFeatures()) {
            System.err.println("Die Klasse Robby hat nicht alle Tests bestanden. Bitte überprüfen sie den Log, um das Problem näher einzugrenzen. ");
        } else {
            System.out.println("Die Klasse Robby hat alle Tests bestanden. Bravo!");
        }

        prepare();
    }

    /**
     * Prepare the world for the start of the program. That is: create the initial
     * objects and add them to the world.
     */
    private void prepare()
    {
        Wand wand = new Wand();
        addObject(wand, 6, 4);
        Wand wand2 = new Wand();
        addObject(wand2, 7, 5);
        Wand wand3 = new Wand();
        addObject(wand3, 6, 6);
        Robby robby = new Robby();
        addObject(robby, 5, 4);
    }
}
