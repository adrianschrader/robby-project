import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

import java.util.*;


/**
 * Diese Klasse ist die Oberklasse fuer die Roboter "Robita", "Robson" und "Robby". 
 * Programme sollen nur in den Unterklassen 
 * implementiert werden, da diese Klasse fuer Java-Beginner sehr komplex ist.
 * Ein Roboter kann sich felderweise in die vier Himmelsrichtungen im Szenario bewegen. 
 * Ueber Sensoren kann er Informationen ueber seine Umwelt erhalten.
 */

public class Roboter extends Actor
{

    /**
     * Ein Objekt der Klasse Roboter wird exempliert bzw. instanziert.
     */
    public Roboter()
    {
    }
    
    
    /**
     * Der Roboter geht einen Schritt in die Pfeilrichtung.
     * Das macht er nur, wenn sich keine Wand vor ihm befindet oder
     * er nicht an der Grenze der Welt zur Grenze blickt.
     */
    
    public void bewegen()
    {
        int lRichtung = this.getRotation();
            
        if (this.kannBewegen())
        {   
            switch(lRichtung)
            {
            case 0 :
                this.setLocation(getX()+1, getY());
                break;
            case 90 :
                this.setLocation(getX(), getY()+1);
                break;
            case 180 :
                this.setLocation(getX()-1, getY());
                break;
            case 270 :
                this.setLocation(getX(), getY()-1);
                break;
            }
            
            Greenfoot.delay(1);
        }
        else
        {
  
             System.out.println("Ich kann mich nicht bewegen!");   
        }

    }

    
    /**
     * Der Roboter dreht sich um 90 Grad nach rechts (aus der Sicht des Roboters).
     */
    public void dreheRechts()
    {
            this.setRotation((getRotation()+90)%360);
            Greenfoot.delay(1);
    }
    
    /**
     * Der Roboter dreht sich um 90 Grad nach links (aus der Sicht des Roboters).
     */
    public void dreheLinks()
    {
            this.setRotation((getRotation()+270)%360);
            Greenfoot.delay(1);
    }
    
    
    /**
     * Der Roboter nimmt einen Akku auf. Dieser Akku muss sich auf seinem Feld befinden.
     */
    public void akkuAufnehmen()
    {
            Akku aktAkku = (Akku)this.getOneObjectAtOffset(0, 0, Akku.class);
            if(aktAkku != null)
            {
                this.getWorld().removeObject(aktAkku);
            }
            else
            {
                System.out.println("Hier ist kein Akuu!");   
            }
     }
     
    /**
     * Der Roboter legt eine Schraube auf seinem Feld ab.
     */      
    public void schraubeAblegen ()
    {
          int aktuellesX = this.getX();
          int aktuellesY = this.getY();
          
          Schraube schraube = new Schraube();
          this.getWorld().addObject(schraube, aktuellesX, aktuellesY);
          this.getWorld().addObject(this, aktuellesX, aktuellesY);
    }
 
    
     /**
     * Der Sensor ueberprueft, ob sich der Roboter bewegen kann?
     */     
    private boolean kannBewegen()
    {
        if (this.wandVorne())
        {
            return false;   
        }       
        if (this.getRotation() == 0 && this.getX() == this.getWorld().getWidth()-1)
        {
            return false;   
        }                   
        if (this.getRotation() == 90 && this.getY() == this.getWorld().getHeight()-1)
        {
            return false;   
        }        
        if (this.getRotation() == 180 && this.getX() == 0)
        {
            return false;   
        }       
        if (this.getRotation() == 270 && this.getY() == 0)
        {
            return false;   
        }         
        return true;
        
    }
    
     /**
     * Der Sensor ueberprueft, ob der Roboter nach rechts blickt.
     */  
    
    private boolean rechts()
    {
        if(this.getRotation()==0)
        {
            return true;
        }
        else
        {
            return false;   
        }
    }
    
    private boolean links()
    {
        if(this.getRotation()==180)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
      /**
      * Der Sensor ueberprueft, ob auf dem Feld des Roboters ein Akku liegt.
      */  
    public boolean akkuAufFeld ()
    {
        if (this.getOneObjectAtOffset(0, 0, Akku.class)!= null)
        {
           return true;     
        } 
        else
        {
            return false;   
        }
    }

      /**
      * Der Sensor ueberprueft, ob sich der Roboter an der Weltgrenze befindet und zur Grenze "blickt".
      */  
    public boolean anGrenze()
    {
        if (this.getX()+1>=this.getWorld().getWidth() || this.getX()<=0)  
        {return true;}

        if (this.getY()+1>=this.getWorld().getHeight() || this.getY()<=0)  
        {return true;}
        
        return false;
        
    }
    
    
     /**
      * Der Sensor ueberprueft, ob sich in Laufrichtung des Roboters eine Wand befindet.
      */
     public boolean wandVorne()
     {
         int lRichtung = this.getRotation();
         
         if (lRichtung == 0 && this.getOneObjectAtOffset(1, 0, Wand.class)!= null)
         {
            return true;     
         }
         
         if (lRichtung == 90 && this.getOneObjectAtOffset(0, 1, Wand.class)!= null)
         {
            return true;     
         }
         
         if (lRichtung == 180 && this.getOneObjectAtOffset(-1, 0, Wand.class)!= null)
         {
            return true;     
         }
         
         if (lRichtung == 270 && this.getOneObjectAtOffset(0, -1, Wand.class)!= null)
         {
            return true;     
         }
         
         return false;
     } 

     /**
      * Der Sensor ueberprueft, ob sich rechts der Laufrichtung eine Wand befindet.
      */    
     public boolean wandRechts()
     {
         int lRichtung = this.getRotation();
         
         if (lRichtung == 0 && this.getOneObjectAtOffset(0, 1, Wand.class)!= null)
         {
            return true;     
         }
         
         if (lRichtung == 90 && this.getOneObjectAtOffset(-1, 0, Wand.class)!= null)
         {
            return true;     
         }
         
         if (lRichtung == 180 && this.getOneObjectAtOffset(0, -1, Wand.class)!= null)
         {
            return true;     
         }
         
         if (lRichtung == 270 && this.getOneObjectAtOffset(1, 0, Wand.class)!= null)
         {
            return true;     
         }
         
         return false;
     }

     
     /**
      * Der Sensor ueberprueft, ob sich linkss der Laufrichtung eine Wand befindet.
      */  
     public boolean wandLinks()
     {
         int lRichtung = this.getRotation();
         
         if (lRichtung == 0 && this.getOneObjectAtOffset(0, -1, Wand.class)!= null)
         {
            return true;     
         }
         
         if (lRichtung == 90 && this.getOneObjectAtOffset(1, 0, Wand.class)!= null)
         {
            return true;     
         }
         
         if (lRichtung == 180 && this.getOneObjectAtOffset(0, 1, Wand.class)!= null)
         {
            return true;     
         }
         
         if (lRichtung == 270 && this.getOneObjectAtOffset(-1, 0, Wand.class)!= null)
         {
            return true;     
         }
         
         return false;
     }
     
}