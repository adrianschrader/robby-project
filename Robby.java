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
    public static final int MAX_AKKUS      = 10;
    public static final int INIT_AKKUS     =  0;
    public static final int INIT_SCHRAUBEN = 10;

    /* Globale Variablen */
    public int anzahlSchrauben;
    public int anzahlAkkus;

    /**
     * Konstruktor
     */
    public Robby()
    {
        anzahlAkkus     = Robby.INIT_AKKUS;
        anzahlSchrauben = Robby.INIT_SCHRAUBEN;
    }

    /**
     * In der Methode "act" koennen Befehle / andere Methoden angewendet werden:
     * Die Methoden werden dort nacheinander "aufgerufen", wenn man
     * nach dem Kompilieren / uebersetzen den Act-Knopf drueckt.
     */
    public void act()
    {

    }

    /**
     * Robby soll hiermit einen Akku aufnehmen und im Inventar speichern. Vor
     * der Akkuaufnahme wird auf dem Feld zunächst überprüft, ob sich hier ein
     * Akku befindet. Wenn dies der Fall ist, aber auch noch weniger als 10
     * Akkus im Inventar sind, nimmt Robby einen Akku auf und fügt dem Inventar * einen hinzu und speichert dies. Hat er bereits die Maximalanzahl von 10
     * Akkus im Inventar erreicht, meldet er dies und nimmt keinen weiteren Akku * auf. Wenn sich andernfalls auch kein Akku auf dem Feld befindet, meldet
     * er dies ebenfalls.
     */
    public void akkuAufnehmen()
    {
        if((Akku)this.getOneObjectAtOffset(0, 0, Akku.class) != null)
        {
            if(anzahlAkkus < Robby.MAX_AKKUS)
            {
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
    public void schraubeAblegen ()
    {
        if(anzahlSchrauben > 0)
        {
            this.getWorld().addObject(
                new Schraube(),
                this.getX(),
                this.getY() );

            anzahlSchrauben--;
        }
        else
            System.out.println("Ich besitze keine Schrauben mehr!");
    }

    public void hindernisUmrunden2() {
        int startX = this.getX();
        int startY = this.getY();

        dreheLinks();

        do {
            for (int direction = 450; direction > 90; direction -= 90) {
                if ( !istObjektNebendran(direction, Wand.class)
                  && !istGrenzeNebendran(direction) ) {
                    bewegen(direction);
                    break;
                }
            }
        } while( startX != this.getX() || startY != this.getY() );

        dreheRechts();
    }

    public void bewegen(int direction) {
        int newDirection = (this.getRotation() + direction) % 360;
        if (newDirection != this.getRotation()) {
            this.setRotation(newDirection);
            Greenfoot.delay(1);
        }
        bewegen();
    }

    /**
     * Umrundet ein geschlossenes Hindernis aus Wänden, bis er wieder am
     * Ausgangspunkt angekommen ist.
     */
    public void hindernisUmrunden()
    {
        boolean moved = false;
        int startX = this.getX();
        int startY = this.getY();

        dreheLinks(); // Da Robby anfangs zum Hindernis Blickt, muss er sich erst in Marschrichtung drehen.

        while(!( moved && startX == this.getX() && startY == this.getY() ))
        {
            // Erst wenn rechts eine Wand steht, muss überprüft werden, ob vorne eine Wand steht, andernfalls kann gleich nach rechts gegangen werden.

            if(wandRechts() || grenzeRechts())           // Erst wenn rechts eine Wand steht, muss überprüft werden, ob vorne eine Wand steht, andernfalls kann gleich nach rechts gegangen werden
            {
                if(wandVorne() || grenzeVorne())       // wenn jedoch auch vorne eine wand steht, muss sich Robby um mindestens 90 grad drehen,
                {
                    if(wandLinks() || grenzeLinks())   // wenn auch vorbe eine Wand steht, sogar um 180 grad, und so wieder aus der Sackgasse herauslaufen.
                    {
                        if(wandHinten())
                        {
                            System.out.println("Robby kann das Hindernis nicht umrunden, weil er eingemauert ist. ");
                        } else {
                            moved = true;
                            dreheRechts();  // Drehung um 180 Grad
                            dreheRechts();
                            bewegen();
                        }
                    }
                    else
                    {
                        moved = true;
                        dreheLinks();      // Wenn Links keine Wand ist, genügt die Drehung um 90 Grad.
                        bewegen();
                    }
                }
                else
                {
                    moved = true;
                    bewegen();
                }
            }
            else
            {
                moved = true;
                dreheRechts();
                bewegen();
            }
        }

        System.out.println("Ich habe das Hindernis erfolgreich umrundet");
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
    public boolean istObjektNebendran(int direction, Class<?> cl)
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
    public boolean istGrenzeNebendran(int direction) {
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
