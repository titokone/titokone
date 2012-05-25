/* This class was used for testing GUIBrain */

package fi.helsinki.cs.titokone.GUIBrainStubs;

import fi.helsinki.cs.titokone.*;
import fi.helsinki.cs.ttk91.TTK91NoKbdData;

import java.io.*;
import java.net.*;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class __stub_Control {


    public __stub_Control(File defaultStdInFile, File defaultStdOutFile) {

    }

    public String loadSettingsFileContents(File settingsFile) {
        if (settingsFile.getName().equals("languages.cfg")) {
            return new String("English = en.GB\n" + "Suomi = fi.FI");
        } else if (settingsFile.getName().equals("settings.cfg")) {
            return new String(
                    "# Default settings. Running mode: commented, one row at a\n" +
                            "# time, compilation mode: commented. Memory size 2^9 = 512 \n" +
                            "# words. Stdin and stdout file paths are relative to the \n" +
                            "# current working directory. Stdout use describes that the\n" +
                            "# file should be used in append mode instead of overwriting\n" +
                            "# it for every new run.\n" +
                            "Language = 0\n" +
                            "Running mode = 3\n" +
                            "Compilation mode = 3\n" +
                            "Stdin file = stdin\n" +
                            "Stdin path = relative\n" +
                            "Stdout file = stdout\n" +
                            "Stdout path = relative\n" +
                            "Stdout use = append\n" +
                            "Memory size = 9\n");

        } else {
            return new String("gnreghnkrejnhnhjk");
        }

    }

    public void openBinary(File openedFile) throws IOException {
        __stub_RunInfo.line = -1;
    }

    public __stub_LoadInfo load() {
        return new __stub_LoadInfo();
    }

    public String openSource(File openedFile) throws IOException {
        String ret = "g54qhyuq5htrhrshwjw56j56wjw5j56eje5j5j5ejnytgn  4ihnwjkhnw5jknh5wjkn\ngnergernjkgre\ngnrejkgnkerge\nbjtrnbjkre";
        return ret;
    }

    public __stub_CompileInfo compileLine() {
        return new __stub_CompileInfo();
    }

    public __stub_RunInfo runLine() throws TTK91NoKbdData {
        __stub_RunInfo ri = new __stub_RunInfo();
        if (ri.getLineNumber() == 4) {
            throw new TTK91NoKbdData(null);
        }
        return ri;
    }

    public void keyboardInput(int inputValue) {
    }


    public void eraseMemory() {
    }

    public void setLanguage(String language) {
    }

    public ResourceBundle loadLanguageFile(File rbFile) throws ResourceLoadFailedException {

        Class theClass;
        Object translations;
        String className, errorMessage;
        String errorParams[] = new String[2];
        Logger logger = Logger.getLogger(getClass().getPackage().getName());
        URL[] url = new URL[1];

        // Poistetaan .class tiedostosta.
        className = changeExtension(rbFile, "").getName();

        try {
            url[0] = rbFile.toURL(); // MalformedURLException, anyone?
            System.out.println(url[0]);
            //URLClassLoader loader = new URLClassLoader(url); // SecurityExcp..
            URLClassLoader loader = new URLClassLoader(new URL[]{new URL("file:/D:/Documents and Settings/Arto/My Documents/Tuutorointi/")});
            theClass = loader.loadClass(className); // ClassNotFoundExcp..?
            // InstantiationException or IllegalAccessException, anyone?
            translations = theClass.newInstance();
            return (ResourceBundle) translations;  // ClassCastException?
        } catch (Exception originalException) {
            // Throw an exception with a message like "<exception name> in
            // loadResourceBundle(): <original exception message>".
            errorParams[0] = originalException.getClass().getName();
            errorParams[1] = originalException.getMessage();
            errorMessage = new Message("{0} in loadResourceBundle(): {1}",
                    errorParams).toString();
            logger.fine(errorMessage);
            throw new ResourceLoadFailedException(errorMessage);
        }
    }

    public void setDefaultStdIn(File stdinFile) throws IOException {
    }

    public void setDefaultStdOut(File stdoutFile) throws IOException {
    }

    public void changeMemorySize(int newSize) {
        System.out.println(newSize);
    }

    public void saveSettings(String currentSettings, File settingsFile) {
    }


    /**
     * This method changes the extension of the given filename to
     * newExtension and returns the new filename as a File object.
     * File extensions are considered to be the part after the last period
     * in the File.getName(). If there are no periods in that part,
     * the file is considered to be without an extension and newExtension
     * is added.
     */
    public File changeExtension(File f, String newExtension) {
        String filenamestr;
        File parent;
        filenamestr = f.getName();
        parent = f.getParentFile();

        return new File(parent, changeExtensionStr(filenamestr,
                newExtension));
    }

    /**
     * This method returns the first string modified so that the part of
     * it following the last period is removed, including the period,
     * and the result is this modified followed by newExtension. If
     * newExtension is not an empty string, the two are separated with a
     * ".".
     */
    private String changeExtensionStr(String filename, String newExtension) {
        String result;
        int lastPeriod;
        lastPeriod = filename.lastIndexOf(".");
        if (lastPeriod == -1) {
            lastPeriod = filename.length();
        }
        result = filename.substring(0, lastPeriod);
        if (!newExtension.equals("")) {
            result += "." + newExtension;
        }
        return result;
    }


}