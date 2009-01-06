/*
 * BungeniCommandsCatalogLoader.java
 *
 * Created on December 20, 2007, 1:06 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.commands.chains;

import org.apache.commons.chain.Catalog;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.chain.config.ConfigParser;
import org.apache.commons.chain.impl.CatalogFactoryBase;
import org.bungeni.editor.selectors.BungeniFormContext;

/**
 *
 * @author Administrator
 */
public class BungeniCommandsCatalogLoader {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BungeniCommandsCatalogLoader.class.getName());

	private static  String CONFIG_FILE = 
		"/org/bungeni/commands/chains/commandChain.xml";
	/* parser used to parse the commandChain file*/
        private ConfigParser parser;
	/* command catalog object */
        private Catalog catalog;
        /* catalog command object describing the command to be loaded and executed*/
        private BungeniCatalogCommand catalogCommand ;
        
	public BungeniCommandsCatalogLoader() {
            initParser();
	}

        public BungeniCommandsCatalogLoader(BungeniCatalogCommand cmd) {
                initParser();
                catalogCommand = cmd;
        }
        
        private void initParser(){
                parser = new ConfigParser();
                parser.setUseContextClassLoader(false);
        }
        /*
        public Catalog getCatalog() {
            try {
                parser.parse(this.catalogCommand.getCatalogSourceURL());		
		catalog = CatalogFactoryBase.getInstance().getCatalog();
            } catch (Exception ex) {
                log.error("getCatalog = " + ex.getMessage());
            } finally {
                return catalog;
            }
           }
        */
        public Catalog getCatalog() {
            log.info("getCatalog for: " + this.catalogCommand.getCommandCatalog());
            log.info("getCatalog for path : " + this.catalogCommand.getCatalogSourceURL().toString());
            try {
            parser.parse(this.catalogCommand.getCatalogSourceURL());
            catalog = CatalogFactoryBase.getInstance().getCatalog(this.catalogCommand.getCommandCatalog());
            } catch (Exception ex) {
                log.error("getCatalog (" + catalogCommand.getCommandCatalog() + ") = " + ex.getMessage());
            } finally {
            return catalog;
            }
        }
        
        /*
        public Catalog getCatalog(String catalogName) throws Exception {
		if (catalog == null) {
                    parser.parse(this.getClass().getResource(CONFIG_FILE));		
		}
		catalog = CatalogFactoryBase.getInstance().getCatalog(catalogName);
		return catalog;
	}
        */
        
	public static void main(String[] args) throws Exception {
                //move to unit tests...
		BungeniCommandsCatalogLoader loader = new BungeniCommandsCatalogLoader();
		/*Catalog sampleCatalog = loader.getCatalog("debaterecord");
		Command command = sampleCatalog.getCommand("debateRecordInsertMasthead"); */
                //Context ctx = new BungeniFormContext();
		//command.execute(ctx);
	}
}