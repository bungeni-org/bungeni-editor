/*
 *  Copyright (C) 2012 Africa i-Parliaments
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

import junit.framework.TestCase;

/**
 *
 * @author Ashok
 */
public class StartupConfigGeneratorTest extends TestCase {
    
    public StartupConfigGeneratorTest(String testName) {
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
     * Test of startupGenerate method, of class StartupConfigGenerator.
     */
    public void testStartupGenerate() {
        System.out.println("startupGenerate");
        StartupConfigGenerator instance = StartupConfigGenerator.getInstance();
        instance.startupGenerate();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
