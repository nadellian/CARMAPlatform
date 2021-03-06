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

package gov.dot.fhwa.saxton.carma.plugins.platooning;

import cav_msgs.MobilityOperation;
import cav_msgs.MobilityRequest;
import cav_msgs.MobilityResponse;
import gov.dot.fhwa.saxton.carma.guidance.arbitrator.TrajectoryPlanningResponse;
import gov.dot.fhwa.saxton.carma.guidance.mobilityrouter.MobilityRequestResponse;
import gov.dot.fhwa.saxton.carma.guidance.trajectory.Trajectory;

public interface IPlatooningState extends Runnable {
    
    /**
     * Execute the plugin's planning algorithm and generate maneuvers in the supplied trajectory if possible.
     * @param traj The current partially planned Trajectory, which cannot be modified
     * @param expectedEntrySpeed The speed (in m/s) the vehicle is expected to have upon the start of the new trajectory
     * @return Trajectory planning response
     */
    public TrajectoryPlanningResponse planTrajectory(Trajectory traj, double expectedEntrySpeed);
    
    /**
     * Callback method to handle mobility requests which may result in
     * state changing, trajectory re-plan and platooning info updates. 
     * @param msg the detailed proposal from other vehicles
     * @return simple yes/no response to the incoming proposal
     */
    public MobilityRequestResponse onMobilityRequestMessgae(MobilityRequest msg);
    
    /**
     * Callback method to handle mobility operation.
     * @param msg the necessary operational info from other vehicles
     */
    public void onMobilityOperationMessage(MobilityOperation msg);
    
    /**
     * Callback method to handle mobility response.
     * @param msg response for the current plan from other vehicles
     */
    public void onMobilityResponseMessage(MobilityResponse msg);
}
