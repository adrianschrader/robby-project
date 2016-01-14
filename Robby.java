import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Die Klasse Robby ist eine Unterklasse von Roboter.
 * Sie erbt damit alle Attribute und Methoden der Klasse Roboter.
 */

public class Robby extends Roboter
{

    /**
     *
     */
    public Robby()
    {
        
        }

    /**
     * In der Methode "act" koennen Befehle / andere Methoden angewendet werden:
     * Die Methoden werden dort nacheinander "aufgerufen", wenn man
     * nach dem Kompilieren / uebersetzen den Act-Knopf drueckt.
     */
    public void act()
    {

    }

void HindernisUmrunden()
        {
            boolean moved = false;  //zeichnet Bewegung von Robby auf
            int count_ud = 0;       // Vertikalbewegung
            int count_lr = 0;       // Horizontalbewegung
            
            dreheLinks();           // Da Robby anfangs zum Hindernis Blickt, muss er sich erst in Marschrichtung drehen.
            
            while(!(moved && count_ud == 0 && count_lr == 0))
            {
                if(WandRechts)          // erst wenn rechts eine Wand steht, muss überprüft werden, ob aich vorne eine Wand steht, andernfalls kann gleich nach rechts gegangen werden.
                {
                    if(WandVorne)       // wenn jedoch auch vorne eine wand steht, muss sich Robby um mindestens 90 grad drehen,
                    {
                        if(WandLinks)   // wenn auch vorbe eine Wand steht, sogar um 180 grad, und so wieder aus der Sackgasse herauslaufen.
                        {
                            if(WandHinten())
                            {
                                dreheRechts();  // Drehung um 180 Grad
                                dreheRechts();
                                bewegen();                      // nach dem Bewegen muss die Variable moved auf true gesetzt werden und je nach laufrichtung müssen auch die counter für die Richtung erhöht oder gesenkt werden.
                                moved = true;
                                int Blick = this.getRotation();
                                switch(Blick)
                                {
                                    case 0  : count_lr ++ ;     // Blickrichtung nach Rechts
                                        break;
                                    case 90  : count_ud ++ ;    // Blickrichtung nach Oben
                                        break;
                                    case 180 : count_lr -- ;    // Blickrichtung nach Links
                                        break;
                                    case 270 : count_ud -- ;    // Blickrichtung nach Unten
                                        break;
                                }
                                
                            }
                        }
                        else
                        {
                            dreheLinks      // Wenn Links keine Wand ist, genügt die Drehung um 90 Grad.
                            bewegen();                      // nach dem Bewegen muss die Variable moved auf true gesetzt werden und je nach laufrichtung müssen auch die counter für die Richtung erhöht oder gesenkt werden.
                            moved = true;
                            int Blick = this.getRotation();
                            switch(Blick)
                            {
                                case 0  : count_lr ++ ;     // Blickrichtung nach Rechts
                                        break;
                                case 90  : count_ud ++ ;    // Blickrichtung nach Oben
                                        break;
                                case 180 : count_lr -- ;    // Blickrichtung nach Links
                                        break;
                                case 270 : count_ud -- ;    // Blickrichtung nach Unten
                                        break;
                            }
                        }
                    }
                    else
                    {
                        bewegen();                      // nach dem Bewegen muss die Variable moved auf true gesetzt werden und je nach laufrichtung müssen auch die counter für die Richtung erhöht oder gesenkt werden.
                        moved = true;
                        int Blick = this.getRotation();
                        switch(Blick)
                        {
                            case 0  : count_lr ++ ;     // Blickrichtung nach Rechts
                                    break;
                            case 90  : count_ud ++ ;    // Blickrichtung nach Oben
                                    break;
                            case 180 : count_lr -- ;    // Blickrichtung nach Links
                                    break;
                            case 270 : count_ud -- ;    // Blickrichtung nach Unten
                                    break;
                        }
                    }
                }
                else
                {
                    dreheRechts();
                    bewegen();                      // nach dem Bewegen muss die Variable moved auf true gesetzt werden und je nach laufrichtung müssen auch die counter für die Richtung erhöht oder gesenkt werden.
                    moved = true;
                    int Blick = this.getRotation();
                    switch(Blick)
                    {
                        case 0  : count_lr ++ ;
                                break;
                        case 90  : count_ud ++ ;
                                break;
                        case 180 : count_lr -- ;
                                break;
                        case 270 : count_ud -- ;
                                break;
                    }
                }
            }
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
     * @param direction Winkel von der Laufrichtung zum Suchfeld.
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

}
