import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Die Klasse Robby ist eine Unterklasse von Roboter.
 * Sie erbt damit alle Attribute und Methoden der Klasse Roboter.
 */

public class Robby extends Roboter
{
    /**
     * In der Methode "act" koennen Befehle / andere Methoden angewendet werden:
     * Die Methoden werden dort nacheinander "aufgerufen", wenn man
     * nach dem Kompilieren / uebersetzen den Act-Knopf drueckt.
     */
    public void act()
    {

    }

    /**
     * Der Sensor ueberprueft, ob sich in Laufrichtung des Roboters ein Akku befindet.
     * @return boolean
     */
    public boolean akkuVorne()
    {
       return this.istObjektNebendran(0, Akku.class);
    }

    /**
     * Der Sensor ueberprueft, ob sich rechts der Laufrichtung des Roboters ein Akku befindet.
     * @return boolean
     */
    public boolean akkuRechts()
    {
        return this.istObjektNebendran(90, Akku.class);
    }

    /**
     * Der Sensor ueberprueft, ob sich links der Laufrichtung des Roboters ein Akku befindet.
     * @return boolean
     */
    public boolean akkuLinks()
    {
        return this.istObjektNebendran(-90, Akku.class);
    }

    /**
     * Der Sensor ueberprueft, ob sich entgegen der Laufrichtung des Roboters
     * eine Wand befindet.
     * @return boolean
     */
    public boolean wandHinten()
    {
        return this.istObjektNebendran(180, Wand.class);
    }

    /**
     * Der Sensor überprüft, ob sich neben Robby ein anderes Object befindet.
     * @params angle Winkel von der Horizontalen in dessen Richtung gesucht werden soll. Der Winkel wird in ganzzahligen Vielfachen von 90° angegeben.
     * @params object Klasse des gesuchten Actors als Erasure
     * @return boolean
     */
    public boolean istObjektNebendran(int angle, Class<?> object) {
       double phi = (this.getRotation() + angle) / 180.0 * Math.PI;

       if (this.getOneObjectAtOffset((int)Math.cos(phi), (int)Math.sin(phi), object) != null)
       {
          return true;
       }

       return false;
    }

}
