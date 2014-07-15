// Copyright 2014 Google Inc. All rights reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.google.devtools.build.lib.exec;

import com.google.devtools.build.lib.actions.ActionExecutionContext;
import com.google.devtools.build.lib.actions.ActionExecutionException;
import com.google.devtools.build.lib.actions.ExecException;
import com.google.devtools.build.lib.actions.ExecutionStrategy;
import com.google.devtools.build.lib.actions.Executor;
import com.google.devtools.build.lib.view.SymlinkTreeAction;
import com.google.devtools.build.lib.view.SymlinkTreeActionContext;
import com.google.devtools.build.lib.view.config.BinTools;

/**
 * Implements SymlinkTreeAction by using the output service or by running an embedded script to
 * create the symlink tree.
 */
@ExecutionStrategy(contextType = SymlinkTreeActionContext.class)
public final class SymlinkTreeStrategy implements SymlinkTreeActionContext {
  private final OutputService outputService;
  private final BinTools binTools;

  public SymlinkTreeStrategy(OutputService outputService, BinTools binTools) {
    this.outputService = outputService;
    this.binTools = binTools;
  }

  @Override
  public void createSymlinks(SymlinkTreeAction action,
      ActionExecutionContext actionExecutionContext)
      throws ActionExecutionException, InterruptedException {
    Executor executor = actionExecutionContext.getExecutor();
    try {
      SymlinkTreeHelper helper = new SymlinkTreeHelper(
          action.getInputManifest().getExecPath(),
          action.getOutputManifest().getExecPath().getParentDirectory(), action.isFilesetTree());
      if (outputService != null && outputService.canCreateSymlinkTree()) {
        outputService.createSymlinkTree(action.getInputManifest().getPath(),
            action.getOutputManifest().getPath(),
            action.isFilesetTree(), helper.getSymlinkTreeRoot());
      } else {
        helper.createSymlinks(action, actionExecutionContext, binTools);
      }
    } catch (ExecException e) {
      throw e.toActionExecutionException(
          action.getProgressMessage(), executor.getVerboseFailures(), action);
    }
  }
}
