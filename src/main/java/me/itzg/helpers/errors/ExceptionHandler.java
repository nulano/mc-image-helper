package me.itzg.helpers.errors;

import lombok.extern.slf4j.Slf4j;
import me.itzg.helpers.McImageHelper;
import picocli.CommandLine;
import picocli.CommandLine.ExitCode;
import picocli.CommandLine.IExecutionExceptionHandler;
import picocli.CommandLine.IExitCodeExceptionMapper;
import picocli.CommandLine.ParseResult;

@Slf4j
public class ExceptionHandler implements IExecutionExceptionHandler {

  private final McImageHelper mcImageHelper;

  public ExceptionHandler(McImageHelper mcImageHelper) {
    this.mcImageHelper = mcImageHelper;
  }

  @Override
  public int handleExecutionException(Exception e, CommandLine commandLine,
      ParseResult parseResult) {

    if (!mcImageHelper.isSilent()) {
      if (e instanceof InvalidParameterException) {
        log.error("Invalid parameter provided for '{}' command: {}", commandLine.getCommandName(), e.getMessage());
        log.debug("Invalid parameter details", e);
      } else {
        log.error("'{}' command failed. Version is {}",
            commandLine.getCommandName(),
            McImageHelper.getVersion(),
            e);
      }
    }

    final IExitCodeExceptionMapper mapper = commandLine.getExitCodeExceptionMapper();
    return mapper != null ? mapper.getExitCode(e) : ExitCode.SOFTWARE;
  }
}
