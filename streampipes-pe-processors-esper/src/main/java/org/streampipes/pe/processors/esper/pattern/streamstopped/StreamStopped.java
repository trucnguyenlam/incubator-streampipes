package org.streampipes.pe.processors.esper.pattern.streamstopped;

import org.streampipes.wrapper.esper.EsperEventEngine;

import java.util.List;

public class StreamStopped extends EsperEventEngine<StreamStoppedParameter> {

	protected List<String> statements(final StreamStoppedParameter params) {
		String inName = params.getInputStreamParams().get(0).getInName();
		String topic = params.getTopic();

		String epl = "select '" + topic + "' as topic, current_timestamp as timestamp from pattern[every a="
				+ fixEventName(inName) + " -> timer:interval(6 sec) and not b=" + fixEventName(inName) + "]";

		return makeStatementList(epl);

	}
}