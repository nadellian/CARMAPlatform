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

package gov.dot.fhwa.saxton.carma.guidance.util.trajectoryconverter;

import gov.dot.fhwa.saxton.carma.guidance.maneuvers.FutureLateralManeuver;
import gov.dot.fhwa.saxton.carma.guidance.maneuvers.FutureLongitudinalManeuver;
import gov.dot.fhwa.saxton.carma.guidance.maneuvers.IComplexManeuver;
import gov.dot.fhwa.saxton.carma.guidance.maneuvers.LateralManeuver;
import gov.dot.fhwa.saxton.carma.guidance.maneuvers.LongitudinalManeuver;
import gov.dot.fhwa.saxton.carma.guidance.trajectory.Trajectory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.ros.rosjava_geometry.Transform;
import org.ros.rosjava_geometry.Vector3;

import cav_msgs.LocationECEF;
import cav_msgs.LocationOffsetECEF;
import gov.dot.fhwa.saxton.carma.geometry.cartesian.Point2D;
import gov.dot.fhwa.saxton.carma.geometry.cartesian.Point3D;

/**
 * Interface for objects responsible for converting Trajectories into paths described as a sequence for ECEF points for use in Mobility Messages
 * Users of this interface should call the convertToPath function
 * then call the pathToMessage function to convert the path into a MobilityPath message as needed.
 * 
 * Paths are described internally as a list of ECEFPointStamped objects
 * Simple Longitudinal Maneuver motion is estimated using the basic kinematic equations of motion
 * Lateral motion is approximated using a cubic function calculated based on the start and end distance of a maneuver
 * Complex maneuvers are treated as stead speed maneuvers operating at the average of the min and max speeds of that maneuver
 * 
 * Crosstrack distance is preserved across all traversed route segments
 * Note: This class will become less reliable when dealing with lane changes along tight curves and changing lane widths
 */
public interface ITrajectoryConverter {

  /**
   * Converts the provided trajectory and starting configuration into a list of (downtrack, crosstrack) points with associated time stamps
   * 
   * This function determines all the point downtrack distances using simple longitudinal maneuvers and kinematic equations.
   * Then the longitudinal maneuvers are used to shift the crosstrack values of each point
   * Then any complex maneuvers are added to the path
   * Finally all points are converted into the ECEF frame
   * 
   * Uses the TrajectoryConverter's configured max path size
   * 
   * @param traj The trajectory to convert
   * @param startTimeMs The starting time for this path in ms
   * @param downtrack Current downtrack distance on route
   * @param crosstrack Current crosstrack on route
   * @param currentSegmentIdx The current route segment index
   * @param segDowntrack The current downtrack relative to the current segment start
   * @param lane The current lane index
   * 
   * @return A list of downtrack, crosstrack points associated with time stamps and segments
   */
  List<RoutePointStamped> convertToPath(Trajectory traj, long startTimeMs,
  double downtrack, double crosstrack,
  int currentSegmentIdx, double segDowntrack, int lane);

  /**
   * Converts the provided trajectory and starting configuration into a list of (downtrack, crosstrack) points with associated time stamps
   * 
   * This function determines all the point downtrack distances using simple longitudinal maneuvers and kinematic equations.
   * Then the longitudinal maneuvers are used to shift the crosstrack values of each point
   * Then any complex maneuvers are added to the path
   * Finally all points are converted into the ECEF frame
   * 
   * @param traj The trajectory to convert
   * @param startTimeMs The starting time for this path in ms
   * @param downtrack Current downtrack distance on route, m
   * @param crosstrack Current crosstrack on route, m
   * @param currentSegmentIdx The current route segment index
   * @param segDowntrack The current downtrack distance relative to the current segment start, m
   * @param lane The current lane index
   * @param maxPointsInPath The maximum number of points to include in the path, not to exceed the configured value
   * 
   * @return A list of downtrack, crosstrack points associated with time stamps and segments
   */
  List<RoutePointStamped> convertToPath(Trajectory traj, long startTimeMs,
   double downtrack, double crosstrack,
   int currentSegmentIdx, double segDowntrack, int lane, int maxPointsInPath);

  /**
   * Converts the provided trajectory and starting configuration into a list of (downtrack, crosstrack) points with associated time stamps
   * 
   * This function determines all the point downtrack distances using simple longitudinal maneuvers and kinematic equations.
   * Then the longitudinal maneuvers are used to shift the crosstrack values of each point
   * Then any complex maneuvers are added to the path
   * Finally all points are converted into the ECEF frame
   * 
   * This function uses current data from Guidance Route Service.
   * 
   * @param traj The trajectory to convert
   * 
   * @return A list of downtrack, crosstrack points associated with time stamps and segments
   */
  List<RoutePointStamped> convertToPath(Trajectory traj);

  /**
   * Converts the provided trajectory and starting configuration into a list of (downtrack, crosstrack) points with associated time stamps
   * 
   * This function determines all the point downtrack distances using simple longitudinal maneuvers and kinematic equations.
   * Then the longitudinal maneuvers are used to shift the crosstrack values of each point
   * Then any complex maneuvers are added to the path
   * Finally all points are converted into the ECEF frame
   * 
   * Uses the TrajectoryConverter's configured max path size
   * 
   * @param traj The trajectory to convert
   * @param startPoint the point one timestep prior to the beginning of this trajectory
   * 
   * @return A list of downtrack, crosstrack points associated with time stamps and segments
   */
  List<RoutePointStamped> convertToPath(Trajectory traj, RoutePointStamped startPoint);

  /**
   * Converts the provided trajectory and starting configuration into a list of (downtrack, crosstrack) points with associated time stamps
   * 
   * This function determines all the point downtrack distances using simple longitudinal maneuvers and kinematic equations.
   * Then the longitudinal maneuvers are used to shift the crosstrack values of each point
   * Then any complex maneuvers are added to the path
   * Finally all points are converted into the ECEF frame
   * 
   * @param traj The trajectory to convert
   * @param maxPointsInPath The maximum number of points to include in the path, not to exceed the configured value
   * 
   * @return A list of downtrack, crosstrack points associated with time stamps and segments
   */
  List<RoutePointStamped> convertToPath(Trajectory traj, int maxPointsInPath);

  /**
   * Converts the provided trajectory and starting configuration into a list of (downtrack, crosstrack) points with associated time stamps
   * 
   * This function determines all the point downtrack distances using simple longitudinal maneuvers and kinematic equations.
   * Then the longitudinal maneuvers are used to shift the crosstrack values of each point
   * Then any complex maneuvers are added to the path
   * Finally all points are converted into the ECEF frame
   * 
   * @param traj The trajectory to convert
   * @param startPoint the point one timestep prior to the beginning of this trajectory
   * @param maxPointsInPath The maximum number of points to include in the path, not to exceed the configured value
   * 
   * @return A list of downtrack, crosstrack points associated with time stamps and segments
   */
  List<RoutePointStamped> convertToPath(Trajectory traj, RoutePointStamped startPoint, int maxPointsInPath);

  /**
   * Helper function for converting a List of RoutePoint2DStamped into List of ECEFPointStamped
   * 
   * @param path The list of RoutePoint2DStamped to be converted
   * 
   * @return The path described as ECEF points
   */
  List<ECEFPointStamped> toECEFPoints(List<RoutePointStamped> path);

   /**
   * Helper function for converting a cav_msgs.Trajectory into List of RoutePointStamped
   * 
   * @param trajMsg The message to be converted
   * @param currentSegmentIdx The current route segment index
   * @param segDowntrack the downtrack distance along the segment, m
   * 
   * @return The path described as points along a route
   */
  List<RoutePointStamped> messageToPath(cav_msgs.Trajectory trajMsg, int currentSegmentIdx, double segDowntrack);

   /**
   * Helper function for converting a cav_msgs.Trajectory into List of RoutePointStamped
   * 
   * This method uses current data from Guidance Route Service
   * 
   * @param trajMsg The message to be converted
   * 
   * @return The path described as points along a route
   */
  List<RoutePointStamped> messageToPath(cav_msgs.Trajectory trajMsg);
  
  /**
   * Function converts a path to a cav_msgs.Trajectory message using the provided message factory
   * 
   * @param path The list of ecef points and times which defines the path
   * 
   * @return A cav_msgs.Trajectory message. This message will be empty if the path was empty
   */
  cav_msgs.Trajectory pathToMessage(List<RoutePointStamped> path);

  /**
   * Function which converts and individual Simple Longitudinal Maneuver to a path based on starting configuration
   * This function is used internally in the convertToPath function
   * 
   * Uses the TrajectoryConverter's configured max path size
   * 
   * @param maneuver The maneuver to convert
   * @param path The list which will store the generated points
   * @param startingData The starting configuration of the vehicle
   */
  LongitudinalSimulationData addLongitudinalManeuverToPath(
    final LongitudinalManeuver maneuver, List<RoutePointStamped> path,
    final LongitudinalSimulationData startingData);

  /**
   * Function which converts and individual Simple Longitudinal Maneuver to a path based on starting configuration
   * This function is used internally in the convertToPath function
   * 
   * @param maneuver The maneuver to convert
   * @param path The list which will store the generated points
   * @param startingData The starting configuration of the vehicle
   * @param maxPointsInPath The maximum number of points to compute, not to exceed the configured value
   */
  LongitudinalSimulationData addLongitudinalManeuverToPath(
    final LongitudinalManeuver maneuver, List<RoutePointStamped> path,
    final LongitudinalSimulationData startingData, final int maxPointsInPath);
}