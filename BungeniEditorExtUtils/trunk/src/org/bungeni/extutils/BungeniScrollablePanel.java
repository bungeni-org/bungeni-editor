package org.bungeni.extutils;

import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.JPanel;
import javax.swing.Scrollable;

/**
 * Extended version of JPanel supporting the Scrollable interface
 * @author Ashok Hariharan
 */
public class BungeniScrollablePanel extends JPanel implements Scrollable {

    private boolean scrollableTracksViewportHeight;
    private boolean scrollableTracksViewportWidth;

    /* (non-Javadoc)
     * @see javax.swing.Scrollable#getScrollableTracksViewportHeight()
     */
    public boolean getScrollableTracksViewportHeight() {
        return scrollableTracksViewportHeight;
    }

    /* (non-Javadoc)
     * @see javax.swing.Scrollable#getScrollableTracksViewportWidth()
     */
    public boolean getScrollableTracksViewportWidth() {
        return scrollableTracksViewportWidth;
    }

    /* (non-Javadoc)
     * @see javax.swing.Scrollable#getPreferredScrollableViewportSize()
     */
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    /* (non-Javadoc)
     * @see
    javax.swing.Scrollable#getScrollableBlockIncrement(java.awt.Rectangle,
    int, int)
     */
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 10;
    }

    /* (non-Javadoc)
     * @see
    javax.swing.Scrollable#getScrollableUnitIncrement(java.awt.Rectangle,
    int, int)
     */
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 10;
    }

    /**
     * @param scrollableTracksViewportHeight The
    scrollableTracksViewportHeight to set.
     */
    public void setScrollableTracksViewportHeight(boolean scrollableTracksViewportHeight) {
        this.scrollableTracksViewportHeight =
                scrollableTracksViewportHeight;
    }

    /**
     * @param scrollableTracksViewportWidth The
    scrollableTracksViewportWidth to set.
     */
    public void setScrollableTracksViewportWidth(boolean scrollableTracksViewportWidth) {
        this.scrollableTracksViewportWidth = scrollableTracksViewportWidth;
    }
}

