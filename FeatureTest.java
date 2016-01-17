import java.lang.reflect.*;

/**
 * Basisklasse zum Testen einzelner Klassen in Greenfoot. Benötigt den 
 * Klassentyp. Implementierungen sollten eigene Subklassen verwenden.
 * @author Adrian Schrader
 * @version 1.5
 * @param <T> Typ der getesteten Klasse
 */
public class FeatureTest<T> {
    /** Statusmeldung für bestandene Tests **/
    public static final String MESSAGE_PASSED = "  Bestanden  ";
    /** Statusmeldung für nicht bestandene Tests **/
    public static final String MESSAGE_FAILED = "Durchgefallen";

    /* Attribute und zu testende Objektinstanz */
    private T object;
    private Class<T> cl;
    private String name;
    
    private boolean failed;

    /**
     * Instanziiert die Klasse über den Typ der Testklasse. Die zu testende
     * Klasseninstanz wird automatisch erstellt.
     * @param cl Typ der Testklasse
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
     * @param obj Objekt vom zu testenden Typ
     */
    public FeatureTest(T obj) {
        this.cl = (Class<T>)obj.getClass();
        this.name = cl.getName();
        this.failed = false;
        this.object = obj;
    }

    /**
     * @return Instanz der zu testenden Klasse
     */
    public T getInstance() {
        return this.object;
    }

    /**
     * @return Name der Testklasse, der auch in den Statusmeldungen benutzt wird.
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * @return Klasse der Testinstanz
     */
    public Class<?> getTestClass() {
        return this.cl;
    }
    
    /**
     * @return Erfolg der Testergebnisse
     */
    public boolean hasFailed() {
        return this.failed;
    }
    
    /**
     * @param failed Erfolg der Testergebnisse
     */
    protected void hasFailed(boolean failed) {
        this.failed = failed;
    }

    /**
     * Diese Funktion sollte von Unterklassen überschrieben werden, um alle
     * Tests auszuführen und deren Ergebnisse zurückzugeben.
     * @return Erfolg des Tests
     */
    public boolean testAllFeatures() {
        return this.failed;
    }

    /**
     * Gibt die Statusmeldungen für einzelne Tests aus
     * @param message Nachricht für die Konsole/Log
     * @param passed Erfolgreiche Meldung
     * @see #MESSAGE_PASSED
     * @see #MESSAGE_FAILED
     */
    protected void sendStatus(String message, boolean passed) {
        System.out.println("[" + (passed ? FeatureTest.MESSAGE_PASSED : FeatureTest.MESSAGE_FAILED) + "] " + message);
    }

    /**
     * Gibt das reflexive Feld aus.
     * @param <F> Typ des Feldes
     * @param name Name des Fields
     * @param type Typ des Feldes
     * @return Aktueller gecasteter Wert des Feldes
     * @see testField
     */
    protected <F> F getField(String name, Class<F> type) {
        try {
            return (F)(cl.getField(name).get(this.object));
        } catch (Exception ex) {
            this.sendStatus(name, false);
            failed = true;

            System.err.println("Fehlerhaftes Testskript (testMethod): Feld in " + cl.getName() + " konnte nicht geladen oder gecastet werden. (Nachricht: " + ex.getMessage() + ")");
            return null;
        }
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
        return this.testMethod(name, returnValue, new Class<?>[]{}, new Object[] {});
    }

    /**
     * Überprüft, ob die reflektiv angegebene Methode den Zielwert zurückgibt.
     * @param name Name der Methode
     * @param expectedValue Erwartungswert für den Rückgabewert
     * @param parameterTypes Array der Typen der Parameter, mit denen die Methode gefunden werden kann
     * @param parameters Array der benötigten Parameter der gesuchten Methode
     * @return Gibt an, ob die Methode den Test bestanden hat
     */
    protected boolean testMethod(String name, Object expectedValue, Class<?>[] parameterTypes, Object[] parameters) {
        try {
            Method method = this.cl.getMethod(name, parameterTypes);
            Object returnValue = method.invoke(this.object, parameters);

            if (expectedValue != null) {
                if (!returnValue.equals(expectedValue))
                {
                    failed = true;
                    return false;
                }
            }

            return true;
        }
        catch (Exception ex)
        {
            this.sendStatus(name, false);
            failed = true;

            System.err.println("Fehlerhaftes Testskript (testMethod): Methode in " + cl.getName() + " konnte nicht geladen werden. ");
            return false;
        }
    }

    /**
     * Gibt den Wert eines Getters ohne Parameter mit dem zugehörigen Namen zurück
     * @param name Name der Methode
     * @return Rückgabewert der Methode
     */
    protected Object getReturnValue(String name) {
        return getReturnValue(name, new Class<?>[] {}, new Object[] {});
    }

    /**
     * Führt die angegebene Methode reflexiv aus und gibt seinen Rückgabewert zurück
     * @param name Name der Methode
     * @param parameterTypes Array der Typen der Parameter, mit denen die Methode gefunden werden kann
     * @param parameters Array der benötigten Parameter der gesuchten Methode
     * @return Rückgabewert der Methode
     */
    protected Object getReturnValue(String name, Class<?>[] parameterTypes, Object[] parameters) {
         try {
            Method method = this.cl.getMethod(name, parameterTypes);
            return method.invoke(this.object, parameters);
        }
        catch (Exception ex)
        {
            this.sendStatus(name, false);
            failed = true;

            System.err.println("Fehlerhaftes Testskript (getReturnValue): Methode in " + cl.getName() + " konnte nicht geladen werden. ");
            return false;
        }
    }
}
