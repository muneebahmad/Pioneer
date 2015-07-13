/**
 * copyright ©2013-2014 ®Algorithmi™.
 *
 * @author ¶muneebahmad¶ (ahmadgallian@yahoo.com) NetBeans IDE
 * http://www.netbeans.org
 *
 * For all entities this program is free software; you can redistribute it
 * and/or modify it under the terms of the 'MyGdxEngine' license with the
 * additional provision that 'MyGdxEngine' must be credited in a manner that can
 * be be observed by end users, for example, in the credits or during start up.
 * (please find MyGdxEngine logo in sdk's logo folder)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * The following source - code IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY
 * KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO
 * EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES
 * OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE. *
 */
package pk.muneebahmad.client.pioneer;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import pk.muneebahmad.client.pioneer.persistence.CachePersistence;
import pk.muneebahmad.client.pioneer.persistence.LAFPersistence;
import pk.muneebahmad.client.pioneer.ui.LoginDialog;
import pk.muneebahmad.client.pioneer.ui.MainWindow;

/**
 *
 * @author muneebahmad
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            CachePersistence.load(MainWindow.CACHE_FILENAME);
            LAFPersistence.load(MainWindow.LAF_FILENAME);
        } catch (Exception e) {
                
        }
        if (!(LAFPersistence.isLoaded())) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (LAFPersistence.isLoaded()) {
            String ui = LAFPersistence.getLoadedLaf();
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if (ui.equals(info.getName())) {
                    try {
                        UIManager.setLookAndFeel(info.getClassName());
                    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (ui.equals("System")) {
                    try {
                        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                        Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        if (!(CachePersistence.isLoaded())) {
            LoginDialog dia = new LoginDialog(null, true);
            dia.setLocationRelativeTo(null);
            dia.pack();
            dia.setVisible(true);
        } else if (CachePersistence.isLoaded()) {
            /* Create and display the form */
            java.awt.EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new MainWindow(true).setVisible(true);
                }
            });
        }
    }

}
/**
 * end class.
 */
