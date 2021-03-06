/*
 * Copyright (C) 2018-2019 LEIDOS.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package gov.dot.fhwa.saxton.carma.guidance.arbitrator;

/**
 * Events that the {@link ArbitratorStateMachine} is capable of responding to
 */
public enum ArbitratorEvent {
  INITIALIZE,
  FINISHED_TRAJECTORY_PLANNING,
  EXECUTING_COMPLEX_TRAJECTORY,
  TRAJECTORY_COMPLETION_ALERT,
  TRAJECTORY_FAILED_EXECUTION,
  COMPLEX_TRAJECTORY_COMPLETION_ALERT,
  CLEAN_RESTART
}
