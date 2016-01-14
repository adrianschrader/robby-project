import java.util.*;
import java.lang.reflect.*;

/**
 * Basisklasse zum Testen einzelner Klassen in Greenfoot. Benötigt den Klassentyp. Implementierungen sollten eigene Subklassen verwenden.
 * @author Adrian Schrader
 * @version 1.0.0
 */
public class FeatureTest<T> {
    /* Statischer Text für Statusmeldungen */
    public static final String MESSAGE_PASSED = "  Bestanden  ";
    public static final String MESSAGE_FAILED = "Durchgefallen";

    /* Attribute und zu testende Objektinstanz */
    protected T object;
    protected Class<T> cl;
    protected String name;
    protected boolean failed;

    /**
     * Instanziiert die Klasse über den Typ der Testklasse. Die zu testende
     * Klasseninstanz wird automatisch erstellt.
     */
    public FeatureTest(Class<T> cl) {
        this.cl = cl;
        this.name = cl.getName();
        this.failed = false;

        try {
            this.object = cl.newInstance();
        }
        catch (Exception ex) {
            System.err.println("Es konnte keine Instanz von " + this.name
                + " erstellt werden. ");
        }
    }

    /**
     * Instanziiert die Klasse über ein bestehendes Objekt.
     */
    public FeatureTest(T obj) {
        this.cl = (Class<T>)obj.getClass();
        this.name = cl.getName();
        this.failed = false;
        this.object = obj;
    }

    /**
     * Gibt die erstellte Testinstanz der zu testenden Klasse zurück.
     */
    public T getInstance() {
        return this.object;
    }

    /**
     * Gibt den Namen der Testklasse zurück, der auch in den Statusmeldungen
     * benutzt wird.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Diese Funktion sollte von Unterklassen überschrieben werden, um alle
     * Tests auszuführen und deren Ergebnisse zurückzugeben.
     */
    public boolean testAllFeatures(RoboterWelt welt) {
        return this.failed;
    }

    /**
     * Gibt die Statusmeldungen für einzelne Tests aus
     */
    protected void sendStatus(String message, boolean passed) {
        System.out.println("[" + (passed ? this.MESSAGE_PASSED : this.MESSAGE_FAILED) + "] " + message);
    }

    /**
     * Überprüft, ob das reflexive Feld der Klasse einem Sollwert entspricht.
     * @param name Name des Fields
     * @param target Sollwert für das angegebene Feld
     * @return Gibt an, ob der Sollwert mit dem Feldwert übereinstimmt
     * @see testMethod
     */
    protected boolean testField(String name, Object target) {
        try {
            return cl.getField(name).get(this.object) == target;
        } catch (Exception ex) {
            this.sendStatus(name, false);
            failed = true;

            System.err.println("Fehlerhaftes Testskript (testMethod): Feld in " + cl.getName() + " konnte nicht geladen werden. (Nachricht: " + ex.getMessage() + ")");
            return false;
        }
    }

    /**
     * Überprüft, ob die reflektiv angegebene Methode den Zielwert zurückgibt.
     * Diese Überladung geht davon aus, dass die Methode keine Argumente
     * benötigt.
     * @param name Name der Methode
     * @param returnValue Der erwartete Rückgabewert
     * @return Gibt an, ob Erwartungswert mit dem Rückgabewert übereinstimmt
     * @see #testMethod
     */
    protected boolean testMethod(String name, Object returnValue)
    {
        return this.testMethod(name, returnValue, new Object[] {});
    }

    /**
     * Überprüft, ob die reflektiv angegebene Methode den Zielwert zurückgibt.
     * @param name Name der Methode
     * @param parameters Array der benötigten Parameter der gesuchten Methode
     * @param returnValue Object, dass dem Rückgabewert entsprechen soll
     * @return Gibt an, ob die Methode den Test bestanden hat
     */
    protected boolean testMethod(String name, Object returnValue, Object[] parameters) {
        Class<?>[] parameterTypes = new Class<?>[parameters.length];
        for (int index = 0; index < parameters.length; index++) {
            parameterTypes[index] = parameters[index].getClass();
        }

        try {
            Method method = this.cl.getMethod(name, parameterTypes);

            if (!method.invoke(this.object, parameters).equals(returnValue))
            {
                failed = true;
                return false;
            }

            return true;
        }
        catch (Exception ex)
        {
            this.sendStatus(name, false);
            failed = true;

            System.err.println("Fehlerhaftes Testskript (testMethod): Methode in " + cl.getName() + " konnte nicht geladen werden. (Nachricht: " + ex.getMessage() + ")");
            return false;
        }
    }
}
