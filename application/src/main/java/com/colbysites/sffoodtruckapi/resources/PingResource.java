package com.colbysites.sffoodtruckapi.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

  @ApiOperation(value = "Ping the server", response = String.class)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Successfully pinged application")
  })
  @GET
  public String ping() {
    return "PONG";
  }
}
