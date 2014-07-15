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
package com.google.devtools.build.lib.standalone;

import com.google.devtools.build.lib.actions.ActionContextConsumer;
import com.google.devtools.build.lib.actions.ActionContextProvider;
import com.google.devtools.build.lib.blaze.BlazeModule;

/**
 * StandaloneModule provides pluggable functionality for blaze.
 */
public class StandaloneModule extends BlazeModule {
  private final ActionContextProvider actionContextProvider = new StandaloneContextProvider();
  private final ActionContextConsumer actionContextConsumer = new StandaloneContextConsumer();
  
  /**
   * Returns the action context provider the module contributes to Blaze, if any.
   */
  @Override
  public ActionContextProvider getActionContextProvider() {
    return actionContextProvider;
  }

  /**
   * Returns the action context consumer the module contributes to Blaze, if any.
   */
  @Override
  public ActionContextConsumer getActionContextConsumer() {
    return actionContextConsumer;
  }

}
