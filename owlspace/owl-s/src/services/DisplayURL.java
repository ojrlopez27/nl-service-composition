/*
 * Created on Oct 5, 2005
 */
package services;

import java.net.URL;

import org.mindswap.utils.BrowserControl;

/**
 * @author Evren Sirin
 *
 */
public class DisplayURL {
    public void displayURL( URL url ) {
        BrowserControl.displayURL( url.toString() );
    }
}
