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
package org.apache.camel.metrics.counter;

import com.codahale.metrics.MetricRegistry;
import org.apache.camel.Producer;
import org.apache.camel.metrics.AbstractMetricsEndpoint;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;


@UriEndpoint(scheme = "metrics")
public class CounterEndpoint extends AbstractMetricsEndpoint {

    public static final String ENDPOINT_URI = "metrics:counter";

    @UriParam
    private Long increment;

    @UriParam
    private Long decrement;

    public CounterEndpoint(MetricRegistry registry, String metricsName) {
        super(registry, metricsName);
    }

    @Override
    public Producer createProducer() throws Exception {
        return new CounterProducer(this);
    }

    public Long getIncrement() {
        return increment;
    }

    public void setIncrement(Long increment) {
        this.increment = increment;
    }

    public Long getDecrement() {
        return decrement;
    }

    public void setDecrement(Long decrement) {
        this.decrement = decrement;
    }

    @Override
    protected String createEndpointUri() {
        return ENDPOINT_URI;
    }
}
