package 클라이언트;

import java.lang.reflect.Method;

public class BrowserControl {
    public static void openUrl(String url) {
        String os = System.getProperty("os.name");
        Runtime runtime = Runtime.getRuntime();
        try {
            if (os.startsWith("Windows")) {
                String cmd = "rundll32 url.dll,FileProtocolHandler " + url;
                Process p = runtime.exec(cmd);
            }
            else if (os.startsWith("Mac OS")) {
                Class fileMgr = Class.forName("com.apple.eio.FileManager");
                Method openURL = fileMgr.getDeclaredMethod("openURL", new Class[] { String.class });
                openURL.invoke(null, new Object[] { url });
            }
        } catch (Exception x) {
            System.err.println("Exception occurd while invoking Browser!");
            x.printStackTrace();
        }
    }
 
    public static void main(String[] args) {
        
    }
}
