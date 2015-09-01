package de.fzi.cep.sepa.model.util;

import java.util.List;
import java.util.stream.Collectors;

import de.fzi.cep.sepa.model.impl.EventStream;
import de.fzi.cep.sepa.model.impl.JmsTransportProtocol;
import de.fzi.cep.sepa.model.impl.KafkaTransportProtocol;
import de.fzi.cep.sepa.model.impl.TransportFormat;
import de.fzi.cep.sepa.model.impl.TransportProtocol;
import de.fzi.cep.sepa.model.impl.eventproperty.EventProperty;
import de.fzi.cep.sepa.model.impl.eventproperty.EventPropertyList;
import de.fzi.cep.sepa.model.impl.eventproperty.EventPropertyNested;
import de.fzi.cep.sepa.model.impl.eventproperty.EventPropertyPrimitive;
import de.fzi.cep.sepa.model.impl.output.AppendOutputStrategy;
import de.fzi.cep.sepa.model.impl.output.OutputStrategy;
import de.fzi.cep.sepa.model.impl.output.RenameOutputStrategy;
import de.fzi.cep.sepa.model.impl.quality.EventPropertyQualityDefinition;
import de.fzi.cep.sepa.model.impl.quality.EventPropertyQualityRequirement;
import de.fzi.cep.sepa.model.impl.staticproperty.FreeTextStaticProperty;
import de.fzi.cep.sepa.model.impl.staticproperty.MappingPropertyUnary;
import de.fzi.cep.sepa.model.impl.staticproperty.OneOfStaticProperty;
import de.fzi.cep.sepa.model.impl.staticproperty.Option;
import de.fzi.cep.sepa.model.impl.staticproperty.StaticProperty;

public class Cloner {

	public OutputStrategy outputStrategy(OutputStrategy other)
	{
		if (other instanceof RenameOutputStrategy) return new RenameOutputStrategy((RenameOutputStrategy) other);
		else return new AppendOutputStrategy((AppendOutputStrategy) other);
	}

	public StaticProperty staticProperty(StaticProperty o) {
		if (o instanceof FreeTextStaticProperty) return new FreeTextStaticProperty((FreeTextStaticProperty) o);
		else if (o instanceof OneOfStaticProperty) return new OneOfStaticProperty((OneOfStaticProperty) o);
		else return new MappingPropertyUnary((MappingPropertyUnary) o);
	}
	
	public List<TransportProtocol> protocols(List<TransportProtocol> protocols)
	{
		return protocols.stream().map(o -> protocol(o)).collect(Collectors.toList());
	}
	
	public TransportProtocol protocol(TransportProtocol protocol) {
		if (protocol instanceof KafkaTransportProtocol) return new KafkaTransportProtocol((KafkaTransportProtocol) protocol);
		else return new JmsTransportProtocol((JmsTransportProtocol) protocol);
	}
	
	public EventProperty property(EventProperty o) {
		if (o instanceof EventPropertyPrimitive) return new EventPropertyPrimitive((EventPropertyPrimitive) o);
		else if (o instanceof EventPropertyList) return new EventPropertyList((EventPropertyList) o);
		else return new EventPropertyNested((EventPropertyNested) o);
	}

	public EventPropertyQualityRequirement qualityreq(EventPropertyQualityRequirement o) {
		// TODO Auto-generated method stub
		return null;
	}

	public EventPropertyQualityDefinition qualitydef(EventPropertyQualityDefinition o) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<EventStream> streams(List<EventStream> eventStreams) {
		return eventStreams.stream().map(s -> new EventStream(s)).collect(Collectors.toList());
	}

	public List<OutputStrategy> strategies(List<OutputStrategy> outputStrategies) {
		return outputStrategies.stream().map(o -> outputStrategy(o)).collect(Collectors.toList());
	}

	public List<StaticProperty> staticProperties(
			List<StaticProperty> staticProperties) {
		return staticProperties.stream().map(o -> staticProperty(o)).collect(Collectors.toList());
	}

	public List<TransportFormat> transportFormats(
			List<TransportFormat> transportFormats) {
		return transportFormats.stream().map(t -> new TransportFormat(t)).collect(Collectors.toList());
	}

	public List<EventProperty> properties(List<EventProperty> eventProperties) {
		return eventProperties.stream().map(o -> new Cloner().property(o)).collect(Collectors.toList());
	}

	public List<EventPropertyQualityRequirement> reqEpQualitities(
			List<EventPropertyQualityRequirement> requiresEventPropertyQualities) {
		return requiresEventPropertyQualities.stream().map(o -> new Cloner().qualityreq(o)).collect(Collectors.toList());
	}

	public List<EventPropertyQualityDefinition> provEpQualities(
			List<EventPropertyQualityDefinition> eventPropertyQualities) {
		return eventPropertyQualities.stream().map(o -> new Cloner().qualitydef(o)).collect(Collectors.toList());
	}

	public List<Option> options(List<Option> options) {
		return options.stream().map(o -> new Option(o)).collect(Collectors.toList());
	}
	
}