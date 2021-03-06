/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.middleware.internal.service.deserializer;

import javax.jms.Message;

import org.eclipse.vorto.middleware.deserializer.IDeserializer;
import org.eclipse.vorto.middleware.deserializer.MimeType;
import org.eclipse.vorto.middleware.monitoring.IPayloadMonitor;
import org.eclipse.vorto.middleware.monitoring.MonitorMessage;
import org.eclipse.vorto.middleware.monitoring.MonitorMessage.Severity;

public class DeserializerFactory {
    private static final IDeserializer CSV_DESERIALIZER = new CsvDeserializer();
    private static final IDeserializer JSON_DESERIALIZER = new JsonDeserializer();
    private static final IDeserializer DITTO_DESERIALIZER = new DittoDeserializer();

    private static final IDeserializer NOOP_DESERIALIZER = new AbstractDeserializer() {
      
      @Override
      public Object deserialize(Object message, IPayloadMonitor monitor ) {
    	monitor.monitor(MonitorMessage.inboundMessage(getCorrelationId((Message)message),getDeviceId((Message)message),"No deserializer found for this payload.",Severity.WARNING));
        return message;
      }
    };
    
	public static IDeserializer getDeserializer(MimeType mimeType) {
	  if (mimeType == MimeType.CSV) {
	    return CSV_DESERIALIZER;
	  } else if (mimeType == MimeType.JSON) {
	    return JSON_DESERIALIZER;
	  } else if (mimeType == MimeType.ECLIPSE_DITTO) {
	    return DITTO_DESERIALIZER;
	  } else {
	    return NOOP_DESERIALIZER;
	  }
	}
}
