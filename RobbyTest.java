import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Diese Klasses testet die Funktionalität von Sensoren, Speicher und
 * Bewegungsapparat der Klasse Robby nach dem Programmstart.
 * @author Adrian Schrader
 * @version 1.5
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
    @Override
    public boolean testAllFeatures() {
        System.out.println("Testprogramm für Klasse Robby einleiten...");

        try {
            this.world.addObject(this.object, 0, 0);

            boolean success = this.testSensors() && this.testMemory() && testMovement();

            this.world.removeObject(this.object);
            return success;
        } catch (Exception ex) {
            System.err.println("Innerhalb der Testroutine ist ein Fehler aufgetreten. Sind alle Funktionen aufrufbar?");
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Testet Robbys Sensoren zum Aufspüren von Wänden und Akkus in einer Entfernung von einem Feld.
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public boolean testSensors() throws InstantiationException, IllegalAccessException {
        System.out.println("Sensorfunktionalität wird getetstet...");
        this.failed = false;

        // Füge Robby der Spielwelt hinzu
        this.object.setLocation(1, 1);

        // Überprüfe die Sensorfunktionen für Akkus
        this.testRotationalObjectDetection("Akku-Sensoren", new String[] { "akkuVorne", "akkuLinks", "akkuRechts", "akkuHinten" }, Akku.class);

        // Überprüfe die Sensorfunktionen für Wände
        this.testRotationalObjectDetection("Wand-Sensoren", new String[] { "wandVorne", "wandLinks", "wandRechts", "wandHinten" }, Wand.class);

        return !this.failed;
    }

    /**
     * Testet Robbys Fähigkeit Schrauben abzulegen, Akkus aufzunehmen und
     * dabei seine Statusanzeigen zu aktualisieren.
     */
    public boolean testMemory() {
        boolean success = true;
        System.out.println("Speicherfunktionalität wird getetstet...");

        success &= testObjectAquisition("akkuAufnehmen", "getAnzahlAkkus", Robby.MAX_AKKUS, Akku.class);
        this.sendStatus("Aufnahme und Begrenzung von Akkus", success);

        success &= testObjectDeposition("schraubeAblegen", "getAnzahlSchrauben", 0, Schraube.class);
        this.sendStatus("Ablage und Begrenzung von Schrauben", success);

        return success;
    }

    /**
     * Testet Robbys Fähigkeit ein geschlossenes Hindernis zu Umrunden,
     * dabei anpassbare Aktionen auszuführen und zum Ausgangspunkt
     * zurückzukehren.
     */
    public boolean testMovement() {
        System.out.println("Bewegungsfunktionalität wird getetstet...");
        this.object.setLocation(0, 1);
        this.object.setRotation(0);

        this.world.addObject(new Wand(), 1, 1);
        this.world.addObject(new Wand(), 2, 2);
        this.world.addObject(new Wand(), 1, 3);
        this.world.addObject(new Wand(), 3, 1);

        class MovementCheck implements Runnable {
            boolean success = true;

            @Override
            public void run() {
                boolean isObstacleNearby = false;
                for (int x = -1; x < 2; x++) {
                    for (int y = -1; y < 2; y++) {
                        isObstacleNearby |= !getInstance().getWorld().getObjectsAt(
                            getInstance().getX() + x,
                            getInstance().getY() + y,
                            Wand.class).isEmpty();

                    }
                }
                if (!isObstacleNearby)
                    sendStatus("Robby ist in [" + getInstance().getX() + "," + getInstance().getY() + "] vom Weg abgekommen", false);
                success &= isObstacleNearby;
            }
        }

        MovementCheck testRun = new MovementCheck();

        this.testMethod("hindernisUmrunden", null, new Class<?>[] { Runnable.class }, new Runnable[] { testRun });
        this.world.removeObjects(this.world.getObjectsAt(1, 1, Wand.class));
        this.world.removeObjects(this.world.getObjectsAt(2, 2, Wand.class));
        this.world.removeObjects(this.world.getObjectsAt(1, 3, Wand.class));
        this.world.removeObjects(this.world.getObjectsAt(3, 1, Wand.class));

        if (this.object.getX() != 0 || this.object.getY() != 1) {
            this.sendStatus("Robby ist bei der Umrundung nicht wieder am Ausgangspunkt angekommen", false);
            testRun.success = false;
        }

        this.sendStatus("Hindernis umrunden", testRun.success);
        return testRun.success;
    }

    /**
     * Testet, ob Robby Objekte aus seinem Speicher in die Welt platzieren kann und dabei Grenzen einhält.
     * @param method Methode, die ein Objekt ablegen soll
     * @param field Feld, dass dabei vermindert wird
     * @param min Minimalwert für den Speicher (danach kann kein Objekt mehr platziert werden)
     * @param cl Klasse des zu platzierenden Objekts
     * @see #testObjectAquisition
     */
    protected boolean testObjectDeposition(String method, String field, int min, Class<? extends Actor> cl) {
        this.object.setLocation(0, 0);
        int max = (Integer)this.getReturnValue(field);
         for (int x = max; x > min - 1; x--) {
            this.testMethod(method, null);
            if ((Integer)this.getReturnValue(field) < min) {
                return false;
            }
        }
        this.world.removeObjects(this.world.getObjectsAt(0, 0, cl));
        return true;
    }

    /**
     * Testet, ob Robby Objekte aus der Welt in seinen Speicher laden kann und dabei Grenzen einhält.
     * @param method Methode, die ein Objekt aufnehmen soll
     * @param field Getter für einen Integer, der den erhöhten Wert zurückgeben soll
     * @param max Maximalwert für den Speicher (danach kann kein Objekt mehr aufgenommen werden)
     * @param cl Klasse des aufzunehmenden Objekts
     * @see #testObjectDeposition
     */
    protected boolean testObjectAquisition(String method, String field, int max, Class<? extends Actor> cl) {
        this.object.setRotation(0);

        int startValue = (Integer)this.getReturnValue(field);

        Akku[] akkus = new Akku[max + 1];
        for (int x = 0; x < max + 1; x++) {
            akkus[x] = new Akku();
            this.world.addObject(akkus[x], x, 0);
            this.object.setLocation(x, 0);

            this.testMethod(method, null);

            int newValue = (Integer)this.getReturnValue(field);
            if (newValue < 0 || newValue > max) {
                this.sendStatus("Feld " + field + " blieb nicht im Bereich [ 0," + max + " ]", false);
                return false;
            }

            if (x < max) {
                if (newValue != startValue + (x + 1)
                 || !this.world.getObjectsAt(x, 0, cl).isEmpty()) {
                    this.sendStatus("Feld " + field + " zählt nach Aufnehmen eines Akkus nicht hoch oder sammelt ihn gar nicht erst ein. ", false);
                    return false;
                }
            } else {
                this.world.removeObject(akkus[x]);
            }
        }

        return true;
    }

    /**
     * Testet den Nachweis eines Objekts auf relativer Position zum Aktor.
     * @param title Bezeichnung für den Test
     * @param methods String-Array aus Methodennamen für die einzelnen Positionen (vorne, links, rechts, hinten)
     * @param cl Klasse des nachzuweisenden Aktors
     * @see #testObjektDetection
     */
    protected boolean testRotationalObjectDetection(String title, String[] methods, Class<? extends Actor> cl) {
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
    protected boolean testObjectDetection(int x, int y, String method, String test, Class<? extends Actor> cl)
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
