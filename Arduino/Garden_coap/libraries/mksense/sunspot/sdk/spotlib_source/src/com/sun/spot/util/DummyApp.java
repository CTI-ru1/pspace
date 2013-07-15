/*
 * Copyright 2007-2009 Sun Microsystems, Inc. All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER
 * 
 * This code is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2
 * only, as published by the Free Software Foundation.
 * 
 * This code is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License version 2 for more details (a copy is
 * included in the LICENSE file that accompanied this code).
 * 
 * You should have received a copy of the GNU General Public License
 * version 2 along with this work; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA
 * 
 * Please contact Sun Microsystems, Inc., 16 Network Circle, Menlo
 * Park, CA 94025 or visit www.sun.com if you need additional
 * information or have any questions.
 */

package com.sun.spot.util;

import java.io.IOException;

import com.sun.spot.peripheral.Spot;
import com.sun.spot.peripheral.ota.OTACommandServer;
import com.sun.spot.service.Heartbeat;

/**
 * This class implements an empty application that can be useful when 
 * there is no valid application installed.
 */
public class DummyApp {

    /**
     * A main method that allows this class to be used as a startup class
     * 	 
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        new DummyApp().runIt();
    }

    private void runIt() {
        System.out.println("Running dummy application...");
        new BootloaderListener().start();
        new Heartbeat(10000, 3300).start();
        Spot.getInstance().getSleepManager().disableDeepSleep();
        OTACommandServer.getInstance().start();
        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
    }
}
