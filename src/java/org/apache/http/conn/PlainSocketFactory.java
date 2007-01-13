/*
 * $HeadURL$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.http.conn;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/**
 * The default class for creating sockets.
 * This class just uses the {@link java.net.Socket socket} API
 * in Java 1.4 or greater.
 * 
 * @author <a href="mailto:http-async@dubioso.net">Roland Weber</a>
 * @author Michael Becke
 */
public final class PlainSocketFactory implements SocketFactory {

    /**
     * The factory singleton.
     */
    private static final
        PlainSocketFactory DEFAULT_FACTORY = new PlainSocketFactory();

    /**
     * Gets the singleton instance of this class.
     * @return the one and only plain socket factory
     */
    public static final PlainSocketFactory getSocketFactory() {
        return DEFAULT_FACTORY;
    }

    /**
     * Restricted default constructor.
     */
    private PlainSocketFactory() {
        super();
    }


    // non-javadoc, see interface org.apache.http.conn.SocketFactory
    public Socket createSocket() {
        return new Socket();
    }

    // non-javadoc, see interface org.apache.http.conn.SocketFactory
    public Socket connectSocket(Socket sock, String host, int port, 
                                InetAddress localAddress, int localPort,
                                HttpParams params)
        throws IOException {

        if (host == null) {
            throw new IllegalArgumentException("Target host may not be null.");
        }
        if (params == null) {
            throw new IllegalArgumentException("Parameters may not be null.");
        }

        // resolve the target hostname first
        final InetSocketAddress target = new InetSocketAddress(host, port);

        if (sock == null)
            sock = createSocket();

        if ((localAddress != null) || (localPort > 0)) {

            // we need to bind explicitly
            if (localPort < 0)
                localPort = 0; // indicates "any"

            InetSocketAddress isa =
                new InetSocketAddress(localAddress, localPort);
            sock.bind(isa);
        }

        int timeout = HttpConnectionParams.getConnectionTimeout(params);
        sock.connect(target, timeout);

        return sock;

    } // connectSocket


    /**
     * Compares this factory with an object.
     * There is only one instance of this class.
     *
     * @param obj       the object to compare with
     *
     * @return  iff the argument is this object
     */
    public boolean equals(Object obj) {
        return (obj == this);
    }

    /**
     * Obtains a hash code for this object.
     * All instances of this class have the same hash code.
     * There is only one instance of this class.
     */
    public int hashCode() {
        return PlainSocketFactory.class.hashCode();
    }

}
