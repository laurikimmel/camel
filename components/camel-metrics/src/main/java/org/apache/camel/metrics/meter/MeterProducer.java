/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.metrics.meter;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.metrics.AbstractMetricsProducer;
import static org.apache.camel.metrics.MetricsComponent.HEADER_METER_MARK;

public class MeterProducer extends AbstractMetricsProducer<MeterEndpoint> {

    public MeterProducer(MeterEndpoint endpoint) {
        super(endpoint);
    }

    @Override
    protected void doProcess(Exchange exchange, MeterEndpoint endpoint, MetricRegistry registry, String metricsName) throws Exception {
        Message in = exchange.getIn();
        Meter meter = registry.meter(metricsName);
        Long mark = endpoint.getMark();
        Long finalMark = getLongHeader(in, HEADER_METER_MARK, mark);
        if (finalMark == null) {
            meter.mark();
        } else {
            meter.mark(finalMark);
        }
    }
}
