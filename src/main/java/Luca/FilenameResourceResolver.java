package Luca;

public abstract class FilenameResourceResolver {
    public static String resolvePath(String resourceFile){
        String file = App.class.getResource(resourceFile).toString();
        return file.substring(5); // Strips "file: " from path
    }
}
