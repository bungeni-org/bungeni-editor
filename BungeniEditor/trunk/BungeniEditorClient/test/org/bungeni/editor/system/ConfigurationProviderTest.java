/*
 *  Copyright (C) 2012 PC
 * 
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package org.bungeni.editor.system;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import junit.framework.TestCase;
import org.jdom.Document;
import org.jdom.output.XMLOutputter;

/**
 *
 * @author PC
 */
public class ConfigurationProviderTest extends TestCase {
    
    public ConfigurationProviderTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of generateMergedConfiguration method, of class ConfigurationProvider.
     */
    public void testGenerateMergedConfiguration() throws IOException {
        System.out.println("generateMergedConfiguration");
        String forDocType = "debaterecord";
        ConfigurationProvider instance = ConfigurationProvider.getInstance();
        Document doc = instance.getMergedDocument();
        XMLOutputter xout = new XMLOutputter();
        File f = new File("../test/testdocs/merged_config.xml");
        System.out.println(f.getAbsolutePath());
        FileWriter fw = new FileWriter(new File("../test/testdocs/merged_config.xml"));
        xout.output(doc,fw);
        fw.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
