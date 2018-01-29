package org.streampipes.manager.execution.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.streampipes.model.base.InvocableStreamPipesEntity;
import org.streampipes.model.client.pipeline.PipelineElementStatus;
import org.streampipes.model.client.pipeline.PipelineOperationStatus;

import java.util.List;
import java.util.Optional;

public class GraphSubmitter {

  private List<InvocableStreamPipesEntity> graphs;
  private String pipelineId;
  private String pipelineName;

  private final static Logger LOG = LoggerFactory.getLogger(GraphSubmitter.class);

  public GraphSubmitter(String pipelineId, String pipelineName, List<InvocableStreamPipesEntity> graphs) {
    this.graphs = graphs;
    this.pipelineId = pipelineId;
    this.pipelineName = pipelineName;
  }

  public PipelineOperationStatus invokeGraphs() {
    PipelineOperationStatus status = new PipelineOperationStatus();
    status.setPipelineId(pipelineId);
    status.setPipelineName(pipelineName);


    graphs.forEach(g -> status.addPipelineElementStatus(new HttpRequestBuilder(g).invoke()));
    status.setSuccess(!status.getElementStatus().stream().anyMatch(s -> !s.isSuccess()));

    if (status.isSuccess()) {
      status.setTitle("Pipeline " + pipelineName + " successfully started");
    } else {
      LOG.info("Could not start pipeline, initializing rollback...");
      rollbackInvokedPipelineElements(status);
      status.setTitle("Could not start pipeline " + pipelineName + ".");
    }
    return status;
  }

  private void rollbackInvokedPipelineElements(PipelineOperationStatus status) {
    for (PipelineElementStatus s : status.getElementStatus()) {
      if (s.isSuccess()) {
        Optional<InvocableStreamPipesEntity> graph = findGraph(s.getElementId());
        graph.ifPresent(g -> {
          LOG.info("Rolling back element " + g.getElementId());
          new HttpRequestBuilder(g).detach();
        });
      }
    }
  }

  private Optional<InvocableStreamPipesEntity> findGraph(String elementId) {
    return graphs.stream().filter(g -> g.getBelongsTo().equals(elementId)).findFirst();
  }

  public PipelineOperationStatus detachGraphs() {
    PipelineOperationStatus status = new PipelineOperationStatus();
    status.setPipelineId(pipelineId);

    graphs.forEach(g -> status.addPipelineElementStatus(new HttpRequestBuilder(g).detach()));
    status.setSuccess(!status.getElementStatus().stream().anyMatch(s -> !s.isSuccess()));

    if (status.isSuccess()) {
      status.setTitle("Pipeline " + pipelineName + " successfully stopped");
    } else {
      status.setTitle("Could not stop all pipeline elements of pipeline " + pipelineName + ".");
    }

    return status;
  }
}