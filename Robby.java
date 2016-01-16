import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Der Roboter Robby kann sich in der Welt orientieren, Hindernisse umrunden,
 * Schrauben hinterlassen und Akkus aufnehmen.
 * @author Adrian Schrader, Moritz Jung, Alexander Riecke
 * @version 1.4
 */
public class Robby extends Roboter
{
    /* Konstanten */
    /** Maximale Anzahl an Akkus, die Robby tragen kann (Standard: 10) **/
    public static final int MAX_AKKUS      = 10;
    /** Anzahl an Akkus, mit der Robby initialisiert werden soll (Standard: 0) **/
    public static final int INIT_AKKUS     =  0;
    /** Anzahl an Schrauben, mit der Robby initialisiert werden soll (Standard: 10) **/
    public static final int INIT_SCHRAUBEN = 10;

    /* Globale Variablen */
    /** Anzahl an Schrauben, die Robby trägt **/
    private int anzahlSchrauben;
    /** Anzahl an Akkus, die Robby trägt **/
    private int anzahlAkkus;

    /**
     * Der Konstruktor initialisiert die Speicherwerte für Robby aus den
     * statischen Konstanten.
     */
    public Robby()
    {
        anzahlAkkus     = Robby.INIT_AKKUS;
        anzahlSchrauben = Robby.INIT_SCHRAUBEN;
    }

    /**
     * Gibt die aktuelle Anzahl an Schrauben zurück.
     */
    public int getAnzahlSchrauben() {
        return this.anzahlSchrauben;
    }

    public int getAnzahlAkkus() {
        return this.anzahlAkkus;
    }

    /**
     * In der Methode "act" koennen Befehle / andere Methoden angewendet werden:
     * Die Methoden werden dort nacheinander "aufgerufen", wenn man
     * nach dem Kompilieren / uebersetzen den Act-Knopf drueckt.
     */
    @Override
    public void act()
    {

    }

    /**
     * Robby soll hiermit einen Akku aufnehmen und im Inventar speichern. Vor
     * der Akkuaufnahme wird auf dem Feld zunächst überprüft, ob sich hier ein
     * Akku befindet. Wenn dies der Fall ist, aber auch noch weniger als 10
     * Akkus im Inventar sind, nimmt Robby einen Akku auf und fügt dem Inventar
     * einen hinzu und speichert dies. Hat er bereits die Maximalanzahl von 10
     * Akkus im Inventar erreicht, meldet er dies und nimmt keinen weiteren Akku
     * auf. Wenn sich andernfalls auch kein Akku auf dem Feld befindet, meldet
     * er dies ebenfalls.
     */
    @Override
    public void akkuAufnehmen() {
        Akku aktAkku = (Akku)this.getOneObjectAtOffset(0, 0, Akku.class);
        if(aktAkku != null) {
            if(anzahlAkkus < Robby.MAX_AKKUS) {
                this.getWorld().removeObject(aktAkku);
                anzahlAkkus++;
            }
            else
                System.out.println("Ich kann keine Akkus mehr aufnehmen, da ich bereits zehn Akkus habe!");
        }
        else
            System.out.println("Hier ist kein Akku!");
    }

    /**
     * Robby soll hiermit eine Schraube vom Inventar ablegen und dies
     * abspeichern. Bevor Robby eine Schraube auf seinem Feld ablegt, prüft er
     * aber ob er überhaupt noch mindestens eine Schraube im Inventar besitzt.
     * Ist das der Fall, legt er eine Schraube ab und von der Anzahl der
     * Schrauben im Inventar wird eine abgezogen und dies abgespeichert.
     * Andernfalls meldet Robby, dass er keine Schrauben mehr hat.
     */
    @Override
    public void schraubeAblegen() {
        if(anzahlSchrauben > 0) {
            this.getWorld().addObject(new Schraube(),
                this.getX(), this.getY() );

            anzahlSchrauben--;
        }
        else
            System.out.println("Ich besitze keine Schrauben mehr!");
    }

    /**
     * Bewegt Robby um einen Schritt in die gewünschte, relative Richtung.
     * @param direction Relative Laufrichtung
     * @see Roboter#bewegen
     */
    public void bewegen(int direction) {
        int newDirection = (this.getRotation() + direction) % 360;
        if (newDirection != this.getRotation()) {
            this.setRotation(newDirection);
            Greenfoot.delay(1);
        }
        this.bewegen();
    }

    /**
     * Umrundet aus Wänden bestehende Hindernisse ohne den Kontakt zu diesem
     * zu verliehren und bleibt am Ausgangspunkt stehen. Zu Beginn muss Robby
     * auf eine Wand blicken. Wenn Robby auf die Weltgrenze trifft, behandelt
     * er diese als Teil des Hindernisses.
     * @see #hindernisUmrunden
     */
    public void hindernisUmrunden() {
        hindernisUmrunden(new Runnable() {
            @Override
            public void run() {
            }
        });
    }

    /**
     * Umrundet aus Wänden bestehende Hindernisse ohne den Kontakt zu diesem
     * zu verliehren und bleibt am Ausgangspunkt stehen. Zu Beginn muss Robby
     * auf eine Wand blicken. Wenn Robby auf die Weltgrenze trifft, behandelt
     * er diese als Teil des Hindernisses.
     * @param action Runnable-Aktion, die auf jedem Feld ausgeführt wird
     * @see #istObjektNebendran
     * @see #istGrenzeNebendran
     */
    public void hindernisUmrunden(Runnable action) {
        int startX = this.getX(),
            startY = this.getY();

        dreheLinks();

        do {
            for (int direction = 450; direction > 90; direction -= 90) {
                if ( !istObjektNebendran(direction, Wand.class)
                  && !istGrenzeNebendran(direction) ) {
                    action.run();
                    bewegen(direction);
                    break;
                }
            }
        } while( startX != this.getX() || startY != this.getY() );

        dreheRechts();
    }

    /**
     * Der Sensor ueberprüft, ob sich in Laufrichtung des Roboters
     * die Weltgrenze befindet
     */
    public boolean grenzeVorne() {
        return this.istGrenzeNebendran(0);
    }

    /**
     * Der Sensor ueberprüft, ob sich links der Laufrichtung des Roboters
     * die Weltgrenze befindet
     */
    public boolean grenzeLinks() {
        return this.istGrenzeNebendran(270);
    }

    /**
     * Der Sensor ueberprüft, ob sich rechts der Laufrichtung des Roboters
     * die Weltgrenze befindet
     */
    public boolean grenzeRechts() {
        return this.istGrenzeNebendran(90);
    }

    /**
     * Der Sensor ueberprüft, ob sich entgegen der Laufrichtung des Roboters
     * die Weltgrenze befindet
     */
    public boolean grenzeHinten() {
        return this.istGrenzeNebendran(180);
    }

    /**
     * Der Sensor ueberprueft, ob sich in Laufrichtung des Roboters
     * ein Akku befindet.
     */
    public boolean akkuVorne()
    {
       return this.istObjektNebendran(0, Akku.class);
    }

    /**
     * Der Sensor ueberprueft, ob sich rechts der Laufrichtung des Roboters
     * ein Akku befindet.
     */
    public boolean akkuRechts()
    {
        return this.istObjektNebendran(90, Akku.class);
    }

    /**
     * Der Sensor ueberprueft, ob sich links der Laufrichtung des Roboters
     * ein Akku befindet.
     */
    public boolean akkuLinks()
    {
        return this.istObjektNebendran(-90, Akku.class);
    }

    /**
     * Der Sensor ueberprueft, ob sich links der Laufrichtung des Roboters
     * ein Akku befindet.
     */
    public boolean akkuHinten()
    {
        return this.istObjektNebendran(180, Akku.class);
    }

    /**
     * Der Sensor ueberprueft, ob sich entgegen der Laufrichtung des Roboters
     * eine Wand befindet.
     */
    public boolean wandHinten()
    {
        return this.istObjektNebendran(180, Wand.class);
    }

    /**
     * Der Sensor überprüft, ob sich neben der Laufrichtung von Robby ein
     * anderer Actor befindet.
     * @param direction Winkel von der Laufrichtung zum Suchfeld
     * @param cl Klasse des gesuchten Actors
     * @return boolean Gibt an, ob das Objekt mit den angegeben Eigenschaften existiert
     * @see #akkuVorne
     * @see #akkuRechts
     * @see #akkuLinks
     * @see #akkuHinten
     * @see #wandHinten
     */
    protected boolean istObjektNebendran(int direction, Class<?> cl)
    {
       double angle = (this.getRotation() + direction) / 180.0 * Math.PI;

       return (this.getOneObjectAtOffset(
              (int)Math.cos(angle),
              (int)Math.sin(angle), cl) != null);
    }


    /**
     * Der Sensor überprüft, ob sich neben der Laufrichtung von Robby
     * die Weltgrenze befindet.
     * @param direction Winkel von der Laufrichtung zum Ende
     * @return boolean Gibt an, ob die Weltgrenze neben Robby ist
     * @see #grenzeVorne
     * @see #grenzeRechts
     * @see #grenzeLinks
     * @see #grenzeHinten
     */
    protected boolean istGrenzeNebendran(int direction) {
        direction = direction % 360;
        if (( this.getX() + 1 >= this.getWorld().getWidth()
                && this.getRotation() == (360 - direction) % 360 )
         || ( this.getY() + 1 >= this.getWorld().getHeight()
                && this.getRotation() == (450 - direction) % 360 )
         || ( this.getX() <= 0
                && this.getRotation() == (540 - direction) % 360 )
         || ( this.getY() <= 0
                && this.getRotation() == (630 - direction) % 360 ))
            return true;
        return false;
    }

}
