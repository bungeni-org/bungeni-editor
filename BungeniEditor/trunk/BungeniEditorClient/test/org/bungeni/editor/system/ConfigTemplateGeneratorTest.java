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

import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import org.bungeni.translators.configurations.steps.OAXSLTStep;

/**
 *
 * @author PC
 */
public class ConfigTemplateGeneratorTest extends TestCase {
    
    public ConfigTemplateGeneratorTest(String testName) {
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
     * Test of process method, of class ConfigTemplateGenerator.
     */
    public void testProcess() throws Exception {
        System.out.println("process");
        String configName = "debateCommon";
        String docType = "debaterecord";
        boolean cachePipeline = false;
        List<OAXSLTStep> customInputSteps = new ArrayList<OAXSLTStep>();
        List<OAXSLTStep> customOutputSteps =new ArrayList<OAXSLTStep>();
        ConfigTemplateGenerator instance = new ConfigTemplateGenerator();
        instance.process(configName, docType, cachePipeline, customInputSteps, customOutputSteps);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
