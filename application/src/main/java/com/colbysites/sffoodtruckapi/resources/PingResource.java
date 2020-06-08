package com.colbysites.sffoodtruckapi.resources;

import io.swagger.annotations.Api;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Api(value = "ping", description = "Simple ping resource to act as a health check")
@Path("/ping")
@Produces(MediaType.TEXT_PLAIN)
public class PingResource {
  @Inject
  public PingResource() {
  }

  @GET
  public String ping() {
    return "PONG";
  }
}
