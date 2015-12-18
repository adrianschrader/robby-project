import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

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
        this.testObjectDetection(2, 1, "akkuVorne", Akku.class);
        this.testObjectDetection(1, 0, "akkuLinks", Akku.class);
        this.testObjectDetection(1, 2, "akkuRechts", Akku.class);
        this.testObjectDetection(0, 1, "akkuHinten", Akku.class);

        // Überprüfe die Sensorfunktionen für Wände
        this.testObjectDetection(2, 1, "wandVorne", Wand.class);
        this.testObjectDetection(1, 0, "wandLinks", Wand.class);
        this.testObjectDetection(1, 2, "wandRechts", Wand.class);
        this.testObjectDetection(0, 1, "wandHinten", Wand.class);

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
        // Hier sollten die Speicherfeatures abgefragt werden.
        return true;
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
    private boolean testObjectDetection(int x, int y, String method, Class<? extends Actor> cl)
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

        return (positive && negative);
    }
}
