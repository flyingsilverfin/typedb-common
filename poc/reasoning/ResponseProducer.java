package grakn.common.poc.reasoning;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ResponseProducer implements grakn.common.poc.reasoning.execution.ResponseProducer {
    private final Set<List<Long>> produced;
    private final Iterator<List<Long>> traversalProducer;
    private final Set<Request> downstreamProducer;
    private Iterator<Request> downstreamProducerSelector;

    public ResponseProducer(Iterator<List<Long>> traversalProducer) {
        produced = new HashSet<>();
        this.traversalProducer = traversalProducer;
        downstreamProducer = new HashSet<>();
        downstreamProducerSelector = downstreamProducer.iterator();
    }

    public void recordProduced(List<Long> answer) {
        produced.add(answer);
    }

    public boolean hasProduced(List<Long> answer) {
        return produced.contains(answer);
    }

    public boolean hasTraversalProducer() {
        return traversalProducer.hasNext();
    }

    public Iterator<List<Long>> traversalProducer() {
        return traversalProducer;
    }

    public boolean hasDownstreamProducer() {
        return !downstreamProducer.isEmpty();
    }

    public Request nextDownstreamProducer() {
        if (!downstreamProducerSelector.hasNext()) downstreamProducerSelector = downstreamProducer.iterator();
        return downstreamProducerSelector.next();
    }

    public void addDownstreamProducer(final Request request) {
        assert !(downstreamProducer.contains(request)) : "downstream answer producer already contains this request";

        downstreamProducer.add(request);
        downstreamProducerSelector = downstreamProducer.iterator();
    }

    public void removeDownstreamProducer(final Request request) {
        boolean removed = downstreamProducer.remove(request);
        // only update the iterator when removing an element, to avoid resetting and reusing first request too often
        // note: this is a large performance win when processing large batches of requests
        if (removed) downstreamProducerSelector = downstreamProducer.iterator();
    }
}
