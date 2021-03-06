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
package org.apache.camel.metrics;

import com.codahale.metrics.MetricRegistry;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultProducer;
import org.apache.camel.util.ObjectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.apache.camel.metrics.MetricsComponent.HEADER_METRIC_NAME;
import static org.apache.camel.metrics.MetricsComponent.HEADER_PERFIX;


public abstract class AbstractMetricsProducer<T extends AbstractMetricsEndpoint> extends DefaultProducer {

    public static final String HEADER_PATTERN = HEADER_PERFIX + "*";
    private static final Logger LOG = LoggerFactory.getLogger(AbstractMetricsProducer.class);

    public AbstractMetricsProducer(T endpoint) {
        super(endpoint);
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        @SuppressWarnings("unchecked")
        T endpoint = (T) getEndpoint();
        Message in = exchange.getIn();
        String defaultMetricsName = endpoint.getMetricsName();
        String finalMetricsName = getMetricsName(in, defaultMetricsName);
        MetricRegistry registry = endpoint.getRegistry();
        try {
            doProcess(exchange, endpoint, registry, finalMetricsName);
        } catch (Exception e) {
            LOG.warn("Failed to produce metrics for {} in {} - {}", new Object[] {
                finalMetricsName, getClass().getSimpleName(), e.getMessage() });
        }
        clearMetricsHeaders(in);
    }

    protected abstract void doProcess(Exchange exchange, T endpoint, MetricRegistry registry, String metricsName) throws Exception;

    public String getMetricsName(Message in, String defaultValue) {
        return getStringHeader(in, HEADER_METRIC_NAME, defaultValue);
    }

    public String getStringHeader(Message in, String header, String defaultValue) {
        String headerValue = in.getHeader(header, String.class);
        return ObjectHelper.isNotEmpty(headerValue) ? headerValue : defaultValue;
    }

    public Long getLongHeader(Message in, String header, Long defaultValue) {
        return in.getHeader(header, defaultValue, Long.class);
    }

    protected boolean clearMetricsHeaders(Message in) {
        return in.removeHeaders(HEADER_PATTERN);
    }
}
