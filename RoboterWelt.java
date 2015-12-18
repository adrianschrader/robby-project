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
        setPaintOrder(  Roboter.class, Schraube.class, Akku.class,Wand.class);
        Greenfoot.setSpeed(15);
        
        RobbyTest robbyTest = new RobbyTest(this);
        if (!robbyTest.testAllFeatures()) {
            System.err.println("Die Klasse Robby hat nicht alle Tests bestanden. Bitte überprüfen sie den Log, um das Problem näher einzugrenzen. ");
        } else {
            System.out.println("Die Klasse Robby hat alle Tests bestanden. Bravo!");
        }
   }

    private void sensorTest() {
        System.out.println("Teste Robbys Sensoren auf 'false positives'...");

        // Füge Robby der Spielwelt hinzu
        Robby robby = new Robby();
        this.addObject(robby, 1, 1);

        // Überprüfe auf falsch positive Ergebnisse
        testMethod(robby, "akkuVorne", false);
        testMethod(robby, "akkuRechts", false);
        testMethod(robby, "akkuLinks", false);
        testMethod(robby, "wandHinten", false);

        // Füge die Testhilfsmittel im Unkreis von einem Feld hinzu
        this.addObject(new Akku(), 1, 0);
        this.addObject(new Wand(), 0, 1);
        this.addObject(new Akku(), 1, 2);
        this.addObject(new Akku(), 2, 1);

        System.out.println("Teste Robbys Sensoren auf 'false negatives'...");

        // Auf falsch negative Ergebnisse überprüfen und Testergebis zurückgeben
        testMethod(robby, "akkuVorne", true);
        testMethod(robby, "akkuRechts", true);
        testMethod(robby, "akkuLinks", true);
        testMethod(robby, "akkuHinten", true);
    }

    private boolean testMethod(Object obj, String name, Object returnValue)
    {
        return this.testMethod(obj, name, null, null, returnValue);
    }

    /**
     * Überprüft, ob die reflektiv angegebene Methode den Zielwert zurückgibt.
     * @param obj Die Objektinstanz der Klasse, dessen Methode untersucht werden soll
     * @param name Name der Methode
     * @param paramterTypes Array der Typen der angenommenen Parameter der gesuchten Methode
     * @param parameters Array der für den Test angenommenen Parameter der gesuchten Methode
     * @param returnValue Object, dass dem Rückgabewert entsprechen soll
     * @return Gibt an, ob die Methode den Test bestanden hat
     */
    public boolean testMethod(Object obj, String name, Class<?>[] parameterTypes, Object[] parameters, Object returnValue) {
        Class cl = obj.getClass();

        try {
            Method method = cl.getMethod(name, parameterTypes);
            System.out.print("Methode " + method.getName() + " wird getestet...");

            if (!method.invoke(obj, parameters).equals(returnValue))
            {
                System.err.println("Durchgefallen (!)");
                return false;
            }

            System.out.println("Bestanden");
            return true;
        }
        catch (Exception e)
        {
            System.err.println("Methode in " + cl.getName() + " konnte nicht geladen werden. " + e.getMessage());
            return false;
        }
    }
}
