package org.bungeni.editor.selectors;

/**
 *
 * @author Ashok Hariharan
 */
  public class panelInfo {
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(panelInfo.class.getName());

        String panelName;
        String panelClass;
        IMetadataPanel panelObject = null;

        public panelInfo(String pname, String pclass) {
            panelName = pname;
            panelClass = pclass;
        }

        @Override
        public String toString() {
            return panelName;
        }

        public IMetadataPanel getPanelObject() {
            IMetadataPanel panel = null;
            if (panelObject != null) {
                panel = panelObject;
            } else {
                try {
                    Class metadataPanel = Class.forName(panelClass);
                    panel = (IMetadataPanel) metadataPanel.newInstance();
                    panelObject = panel;
                } catch (InstantiationException ex) {
                    log.debug("getPanelObject :" + ex.getMessage());
                } catch (IllegalAccessException ex) {
                    log.debug("getPanelObject :" + ex.getMessage());
                } catch (ClassNotFoundException ex) {
                    log.debug("getPanelObject :" + ex.getMessage());
                } catch (NullPointerException ex) {
                    log.debug("getPanelObject :" + ex.getMessage());
                }
            }
            return panel;
        }
    }
