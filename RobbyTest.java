import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Diese Klasses testet die Funktionalität von Sensoren, Speicher und
 * Bewegungsapparat der Klasse Robby nach dem Programmstart.
 * @author Adrian Schrader
 * @version 1.0.0
 */
public class RobbyTest extends FeatureTest<Robby>
{
    private RoboterWelt world;

    /**
     * Instanziiert die Klasse in der angegeben Welt.
     */
    public RobbyTest(RoboterWelt world)
    {
        super(Robby.class);
        this.world = world;
    }

    /**
     * Gibt an, ob alle Funktionen von Robby einwandfrei funktionieren.
     */
    public boolean testAllFeatures() {
        System.out.println("Testprogramm für Klasse Robby einleiten...");

        try {
            return this.testSensors() && testMemory() && testMovement();
        } catch (Exception ex) {
            System.err.println("Innerhalb der Testroutine ist ein Fehler aufgetreten. Sind alle Funktionen aufrufbar? (Nachricht: " + ex.getMessage() + ")");
            return false;
        }
    }

    /**
     * Testet Robbys Sensoren zum Aufspüren von Wänden und Akkus in einer Entfernung von einem Feld.
     * @throws Exception
     */
    public boolean testSensors() throws InstantiationException, IllegalAccessException {
        System.out.println("Sensorfunktionalität wird getetstet...");
        this.failed = false;

        // Füge Robby der Spielwelt hinzu
        this.world.addObject(this.object, 1, 1);

        // Überprüfe die Sensorfunktionen für Akkus
        this.testRotationalObjectDetection("Akku-Sensoren", new String[] { "akkuVorne", "akkuLinks", "akkuRechts", "akkuHinten" }, Akku.class); 

        // Überprüfe die Sensorfunktionen für Wände
        this.testRotationalObjectDetection("Wand-Sensoren", new String[] { "wandVorne", "wandLinks", "wandRechts", "wandHinten" }, Wand.class); 

        // Räume die Spielwelt auf
        this.world.removeObject(this.object);

        return !this.failed;
    }

    /**
     * Testet Robbys Fähigkeit Schrauben abzulegen und Akkus aufzunehmen und
     * dabei seine Statusanzeigen zu aktualisieren.
     */
    public boolean testMemory() {
        // Hier sollten die Speicherfeatures abgefragt werden.
        return true;
    }

    /**
     * Testet Robbys Fähigkeit zusammenhängende Hindernisse zu umrunden.
     */
    public boolean testMovement() {
        return true;
    }
    
    private boolean testRotationalObjectDetection(String title, String[] methods, Class<? extends Actor> cl) {
        if (methods.length < 4) {
            System.err.println("Fehlerhaftes Testskript (testRotationalObjectDetection): Benötigt 4 Methodennamen. ");
        }
        try {
            boolean test0 = true;
            this.object.setRotation(0);
            test0 &= this.testObjectDetection(2, 1, methods[0], "0°", cl);
            test0 &= this.testObjectDetection(1, 0, methods[1], "0°", cl);
            test0 &= this.testObjectDetection(1, 2, methods[2], "0°", cl);
            test0 &= this.testObjectDetection(0, 1, methods[3], "0°", cl);
            
            boolean test90 = true;
            this.object.setRotation(90);
            test90 &= this.testObjectDetection(1, 2, methods[0], "90°", cl);
            test90 &= this.testObjectDetection(2, 1, methods[1], "90°", cl);
            test90 &= this.testObjectDetection(0, 1, methods[2], "90°", cl);
            test90 &= this.testObjectDetection(1, 0, methods[3], "90°", cl);
            
            boolean test180 = true;
            this.object.setRotation(180);
            test180 &= this.testObjectDetection(0, 1, methods[0], "180°", cl);
            test180 &= this.testObjectDetection(1, 2, methods[1], "180°", cl);
            test180 &= this.testObjectDetection(1, 0, methods[2], "180°", cl);
            test180 &= this.testObjectDetection(2, 1, methods[3], "180°", cl);
            
            boolean test270 = true;
            this.object.setRotation(270);
            test270 &= this.testObjectDetection(1, 0, methods[0], "270°", cl);
            test270 &= this.testObjectDetection(0, 1, methods[1], "270°", cl);
            test270 &= this.testObjectDetection(2, 1, methods[2], "270°", cl);
            test270 &= this.testObjectDetection(1, 2, methods[3], "270°", cl);
            
            this.sendStatus("Test von " + title + " für 0°", test0);
            this.sendStatus("Test von " + title + " für 90°", test90);
            this.sendStatus("Test von " + title + " für 180°", test180);
            this.sendStatus("Test von " + title + " für 270°", test270);
            
            return ( test0 && test90 && test180 && test270 );
        } catch (Exception ex) {
            System.err.println("Fehlerhaftes Testskript (testRotationalObjectDetection): Kann Methodennamen nicht auflösen (" + ex.getMessage() + ")");
            return false;
        }
    }
    
    /**
     * Gibt an, ob die Methode einen anderen Actor positiv und negativ
     * nachweisen kann. Wirft evtl. Fehler beim Instanziieren des Testobjekts. 
     * @param x Horizontale Koordinate für das Objekt
     * @param y Vertikale Koordinate für das Objekt
     * @param method Name der Methode in der Klasse Robby
     * @param cl Klasse des gesuchten Actors
     * @returns Erfolg des Tests
     * @see FeatureTest#testMethod
     */
    private boolean testObjectDetection(int x, int y, String method, String test, Class<? extends Actor> cl)
            throws InstantiationException, IllegalAccessException {
        // Sicherstellen, dass das Objekt nicht schon in der Spielwelt existiert
        this.world.removeObjects(this.world.getObjectsAt(x, y, cl));

        // Testen, ob die Methode keine false-positives zurückgibt
        boolean negative = this.testMethod(method, false);

        // Platzieren der neuen Objektinstanz in der Welt
        this.world.addObject(cl.newInstance(), x, y);

        // Testen, ob die Methode keine false-negatives zurückgibt
        boolean positive = this.testMethod(method, true);

        // Spielwelt für die nächsten Tests aufräumen
        this.world.removeObjects(this.world.getObjectsAt(x, y, cl));
        
        if (!positive) {
            this.sendStatus(method + "() konnte im Test " + test + " den Actor " + cl.getName() + "nicht positiv erkennen. ", false);
        }
        if (!negative) {
            this.sendStatus(method + "() konnte im Test " + test + " den Actor " + cl.getName() + " nicht negativ erkennen. ", false);
        }

        return (positive && negative);
    }
}
